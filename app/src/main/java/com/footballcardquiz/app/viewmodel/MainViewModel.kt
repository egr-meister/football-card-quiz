package com.footballcardquiz.app.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.footballcardquiz.app.data.local.AppDataStore
import com.footballcardquiz.app.data.model.AppData
import com.footballcardquiz.app.data.model.MatchScheduleSettings
import com.footballcardquiz.app.data.model.QuizCategory
import com.footballcardquiz.app.data.model.QuizDifficulty
import com.footballcardquiz.app.data.model.QuizResult
import com.footballcardquiz.app.data.model.Settings
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

/**
 * Holds the reactive app-wide state (settings, best scores, history, match cache)
 * and exposes safe operations. All writes are fire-and-forget coroutines.
 */
class MainViewModel(private val store: AppDataStore) : ViewModel() {

    val appData: StateFlow<AppData> = store.appDataFlow
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), AppData())

    fun completeOnboarding() = viewModelScope.launch { store.setOnboardingCompleted(true) }
    fun showOnboardingAgain() = viewModelScope.launch { store.setOnboardingCompleted(false) }

    fun saveResult(result: QuizResult) = viewModelScope.launch { store.addQuizResult(result) }

    fun deleteResult(id: String) = viewModelScope.launch { store.deleteQuizResult(id) }
    fun clearHistory() = viewModelScope.launch { store.clearHistory() }
    fun resetBestScores() = viewModelScope.launch { store.resetBestScores() }
    fun clearMatchCache() = viewModelScope.launch { store.clearMatchCache() }
    fun resetAll() = viewModelScope.launch { store.resetAll() }

    fun setDefaultDifficulty(d: QuizDifficulty) = viewModelScope.launch {
        store.updateSettings { it.copy(defaultDifficulty = d) }
    }

    fun setDefaultCategory(c: QuizCategory?) = viewModelScope.launch {
        store.updateSettings { it.copy(defaultCategory = c) }
    }

    fun setCompactMode(enabled: Boolean) = viewModelScope.launch {
        store.updateSettings { it.copy(compactMode = enabled) }
    }

    fun updateMatchSettings(transform: (MatchScheduleSettings) -> MatchScheduleSettings) =
        viewModelScope.launch { store.updateMatchScheduleSettings(transform) }

    fun findResult(id: String?): QuizResult? =
        appData.value.quizResults.firstOrNull { it.id == id }
}
