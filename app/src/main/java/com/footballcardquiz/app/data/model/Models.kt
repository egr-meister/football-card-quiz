package com.footballcardquiz.app.data.model

import kotlinx.serialization.Serializable

/**
 * All persistent + domain data models for Football Card Quiz.
 * Everything is @Serializable so it can be stored as JSON in DataStore
 * and normalized safely from the football-data.org API.
 *
 * Every field has a safe default so decoding partial / legacy / corrupted
 * JSON never throws and never crashes the app.
 */

// ---------------------------------------------------------------------------
// Enums
// ---------------------------------------------------------------------------

@Serializable
enum class QuizCategory(val displayName: String) {
    FootballRules("Football Rules"),
    PlayerPositions("Player Positions"),
    RefereeCards("Referee Cards"),
    MatchSituations("Match Situations"),
    FieldAndEquipment("Field & Equipment"),
    GeneralFootball("General Football");

    companion object {
        fun fromNameSafe(value: String?): QuizCategory? =
            entries.firstOrNull { it.name.equals(value, ignoreCase = true) }
    }
}

@Serializable
enum class QuizDifficulty(val displayName: String) {
    Easy("Easy"),
    Medium("Medium"),
    Hard("Hard");

    companion object {
        fun fromNameSafe(value: String?): QuizDifficulty =
            entries.firstOrNull { it.name.equals(value, ignoreCase = true) } ?: Easy
    }
}

@Serializable
enum class MatchSource { Api, Cache, Demo }

// ---------------------------------------------------------------------------
// Quiz models
// ---------------------------------------------------------------------------

@Serializable
data class QuizQuestion(
    val id: String = "",
    val category: QuizCategory = QuizCategory.GeneralFootball,
    val difficulty: QuizDifficulty = QuizDifficulty.Easy,
    val question: String = "",
    val answers: List<String> = emptyList(),
    val correctAnswerIndex: Int = 0,
    val explanation: String = ""
) {
    /** True only when the option list & index are internally consistent. */
    val isValid: Boolean
        get() = question.isNotBlank() &&
                answers.size >= 2 &&
                correctAnswerIndex in answers.indices

    val correctAnswerText: String
        get() = answers.getOrNull(correctAnswerIndex).orEmpty()
}

@Serializable
data class QuizReviewItem(
    val questionId: String = "",
    val question: String = "",
    val userAnswer: String = "",
    val correctAnswer: String = "",
    val explanation: String = "",
    val wasCorrect: Boolean = false,
    val category: QuizCategory = QuizCategory.GeneralFootball
)

@Serializable
data class QuizResult(
    val id: String = "",
    val date: String = "",           // YYYY-MM-DD
    val time: String = "",           // HH:mm
    val category: QuizCategory = QuizCategory.GeneralFootball,
    val difficulty: QuizDifficulty = QuizDifficulty.Easy,
    val totalQuestions: Int = 0,
    val correctAnswers: Int = 0,
    val incorrectAnswers: Int = 0,
    val scorePercent: Int = 0,
    val reviewItems: List<QuizReviewItem> = emptyList(),
    val createdAt: String = ""       // sortable timestamp string
)

@Serializable
data class BestScores(
    val bestOverall: Int = 0,
    val bestEasy: Int = 0,
    val bestMedium: Int = 0,
    val bestHard: Int = 0,
    val bestFootballRules: Int = 0,
    val bestPlayerPositions: Int = 0,
    val bestRefereeCards: Int = 0,
    val bestMatchSituations: Int = 0,
    val bestFieldAndEquipment: Int = 0,
    val bestGeneralFootball: Int = 0
) {
    fun byDifficulty(d: QuizDifficulty): Int = when (d) {
        QuizDifficulty.Easy -> bestEasy
        QuizDifficulty.Medium -> bestMedium
        QuizDifficulty.Hard -> bestHard
    }

    fun byCategory(c: QuizCategory): Int = when (c) {
        QuizCategory.FootballRules -> bestFootballRules
        QuizCategory.PlayerPositions -> bestPlayerPositions
        QuizCategory.RefereeCards -> bestRefereeCards
        QuizCategory.MatchSituations -> bestMatchSituations
        QuizCategory.FieldAndEquipment -> bestFieldAndEquipment
        QuizCategory.GeneralFootball -> bestGeneralFootball
    }
}

// ---------------------------------------------------------------------------
// Match Schedule models (secondary feature)
// ---------------------------------------------------------------------------

@Serializable
data class NormalizedMatch(
    val id: String = "",
    val utcDate: String = "",
    val date: String = "",           // YYYY-MM-DD
    val time: String = "",           // HH:mm
    val competitionName: String = "",
    val competitionCode: String = "",
    val homeTeam: String = "Unknown",
    val awayTeam: String = "Unknown",
    val status: String = "",
    val homeScore: Int? = null,
    val awayScore: Int? = null,
    val winner: String = "",
    val source: MatchSource = MatchSource.Demo
)

@Serializable
data class MatchScheduleCache(
    val cachedMatches: List<NormalizedMatch> = emptyList(),
    val lastUpdatedAt: String = "",
    val lastError: String = "",
    val lastDateFrom: String = "",
    val lastDateTo: String = ""
)

@Serializable
data class MatchScheduleSettings(
    val apiEnabled: Boolean = true,
    val useDemoData: Boolean = false,
    val dateFrom: String = "",
    val dateTo: String = "",
    val competitionCode: String = ""
)

// ---------------------------------------------------------------------------
// Settings + top-level app data container
// ---------------------------------------------------------------------------

@Serializable
data class Settings(
    val onboardingCompleted: Boolean = false,
    val compactMode: Boolean = false,
    val defaultDifficulty: QuizDifficulty = QuizDifficulty.Easy,
    val defaultCategory: QuizCategory? = null,
    val matchSchedule: MatchScheduleSettings = MatchScheduleSettings()
)

@Serializable
data class AppData(
    val quizResults: List<QuizResult> = emptyList(),
    val bestScores: BestScores = BestScores(),
    val settings: Settings = Settings(),
    val matchScheduleCache: MatchScheduleCache = MatchScheduleCache()
)

/** Result wrapper returned from the API layer to the UI (never throws upward). */
data class FootballApiResult(
    val ok: Boolean,
    val matches: List<NormalizedMatch>,
    val error: String,
    val usedDemoData: Boolean
)
