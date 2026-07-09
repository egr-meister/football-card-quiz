package com.footballcardquiz.app

import android.app.Application
import com.footballcardquiz.app.data.local.AppDataStore
import com.footballcardquiz.app.data.remote.FootballDataRepository

/**
 * Application entry point. Holds simple, manually-created singletons.
 * No Firebase, analytics, background services, or workers are started here.
 */
class FootballCardQuizApp : Application() {

    lateinit var container: AppContainer
        private set

    override fun onCreate() {
        super.onCreate()
        container = AppContainer(this)
    }
}

/** Lightweight dependency container (no third-party DI framework). */
class AppContainer(app: Application) {
    val appDataStore: AppDataStore = AppDataStore(app.applicationContext)
    val footballRepository: FootballDataRepository = FootballDataRepository()
}
