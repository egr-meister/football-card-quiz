package com.footballcardquiz.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.footballcardquiz.app.data.model.QuizCategory
import com.footballcardquiz.app.data.model.QuizDifficulty
import com.footballcardquiz.app.ui.components.DifficultyChip
import com.footballcardquiz.app.ui.components.NotePanel
import com.footballcardquiz.app.ui.components.RefereeCardChip
import com.footballcardquiz.app.ui.components.YellowHeader
import com.footballcardquiz.app.ui.components.clickableCard
import com.footballcardquiz.app.ui.theme.CardDarkText
import com.footballcardquiz.app.ui.theme.CorrectGreen
import com.footballcardquiz.app.ui.theme.DeepCharcoal
import com.footballcardquiz.app.ui.theme.IncorrectRed
import com.footballcardquiz.app.ui.theme.SecondaryGray
import com.footballcardquiz.app.ui.theme.SoftYellowPanel
import com.footballcardquiz.app.ui.theme.StrongYellow
import com.footballcardquiz.app.ui.theme.WhiteCard
import com.footballcardquiz.app.viewmodel.QuizViewModel

@Composable
fun QuizScreen(
    quizViewModel: QuizViewModel,
    difficulty: QuizDifficulty,
    category: QuizCategory?,
    onExit: () -> Unit,
    onFinished: () -> Unit
) {
    // Start (or restart) the session for this difficulty/category once.
    LaunchedEffect(difficulty, category) {
        quizViewModel.startQuiz(difficulty, category)
    }

    val state by quizViewModel.state.collectAsState()

    // When the session finishes, hand off to the result screen.
    LaunchedEffect(state.finished) {
        if (state.finished) onFinished()
    }

    Column(Modifier.fillMaxSize()) {
        YellowHeader(
            title = "Question ${(state.currentIndex + 1).coerceAtMost(state.total.coerceAtLeast(1))} of ${state.total.coerceAtLeast(1)}",
            subtitle = category?.displayName ?: "Mixed Quiz",
            onBack = onExit
        )

        val question = state.currentQuestion
        if (state.total == 0 || question == null) {
            // Friendly fallback if there are somehow no questions.
            Column(Modifier.fillMaxSize().padding(20.dp)) {
                NotePanel("No questions are available for this selection right now. Please go back and try another category or difficulty.")
                Spacer(Modifier.height(16.dp))
                Button(onClick = onExit, modifier = Modifier.fillMaxWidth().height(50.dp), shape = RoundedCornerShape(14.dp)) {
                    Text("Back to Home")
                }
            }
            return@Column
        }

        Column(
            Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            LinearProgressIndicator(
                progress = { state.progress.coerceIn(0f, 1f) },
                modifier = Modifier.fillMaxWidth().height(8.dp).clip(RoundedCornerShape(4.dp)),
                color = DeepCharcoal,
                trackColor = SoftYellowPanel
            )
            Spacer(Modifier.height(14.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
                RefereeCardChip(question.category.displayName)
                DifficultyChip(state.difficulty)
            }
            Spacer(Modifier.height(14.dp))

            // Question card (dark title bar + white body)
            Column(
                Modifier.fillMaxWidth().clip(RoundedCornerShape(18.dp)).background(WhiteCard)
            ) {
                Box(
                    Modifier.fillMaxWidth().background(DeepCharcoal).padding(14.dp)
                ) {
                    Text("Football Question", color = StrongYellow, style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.Bold)
                }
                Text(
                    question.question,
                    modifier = Modifier.padding(16.dp),
                    style = MaterialTheme.typography.titleLarge,
                    color = CardDarkText,
                    fontWeight = FontWeight.Bold
                )
            }
            Spacer(Modifier.height(16.dp))

            // Answer option cards
            question.answers.forEachIndexed { index, answer ->
                AnswerCard(
                    text = answer,
                    index = index,
                    selectedIndex = state.selectedAnswerIndex,
                    answered = state.answered,
                    correctIndex = question.correctAnswerIndex,
                    onClick = { quizViewModel.selectAnswer(index) }
                )
                Spacer(Modifier.height(10.dp))
            }

            // Explanation after answering
            if (state.answered) {
                Spacer(Modifier.height(4.dp))
                val wasCorrect = state.selectedAnswerIndex == question.correctAnswerIndex
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        if (wasCorrect) Icons.Filled.CheckCircle else Icons.Filled.Cancel,
                        contentDescription = null,
                        tint = if (wasCorrect) CorrectGreen else IncorrectRed
                    )
                    Spacer(Modifier.size(8.dp))
                    Text(
                        if (wasCorrect) "Correct!" else "Not quite.",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = if (wasCorrect) CorrectGreen else IncorrectRed
                    )
                }
                Spacer(Modifier.height(8.dp))
                NotePanel(question.explanation.ifBlank { "Review the correct answer above." })
                Spacer(Modifier.height(16.dp))
            }

            // Action button
            if (!state.answered) {
                Button(
                    onClick = { quizViewModel.confirmAnswer() },
                    enabled = state.selectedAnswerIndex != null,
                    modifier = Modifier.fillMaxWidth().height(52.dp),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = DeepCharcoal, contentColor = StrongYellow)
                ) {
                    Text("Confirm answer", fontWeight = FontWeight.Bold)
                }
            } else {
                Button(
                    onClick = { quizViewModel.next() },
                    modifier = Modifier.fillMaxWidth().height(52.dp),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = StrongYellow, contentColor = DeepCharcoal)
                ) {
                    Text(if (state.isLastQuestion) "Finish quiz" else "Next question", fontWeight = FontWeight.Bold)
                }
            }
            Spacer(Modifier.height(24.dp))
        }
    }
}

@Composable
private fun AnswerCard(
    text: String,
    index: Int,
    selectedIndex: Int?,
    answered: Boolean,
    correctIndex: Int,
    onClick: () -> Unit
) {
    val isSelected = selectedIndex == index
    val isCorrect = index == correctIndex

    val bg: Color
    val borderColor: Color
    when {
        answered && isCorrect -> { bg = CorrectGreen.copy(alpha = 0.15f); borderColor = CorrectGreen }
        answered && isSelected && !isCorrect -> { bg = IncorrectRed.copy(alpha = 0.15f); borderColor = IncorrectRed }
        !answered && isSelected -> { bg = SoftYellowPanel; borderColor = DeepCharcoal }
        else -> { bg = WhiteCard; borderColor = SecondaryGray.copy(alpha = 0.3f) }
    }

    Row(
        Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(bg)
            .border(2.dp, borderColor, RoundedCornerShape(14.dp))
            .clickableCard { if (!answered) onClick() }
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            Modifier.size(28.dp).clip(RoundedCornerShape(50)).background(DeepCharcoal),
            contentAlignment = Alignment.Center
        ) {
            Text(('A' + index).toString(), color = StrongYellow, style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.Bold)
        }
        Spacer(Modifier.size(12.dp))
        Text(text, style = MaterialTheme.typography.bodyLarge, color = CardDarkText, modifier = Modifier.weight(1f))
        if (answered && isCorrect) {
            Icon(Icons.Filled.CheckCircle, contentDescription = "Correct", tint = CorrectGreen)
        } else if (answered && isSelected && !isCorrect) {
            Icon(Icons.Filled.Cancel, contentDescription = "Incorrect", tint = IncorrectRed)
        }
    }
}
