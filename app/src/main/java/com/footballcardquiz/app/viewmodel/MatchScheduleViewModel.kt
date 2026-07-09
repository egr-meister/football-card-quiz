package com.footballcardquiz.app.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.footballcardquiz.app.data.local.AppDataStore
import com.footballcardquiz.app.data.model.MatchScheduleCache
import com.footballcardquiz.app.data.model.MatchScheduleSettings
import com.footballcardquiz.app.data.model.MatchSource
import com.footballcardquiz.app.data.model.NormalizedMatch
import com.footballcardquiz.app.data.remote.DemoMatches
import com.footballcardquiz.app.data.remote.FootballDataRepository
import com.footballcardquiz.app.util.AppDateTime
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

sealed interface MatchScheduleUi {
    data object Loading : MatchScheduleUi
    data class Content(
        val matches: List<NormalizedMatch>,
        val source: MatchSource,
        val message: String,
        val lastUpdated: String,
        val dateFrom: String,
        val dateTo: String,
        val usingDefaultWindow: Boolean
    ) : MatchScheduleUi
}

/**
 * Loads the secondary Match Schedule. Priority on open:
 *  1) show cached matches if present (instant, offline-safe);
 *  2) otherwise show demo matches.
 * Manual refresh calls the API (only if a token exists and API is enabled),
 * caches success, and falls back to cache or demo on failure. No auto-polling.
 */
class MatchScheduleViewModel(
    private val store: AppDataStore,
    private val repo: FootballDataRepository
) : ViewModel() {

    private val _ui = MutableStateFlow<MatchScheduleUi>(MatchScheduleUi.Loading)
    val ui: StateFlow<MatchScheduleUi> = _ui.asStateFlow()

    val hasValidToken: Boolean get() = repo.hasValidToken

    private var loadedOnce = false

    /** Called when the screen first appears. Shows cache/demo without hitting the API. */
    fun loadInitial() {
        if (loadedOnce) return
        loadedOnce = true
        viewModelScope.launch {
            val data = store.appDataFlow.first()
            val settings = data.settings.matchSchedule
            val cache = data.matchScheduleCache
            val (from, to) = repo.resolveWindow(settings)

            if (cache.cachedMatches.isNotEmpty()) {
                _ui.value = MatchScheduleUi.Content(
                    matches = cache.cachedMatches,
                    source = MatchSource.Cache,
                    message = if (cache.lastError.isNotBlank())
                        cache.lastError else "Showing your last saved matches.",
                    lastUpdated = cache.lastUpdatedAt.ifBlank { "—" },
                    dateFrom = cache.lastDateFrom.ifBlank { from },
                    dateTo = cache.lastDateTo.ifBlank { to },
                    usingDefaultWindow = isDefaultWindow(settings)
                )
            } else {
                // No cache: show demo (or fetch on first open if token exists).
                if (repo.hasValidToken && settings.apiEnabled && !settings.useDemoData) {
                    refresh()
                } else {
                    showDemo(settings, "Showing demo matches. Add an API token to load live data.")
                }
            }
        }
    }

    fun refresh() {
        viewModelScope.launch {
            _ui.value = MatchScheduleUi.Loading
            val data = store.appDataFlow.first()
            val settings = data.settings.matchSchedule
            val cache = data.matchScheduleCache
            val (from, to) = repo.resolveWindow(settings)

            val result = repo.fetchMatches(settings)

            if (result.ok && result.usedDemoData) {
                // Demo path (no token / demo toggle / api off)
                _ui.value = MatchScheduleUi.Content(
                    matches = result.matches,
                    source = MatchSource.Demo,
                    message = result.error,
                    lastUpdated = AppDateTime.nowReadable(),
                    dateFrom = from, dateTo = to,
                    usingDefaultWindow = isDefaultWindow(settings)
                )
            } else if (result.ok) {
                // Real API success: cache it.
                val newCache = MatchScheduleCache(
                    cachedMatches = result.matches,
                    lastUpdatedAt = AppDateTime.nowReadable(),
                    lastError = "",
                    lastDateFrom = from,
                    lastDateTo = to
                )
                store.updateMatchCache(newCache)
                _ui.value = MatchScheduleUi.Content(
                    matches = result.matches,
                    source = MatchSource.Api,
                    message = if (result.matches.isEmpty())
                        "No matches were found for this date window." else "",
                    lastUpdated = newCache.lastUpdatedAt,
                    dateFrom = from, dateTo = to,
                    usingDefaultWindow = isDefaultWindow(settings)
                )
            } else {
                // API failed: fall back to cache, else demo.
                if (cache.cachedMatches.isNotEmpty()) {
                    _ui.value = MatchScheduleUi.Content(
                        matches = cache.cachedMatches,
                        source = MatchSource.Cache,
                        message = "${result.error} Showing cached matches.",
                        lastUpdated = cache.lastUpdatedAt.ifBlank { "—" },
                        dateFrom = cache.lastDateFrom.ifBlank { from },
                        dateTo = cache.lastDateTo.ifBlank { to },
                        usingDefaultWindow = isDefaultWindow(settings)
                    )
                } else {
                    showDemo(settings, "${result.error} Showing demo matches.")
                }
            }
        }
    }

    private fun showDemo(settings: MatchScheduleSettings, message: String) {
        val (from, to) = repo.resolveWindow(settings)
        val demo = DemoMatches.matches().let { list ->
            val c = settings.competitionCode.trim()
            if (c.isBlank()) list
            else list.filter { it.competitionCode.equals(c, ignoreCase = true) }
        }
        _ui.value = MatchScheduleUi.Content(
            matches = demo,
            source = MatchSource.Demo,
            message = message,
            lastUpdated = AppDateTime.nowReadable(),
            dateFrom = from, dateTo = to,
            usingDefaultWindow = isDefaultWindow(settings)
        )
    }

    private fun isDefaultWindow(settings: MatchScheduleSettings): Boolean =
        !AppDateTime.isRangeValid(settings.dateFrom, settings.dateTo)

    /** Allow the screen to re-fetch after settings change. */
    fun forceReloadFromSettings() {
        loadedOnce = false
        loadInitial()
    }
}
