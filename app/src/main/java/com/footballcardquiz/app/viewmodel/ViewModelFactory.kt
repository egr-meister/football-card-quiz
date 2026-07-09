package com.footballcardquiz.app.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.footballcardquiz.app.AppContainer

/**
 * Single factory that can construct every ViewModel in the app from the
 * shared [AppContainer]. Keeps wiring simple and stable.
 */
@Suppress("UNCHECKED_CAST")
class AppViewModelFactory(private val container: AppContainer) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(MainViewModel::class.java) ->
                MainViewModel(container.appDataStore) as T
            modelClass.isAssignableFrom(QuizViewModel::class.java) ->
                QuizViewModel(container.appDataStore) as T
            modelClass.isAssignableFrom(MatchScheduleViewModel::class.java) ->
                MatchScheduleViewModel(container.appDataStore, container.footballRepository) as T
            else -> throw IllegalArgumentException("Unknown ViewModel: ${modelClass.name}")
        }
    }
}
