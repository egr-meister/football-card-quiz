package com.footballcardquiz.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.footballcardquiz.app.ui.navigation.AppNavHost
import com.footballcardquiz.app.ui.theme.FootballCardQuizTheme
import com.footballcardquiz.app.ui.theme.LightAppBackground
import com.footballcardquiz.app.viewmodel.AppViewModelFactory
import com.footballcardquiz.app.viewmodel.MainViewModel
import com.footballcardquiz.app.viewmodel.MatchScheduleViewModel
import com.footballcardquiz.app.viewmodel.QuizViewModel

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        // Custom splash (yellow bg + black quiz-card logo) via SplashScreen API.
        installSplashScreen()
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        val container = (application as FootballCardQuizApp).container

        setContent {
            FootballCardQuizTheme {
                val factory = remember { AppViewModelFactory(container) }
                val mainViewModel: MainViewModel = viewModel(factory = factory)
                val quizViewModel: QuizViewModel = viewModel(factory = factory)
                val matchViewModel: MatchScheduleViewModel = viewModel(factory = factory)
                val navController = rememberNavController()

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = LightAppBackground
                ) {
                    Scaffold(
                        containerColor = LightAppBackground
                    ) { innerPadding ->
                        androidx.compose.foundation.layout.Box(
                            modifier = Modifier.padding(innerPadding)
                        ) {
                            AppNavHost(
                                navController = navController,
                                mainViewModel = mainViewModel,
                                quizViewModel = quizViewModel,
                                matchViewModel = matchViewModel
                            )
                        }
                    }
                }
            }
        }
    }
}
