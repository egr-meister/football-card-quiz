package com.footballcardquiz.app.data.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.footballcardquiz.app.data.model.AppData
import com.footballcardquiz.app.data.model.BestScores
import com.footballcardquiz.app.data.model.MatchScheduleCache
import com.footballcardquiz.app.data.model.MatchScheduleSettings
import com.footballcardquiz.app.data.model.QuizResult
import com.footballcardquiz.app.data.model.Settings
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.serialization.json.Json

// A single DataStore Preferences instance holds one JSON string for AppData.
private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(
    name = "football_card_quiz_store"
)

/**
 * Single local storage repository. Stores the whole [AppData] as one JSON
 * string in DataStore Preferences. All reads merge with defaults and any
 * corrupted / partial JSON falls back to a valid default object — the app
 * never crashes because of storage.
 */
class AppDataStore(private val context: Context) {

    private val json = Json {
        ignoreUnknownKeys = true      // tolerate schema changes / legacy fields
        encodeDefaults = true
        coerceInputValues = true      // null -> default for non-null fields
        isLenient = true
    }

    private val appDataKey = stringPreferencesKey("app_data_json")

    /** Reactive stream of the full app data, always non-null and valid. */
    val appDataFlow: Flow<AppData> = context.dataStore.data
        .catch { emit(androidx.datastore.preferences.core.emptyPreferences()) }
        .map { prefs -> decode(prefs[appDataKey]) }

    private fun decode(raw: String?): AppData {
        if (raw.isNullOrBlank()) return AppData()
        return try {
            json.decodeFromString(AppData.serializer(), raw)
        } catch (e: Exception) {
            // Corrupted JSON -> safe default, never crash.
            AppData()
        }
    }

    private suspend fun update(transform: (AppData) -> AppData) {
        try {
            context.dataStore.edit { prefs ->
                val existing = decode(prefs[appDataKey])
                val updated = transform(existing)
                prefs[appDataKey] = json.encodeToString(AppData.serializer(), updated)
            }
        } catch (e: Exception) {
            // Swallow storage errors; UI keeps working with in-memory state.
        }
    }

    // ---- Quiz results / history ----

    suspend fun addQuizResult(result: QuizResult) = update { data ->
        val newList = (listOf(result) + data.quizResults).take(200) // cap history
        val newBest = recomputeBest(data.bestScores, result)
        data.copy(quizResults = newList, bestScores = newBest)
    }

    suspend fun deleteQuizResult(id: String) = update { data ->
        data.copy(quizResults = data.quizResults.filterNot { it.id == id })
    }

    suspend fun clearHistory() = update { data ->
        data.copy(quizResults = emptyList())
    }

    // ---- Best scores ----

    private fun recomputeBest(best: BestScores, r: QuizResult): BestScores {
        val s = r.scorePercent
        return best.copy(
            bestOverall = maxOf(best.bestOverall, s),
            bestEasy = if (r.difficulty.name == "Easy") maxOf(best.bestEasy, s) else best.bestEasy,
            bestMedium = if (r.difficulty.name == "Medium") maxOf(best.bestMedium, s) else best.bestMedium,
            bestHard = if (r.difficulty.name == "Hard") maxOf(best.bestHard, s) else best.bestHard,
            bestFootballRules = if (r.category.name == "FootballRules") maxOf(best.bestFootballRules, s) else best.bestFootballRules,
            bestPlayerPositions = if (r.category.name == "PlayerPositions") maxOf(best.bestPlayerPositions, s) else best.bestPlayerPositions,
            bestRefereeCards = if (r.category.name == "RefereeCards") maxOf(best.bestRefereeCards, s) else best.bestRefereeCards,
            bestMatchSituations = if (r.category.name == "MatchSituations") maxOf(best.bestMatchSituations, s) else best.bestMatchSituations,
            bestFieldAndEquipment = if (r.category.name == "FieldAndEquipment") maxOf(best.bestFieldAndEquipment, s) else best.bestFieldAndEquipment,
            bestGeneralFootball = if (r.category.name == "GeneralFootball") maxOf(best.bestGeneralFootball, s) else best.bestGeneralFootball
        )
    }

    suspend fun resetBestScores() = update { data ->
        data.copy(bestScores = BestScores())
    }

    // ---- Settings ----

    suspend fun updateSettings(transform: (Settings) -> Settings) = update { data ->
        data.copy(settings = transform(data.settings))
    }

    suspend fun setOnboardingCompleted(completed: Boolean) = update { data ->
        data.copy(settings = data.settings.copy(onboardingCompleted = completed))
    }

    suspend fun updateMatchScheduleSettings(transform: (MatchScheduleSettings) -> MatchScheduleSettings) =
        update { data ->
            data.copy(
                settings = data.settings.copy(
                    matchSchedule = transform(data.settings.matchSchedule)
                )
            )
        }

    // ---- Match cache ----

    suspend fun updateMatchCache(cache: MatchScheduleCache) = update { data ->
        data.copy(matchScheduleCache = cache)
    }

    suspend fun clearMatchCache() = update { data ->
        data.copy(matchScheduleCache = MatchScheduleCache())
    }

    // ---- Global reset ----

    suspend fun resetAll() = update { AppData() }
}
