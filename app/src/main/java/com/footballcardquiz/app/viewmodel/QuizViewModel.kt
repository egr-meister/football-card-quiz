package com.footballcardquiz.app.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.footballcardquiz.app.data.local.AppDataStore
import com.footballcardquiz.app.data.local.QuestionBank
import com.footballcardquiz.app.data.model.QuizCategory
import com.footballcardquiz.app.data.model.QuizDifficulty
import com.footballcardquiz.app.data.model.QuizQuestion
import com.footballcardquiz.app.data.model.QuizResult
import com.footballcardquiz.app.data.model.QuizReviewItem
import com.footballcardquiz.app.util.AppDateTime
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.UUID

/** Snapshot of an in-progress or completed quiz session. */
data class QuizUiState(
    val questions: List<QuizQuestion> = emptyList(),
    val currentIndex: Int = 0,
    val selectedAnswerIndex: Int? = null,
    val answered: Boolean = false,
    val correctCount: Int = 0,
    val reviewItems: List<QuizReviewItem> = emptyList(),
    val difficulty: QuizDifficulty = QuizDifficulty.Easy,
    val category: QuizCategory? = null,
    val finished: Boolean = false,
    val fallbackUsed: Boolean = false,
    val lastResultId: String? = null
) {
    val total: Int get() = questions.size
    val currentQuestion: QuizQuestion? get() = questions.getOrNull(currentIndex)
    val isLastQuestion: Boolean get() = currentIndex >= questions.size - 1
    val progress: Float
        get() = if (total == 0) 0f else (currentIndex + if (answered) 1 else 0).toFloat() / total
}

/**
 * Drives a single quiz session: question sequencing, answer confirmation,
 * scoring, review building, and saving the result. Never crashes on empty
 * or mismatched question pools.
 */
class QuizViewModel(private val store: AppDataStore) : ViewModel() {

    private val _state = MutableStateFlow(QuizUiState())
    val state: StateFlow<QuizUiState> = _state.asStateFlow()

    private val defaultCount = 10

    fun startQuiz(
        difficulty: QuizDifficulty,
        category: QuizCategory?,
        count: Int = defaultCount
    ) {
        val requested = QuestionBank.countFor(difficulty, category)
        val questions = QuestionBank.buildQuiz(difficulty, category, count)
        _state.value = QuizUiState(
            questions = questions,
            difficulty = difficulty,
            category = category,
            fallbackUsed = questions.isNotEmpty() && requested < count
        )
    }

    /** Confirm the currently selected answer. No-op if nothing selected or already answered. */
    fun confirmAnswer() {
        val s = _state.value
        val question = s.currentQuestion ?: return
        val selected = s.selectedAnswerIndex ?: return
        if (s.answered) return

        val wasCorrect = selected == question.correctAnswerIndex
        val review = QuizReviewItem(
            questionId = question.id,
            question = question.question,
            userAnswer = question.answers.getOrNull(selected).orEmpty(),
            correctAnswer = question.correctAnswerText,
            explanation = question.explanation,
            wasCorrect = wasCorrect,
            category = question.category
        )
        _state.value = s.copy(
            answered = true,
            correctCount = s.correctCount + if (wasCorrect) 1 else 0,
            reviewItems = s.reviewItems + review
        )
    }

    fun selectAnswer(index: Int) {
        val s = _state.value
        if (s.answered) return // cannot change after confirmation
        _state.value = s.copy(selectedAnswerIndex = index)
    }

    /** Advance to next question, or finish + persist the result on the last one. */
    fun next() {
        val s = _state.value
        if (!s.answered) return
        if (s.isLastQuestion) {
            finish(s)
        } else {
            _state.value = s.copy(
                currentIndex = s.currentIndex + 1,
                selectedAnswerIndex = null,
                answered = false
            )
        }
    }

    private fun finish(s: QuizUiState) {
        val total = s.total
        val correct = s.correctCount
        val incorrect = (total - correct).coerceAtLeast(0)
        val percent = if (total == 0) 0 else (correct * 100) / total
        val id = UUID.randomUUID().toString()

        val result = QuizResult(
            id = id,
            date = AppDateTime.today(),
            time = AppDateTime.currentTime(),
            category = s.category ?: QuizCategory.GeneralFootball,
            difficulty = s.difficulty,
            totalQuestions = total,
            correctAnswers = correct,
            incorrectAnswers = incorrect,
            scorePercent = percent,
            reviewItems = s.reviewItems,
            createdAt = AppDateTime.nowTimestamp()
        )

        // Only persist real sessions (avoid saving an empty fallback with 0 questions).
        if (total > 0) {
            viewModelScope.launch { store.addQuizResult(result) }
        }
        _state.value = s.copy(finished = true, lastResultId = id)
    }

    fun retry() {
        val s = _state.value
        startQuiz(s.difficulty, s.category, defaultCount)
    }

    fun reset() {
        _state.value = QuizUiState()
    }
}
