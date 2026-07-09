package com.footballcardquiz.app.ui.screens

import androidx.compose.foundation.background
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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.footballcardquiz.app.ui.components.DifficultyChip
import com.footballcardquiz.app.ui.components.RefereeCardChip
import com.footballcardquiz.app.ui.components.ScoreBadge
import com.footballcardquiz.app.ui.components.YellowHeader
import com.footballcardquiz.app.ui.theme.CardDarkText
import com.footballcardquiz.app.ui.theme.CorrectGreen
import com.footballcardquiz.app.ui.theme.DeepCharcoal
import com.footballcardquiz.app.ui.theme.IncorrectRed
import com.footballcardquiz.app.ui.theme.SecondaryGray
import com.footballcardquiz.app.ui.theme.StrongYellow
import com.footballcardquiz.app.ui.theme.WhiteCard
import com.footballcardquiz.app.viewmodel.QuizViewModel

fun resultMessage(percent: Int): String = when {
    percent >= 90 -> "Excellent quiz result."
    percent >= 70 -> "Strong football knowledge."
    percent >= 40 -> "Good effort. Review the explanations."
    else -> "Keep practicing the rules."
}

@Composable
fun ResultScreen(
    quizViewModel: QuizViewModel,
    onRetry: () -> Unit,
    onReview: () -> Unit,
    onHistory: () -> Unit,
    onHome: () -> Unit
) {
    val state by quizViewModel.state.collectAsState()
    val total = state.total
    val correct = state.correctCount
    val incorrect = (total - correct).coerceAtLeast(0)
    val percent = if (total == 0) 0 else (correct * 100) / total

    Column(Modifier.fillMaxSize()) {
        YellowHeader(title = "Your result", onBack = onHome)
        Column(
            Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Column(
                Modifier.fillMaxWidth().clip(RoundedCornerShape(20.dp)).background(DeepCharcoal).padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                ScoreBadge(percent = percent, size = 96)
                Spacer(Modifier.height(14.dp))
                Text(resultMessage(percent), color = StrongYellow, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.ExtraBold, textAlign = TextAlign.Center)
                Spacer(Modifier.height(6.dp))
                Text("Saved to your local history", color = WhiteCard, style = MaterialTheme.typography.labelMedium)
            }
            Spacer(Modifier.height(16.dp))

            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                StatBox("Correct", correct.toString(), CorrectGreen, Modifier.weight(1f))
                StatBox("Incorrect", incorrect.toString(), IncorrectRed, Modifier.weight(1f))
            }
            Spacer(Modifier.height(12.dp))

            Column(Modifier.fillMaxWidth().clip(RoundedCornerShape(16.dp)).background(WhiteCard).padding(16.dp)) {
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                    Text("Category", color = SecondaryGray, style = MaterialTheme.typography.bodyMedium)
                    RefereeCardChip((state.category?.displayName) ?: "Mixed Quiz")
                }
                Spacer(Modifier.height(10.dp))
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                    Text("Difficulty", color = SecondaryGray, style = MaterialTheme.typography.bodyMedium)
                    DifficultyChip(state.difficulty)
                }
            }
            Spacer(Modifier.height(20.dp))

            Button(
                onClick = onRetry,
                modifier = Modifier.fillMaxWidth().height(52.dp),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(containerColor = DeepCharcoal, contentColor = StrongYellow)
            ) { Text("Retry quiz", fontWeight = FontWeight.Bold) }
            Spacer(Modifier.height(10.dp))
            OutlinedButton(onClick = onReview, modifier = Modifier.fillMaxWidth().height(50.dp), shape = RoundedCornerShape(14.dp)) {
                Text("Review answers", color = CardDarkText, fontWeight = FontWeight.Bold)
            }
            Spacer(Modifier.height(4.dp))
            TextButton(onClick = onHistory) { Text("Go to score history", color = CardDarkText) }
        }
    }
}

@Composable
private fun StatBox(label: String, value: String, color: androidx.compose.ui.graphics.Color, modifier: Modifier) {
    Column(
        modifier.clip(RoundedCornerShape(16.dp)).background(WhiteCard).padding(18.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(value, style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.ExtraBold, color = color)
        Text(label, style = MaterialTheme.typography.labelMedium, color = SecondaryGray)
    }
}
