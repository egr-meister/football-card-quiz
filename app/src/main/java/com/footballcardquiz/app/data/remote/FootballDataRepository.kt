package com.footballcardquiz.app.data.remote

import com.footballcardquiz.app.BuildConfig
import com.footballcardquiz.app.data.model.FootballApiResult
import com.footballcardquiz.app.data.model.MatchScheduleSettings
import com.footballcardquiz.app.data.model.MatchSource
import com.footballcardquiz.app.data.model.NormalizedMatch
import com.footballcardquiz.app.util.AppDateTime
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import retrofit2.HttpException
import java.io.IOException

/**
 * Isolated repository for the secondary Match Schedule feature.
 * All football-data.org access lives here. This class never throws into the
 * UI: every call returns a [FootballApiResult] with a friendly message.
 *
 * Token handling:
 *  - Reads token + base URL from BuildConfig (set via local.properties).
 *  - If the token is missing/empty/placeholder, returns demo data.
 *  - The token is sent only as an X-Auth-Token header and is NEVER logged.
 */
class FootballDataRepository {

    private val placeholderToken = "your_api_token_here"

    private val token: String = BuildConfig.FOOTBALL_DATA_API_TOKEN.trim()
    private val baseUrl: String = normalizeBaseUrl(BuildConfig.FOOTBALL_API_BASE_URL)

    val hasValidToken: Boolean
        get() = token.isNotBlank() && token != placeholderToken

    private fun normalizeBaseUrl(raw: String): String {
        val safe = raw.ifBlank { "https://api.football-data.org/v4" }
        return if (safe.endsWith("/")) safe else "$safe/"
    }

    private val json = Json {
        ignoreUnknownKeys = true
        coerceInputValues = true
        isLenient = true
    }

    private val api: FootballDataApi by lazy {
        // The auth token is attached only as a request header and is never logged.
        val client = OkHttpClient.Builder()
            .addInterceptor { chain ->
                val requestBuilder = chain.request().newBuilder()
                if (hasValidToken) {
                    requestBuilder.addHeader("X-Auth-Token", token)
                }
                chain.proceed(requestBuilder.build())
            }
            .connectTimeout(15, java.util.concurrent.TimeUnit.SECONDS)
            .readTimeout(20, java.util.concurrent.TimeUnit.SECONDS)
            .build()

        Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(client)
            .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
            .build()
            .create(FootballDataApi::class.java)
    }

    /** Resolve the effective date window from settings (defaults to today..today+9). */
    fun resolveWindow(settings: MatchScheduleSettings): Pair<String, String> {
        val from = settings.dateFrom
        val to = settings.dateTo
        return if (AppDateTime.isRangeValid(from, to)) {
            from to to
        } else {
            AppDateTime.today() to AppDateTime.todayPlus(9)
        }
    }

    /**
     * Fetch matches for the given settings. Returns a stable result object.
     * Never throws. Applies local competition-code filtering when set.
     */
    suspend fun fetchMatches(settings: MatchScheduleSettings): FootballApiResult =
        withContext(Dispatchers.IO) {
            val (from, to) = resolveWindow(settings)

            // Demo path: no token, demo toggle on, or API disabled.
            if (!settings.apiEnabled || settings.useDemoData || !hasValidToken) {
                val message = when {
                    !settings.apiEnabled -> "API is turned off in settings. Showing demo matches."
                    settings.useDemoData -> "Demo data is enabled in settings. Showing demo matches."
                    else -> "API token is not configured. Showing demo matches."
                }
                return@withContext FootballApiResult(
                    ok = true,
                    matches = filterByCompetition(DemoMatches.matches(), settings.competitionCode),
                    error = message,
                    usedDemoData = true
                )
            }

            try {
                val response = api.getMatches(from, to)
                val normalized = normalize(response, MatchSource.Api)
                FootballApiResult(
                    ok = true,
                    matches = filterByCompetition(normalized, settings.competitionCode),
                    error = "",
                    usedDemoData = false
                )
            } catch (e: HttpException) {
                val msg = when (e.code()) {
                    429 -> "API request limit reached. Showing cached or demo data if available."
                    403, 401 -> "API access was refused for this request. Check your token or plan."
                    else -> "Could not load the latest matches (server responded ${e.code()})."
                }
                FootballApiResult(ok = false, matches = emptyList(), error = msg, usedDemoData = false)
            } catch (e: IOException) {
                FootballApiResult(
                    ok = false, matches = emptyList(),
                    error = "No internet connection. Showing cached or demo data if available.",
                    usedDemoData = false
                )
            } catch (e: Exception) {
                FootballApiResult(
                    ok = false, matches = emptyList(),
                    error = "The match data could not be read. Showing cached or demo data if available.",
                    usedDemoData = false
                )
            }
        }

    private fun filterByCompetition(
        matches: List<NormalizedMatch>,
        code: String
    ): List<NormalizedMatch> {
        val c = code.trim()
        if (c.isBlank()) return matches
        return matches.filter { it.competitionCode.equals(c, ignoreCase = true) }
    }

    /** Convert DTOs into safe NormalizedMatch objects; missing fields become fallbacks. */
    private fun normalize(dto: MatchesResponseDto, source: MatchSource): List<NormalizedMatch> {
        val list = dto.matches ?: return emptyList()
        return list.mapNotNull { m ->
            try {
                val (date, time) = AppDateTime.parseUtcToLocal(m.utcDate)
                NormalizedMatch(
                    id = (m.id?.toString() ?: "").ifBlank { "match-${m.hashCode()}" },
                    utcDate = m.utcDate.orEmpty(),
                    date = date,
                    time = time,
                    competitionName = m.competition?.name.orEmpty(),
                    competitionCode = m.competition?.code.orEmpty(),
                    homeTeam = m.homeTeam?.name ?: m.homeTeam?.shortName ?: "Unknown",
                    awayTeam = m.awayTeam?.name ?: m.awayTeam?.shortName ?: "Unknown",
                    status = m.status.orEmpty(),
                    homeScore = m.score?.fullTime?.home,
                    awayScore = m.score?.fullTime?.away,
                    winner = m.score?.winner.orEmpty(),
                    source = source
                )
            } catch (e: Exception) {
                null // skip a broken entry rather than crash the whole list
            }
        }
    }
}
