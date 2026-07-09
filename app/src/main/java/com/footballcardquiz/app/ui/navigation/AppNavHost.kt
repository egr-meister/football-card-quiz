package com.footballcardquiz.app.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.footballcardquiz.app.data.model.QuizCategory
import com.footballcardquiz.app.data.model.QuizDifficulty
import com.footballcardquiz.app.ui.screens.BestScoresScreen
import com.footballcardquiz.app.ui.screens.CategorySelectScreen
import com.footballcardquiz.app.ui.screens.DifficultySelectScreen
import com.footballcardquiz.app.ui.screens.HomeScreen
import com.footballcardquiz.app.ui.screens.MatchScheduleScreen
import com.footballcardquiz.app.ui.screens.MatchScheduleSettingsScreen
import com.footballcardquiz.app.ui.screens.OnboardingScreen
import com.footballcardquiz.app.ui.screens.QuizScreen
import com.footballcardquiz.app.ui.screens.ResultScreen
import com.footballcardquiz.app.ui.screens.ReviewAnswersScreen
import com.footballcardquiz.app.ui.screens.ScoreHistoryScreen
import com.footballcardquiz.app.ui.screens.SettingsScreen
import com.footballcardquiz.app.viewmodel.MainViewModel
import com.footballcardquiz.app.viewmodel.MatchScheduleViewModel
import com.footballcardquiz.app.viewmodel.QuizViewModel

/** Safely map a route string arg back to an enum with defaults. */
private fun parseDifficulty(value: String?): QuizDifficulty =
    QuizDifficulty.fromNameSafe(value)

private fun parseCategory(value: String?): QuizCategory? =
    if (value == null || value == Routes.CATEGORY_ALL) null
    else QuizCategory.fromNameSafe(value)

@Composable
fun AppNavHost(
    navController: NavHostController,
    mainViewModel: MainViewModel,
    quizViewModel: QuizViewModel,
    matchViewModel: MatchScheduleViewModel
) {
    val appData by mainViewModel.appData.collectAsState()
    val startDestination = if (appData.settings.onboardingCompleted) Routes.HOME else Routes.ONBOARDING

    NavHost(navController = navController, startDestination = startDestination) {

        composable(Routes.ONBOARDING) {
            OnboardingScreen(
                onStart = {
                    mainViewModel.completeOnboarding()
                    navController.navigate(Routes.HOME) {
                        popUpTo(Routes.ONBOARDING) { inclusive = true }
                    }
                },
                onSkip = {
                    mainViewModel.completeOnboarding()
                    navController.navigate(Routes.HOME) {
                        popUpTo(Routes.ONBOARDING) { inclusive = true }
                    }
                }
            )
        }

        composable(Routes.HOME) {
            HomeScreen(
                appData = appData,
                onQuickQuiz = { difficulty ->
                    navController.navigate(Routes.quiz(difficulty.name, Routes.CATEGORY_ALL))
                },
                onChooseDifficulty = { navController.navigate(Routes.DIFFICULTY) },
                onCategory = { category ->
                    val diff = appData.settings.defaultDifficulty.name
                    navController.navigate(Routes.quiz(diff, category.name))
                },
                onHistory = { navController.navigate(Routes.HISTORY) },
                onBest = { navController.navigate(Routes.BEST) },
                onMatches = { navController.navigate(Routes.MATCHES) },
                onSettings = { navController.navigate(Routes.SETTINGS) }
            )
        }

        composable(Routes.DIFFICULTY) {
            DifficultySelectScreen(
                onBack = { navController.popBackStack() },
                onSelect = { difficulty ->
                    navController.navigate(Routes.category(difficulty.name))
                }
            )
        }

        composable(
            Routes.CATEGORY,
            arguments = listOf(navArgument("difficulty") {
                type = NavType.StringType; defaultValue = QuizDifficulty.Easy.name
            })
        ) { entry ->
            val difficulty = parseDifficulty(entry.arguments?.getString("difficulty"))
            CategorySelectScreen(
                difficulty = difficulty,
                onBack = { navController.popBackStack() },
                onSelect = { category ->
                    val cat = category?.name ?: Routes.CATEGORY_ALL
                    navController.navigate(Routes.quiz(difficulty.name, cat))
                }
            )
        }

        composable(
            Routes.QUIZ,
            arguments = listOf(
                navArgument("difficulty") { type = NavType.StringType; defaultValue = QuizDifficulty.Easy.name },
                navArgument("category") { type = NavType.StringType; defaultValue = Routes.CATEGORY_ALL }
            )
        ) { entry ->
            val difficulty = parseDifficulty(entry.arguments?.getString("difficulty"))
            val category = parseCategory(entry.arguments?.getString("category"))
            QuizScreen(
                quizViewModel = quizViewModel,
                difficulty = difficulty,
                category = category,
                onExit = { navController.popBackStack(Routes.HOME, inclusive = false) },
                onFinished = {
                    navController.navigate(Routes.RESULT) {
                        popUpTo(Routes.QUIZ) { inclusive = true }
                    }
                }
            )
        }

        composable(Routes.RESULT) {
            ResultScreen(
                quizViewModel = quizViewModel,
                onRetry = {
                    quizViewModel.retry()
                    navController.navigate(
                        Routes.quiz(
                            quizViewModel.state.value.difficulty.name,
                            quizViewModel.state.value.category?.name ?: Routes.CATEGORY_ALL
                        )
                    ) { popUpTo(Routes.RESULT) { inclusive = true } }
                },
                onReview = { navController.navigate(Routes.REVIEW) },
                onHistory = {
                    navController.navigate(Routes.HISTORY) {
                        popUpTo(Routes.HOME) { inclusive = false }
                    }
                },
                onHome = { navController.popBackStack(Routes.HOME, inclusive = false) }
            )
        }

        composable(Routes.REVIEW) {
            val quizState by quizViewModel.state.collectAsState()
            ReviewAnswersScreen(
                reviewItems = quizState.reviewItems,
                onBack = { navController.popBackStack() }
            )
        }

        composable(Routes.HISTORY) {
            ScoreHistoryScreen(
                results = appData.quizResults,
                onBack = { navController.popBackStack() },
                onOpenReview = { id -> navController.navigate(Routes.reviewResult(id)) },
                onDelete = { id -> mainViewModel.deleteResult(id) },
                onClearAll = { mainViewModel.clearHistory() }
            )
        }

        composable(
            Routes.REVIEW_RESULT,
            arguments = listOf(navArgument("resultId") { type = NavType.StringType; defaultValue = "" })
        ) { entry ->
            val id = entry.arguments?.getString("resultId")
            val result = mainViewModel.findResult(id)
            ReviewAnswersScreen(
                reviewItems = result?.reviewItems ?: emptyList(),
                onBack = { navController.popBackStack() }
            )
        }

        composable(Routes.BEST) {
            BestScoresScreen(
                appData = appData,
                onBack = { navController.popBackStack() }
            )
        }

        composable(Routes.MATCHES) {
            MatchScheduleScreen(
                viewModel = matchViewModel,
                onBack = { navController.popBackStack() },
                onOpenSettings = { navController.navigate(Routes.MATCH_SETTINGS) }
            )
        }

        composable(Routes.MATCH_SETTINGS) {
            MatchScheduleSettingsScreen(
                settings = appData.settings.matchSchedule,
                hasToken = matchViewModel.hasValidToken,
                onBack = { navController.popBackStack() },
                onUpdate = { transform -> mainViewModel.updateMatchSettings(transform) },
                onClearCache = { mainViewModel.clearMatchCache() },
                onApplied = { matchViewModel.forceReloadFromSettings() }
            )
        }

        composable(Routes.SETTINGS) {
            SettingsScreen(
                appData = appData,
                onBack = { navController.popBackStack() },
                onDefaultDifficulty = { mainViewModel.setDefaultDifficulty(it) },
                onDefaultCategory = { mainViewModel.setDefaultCategory(it) },
                onCompactMode = { mainViewModel.setCompactMode(it) },
                onMatchSettings = { navController.navigate(Routes.MATCH_SETTINGS) },
                onShowOnboarding = {
                    mainViewModel.showOnboardingAgain()
                    navController.navigate(Routes.ONBOARDING) {
                        popUpTo(Routes.HOME) { inclusive = true }
                    }
                },
                onClearMatchCache = { mainViewModel.clearMatchCache() },
                onDeleteHistory = { mainViewModel.clearHistory() },
                onResetBest = { mainViewModel.resetBestScores() },
                onResetAll = { mainViewModel.resetAll() }
            )
        }
    }
}
