package com.footballcardquiz.app.ui.navigation

/** Central route constants. Category "ALL" means a mixed quiz (no category filter). */
object Routes {
    const val ONBOARDING = "onboarding"
    const val HOME = "home"
    const val DIFFICULTY = "difficulty"
    const val CATEGORY = "category/{difficulty}"
    const val QUIZ = "quiz/{difficulty}/{category}"
    const val RESULT = "result"
    const val REVIEW = "review"
    const val HISTORY = "history"
    const val REVIEW_RESULT = "review_result/{resultId}"
    const val BEST = "best"
    const val MATCHES = "matches"
    const val MATCH_SETTINGS = "match_settings"
    const val SETTINGS = "settings"

    const val CATEGORY_ALL = "ALL"

    fun category(difficulty: String) = "category/$difficulty"
    fun quiz(difficulty: String, category: String) = "quiz/$difficulty/$category"
    fun reviewResult(resultId: String) = "review_result/$resultId"
}
