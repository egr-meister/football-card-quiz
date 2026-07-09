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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.footballcardquiz.app.data.model.AppData
import com.footballcardquiz.app.data.model.QuizCategory
import com.footballcardquiz.app.data.model.QuizDifficulty
import com.footballcardquiz.app.ui.components.NotePanel
import com.footballcardquiz.app.ui.components.ScoreBadge
import com.footballcardquiz.app.ui.components.YellowHeader
import com.footballcardquiz.app.ui.theme.CardDarkText
import com.footballcardquiz.app.ui.theme.DeepCharcoal
import com.footballcardquiz.app.ui.theme.SecondaryGray
import com.footballcardquiz.app.ui.theme.StrongYellow
import com.footballcardquiz.app.ui.theme.WhiteCard

@Composable
fun BestScoresScreen(appData: AppData, onBack: () -> Unit) {
    val best = appData.bestScores
    val latest = appData.quizResults.firstOrNull()

    Column(Modifier.fillMaxSize()) {
        YellowHeader(title = "Best scores", subtitle = "Your top local results", onBack = onBack)
        Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(16.dp)) {

            Row(
                Modifier.fillMaxWidth().clip(RoundedCornerShape(20.dp)).background(DeepCharcoal).padding(20.dp),
                verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text("Best overall", color = StrongYellow, style = MaterialTheme.typography.labelLarge)
                    Text("${best.bestOverall}%", color = WhiteCard, style = MaterialTheme.typography.headlineLarge, fontWeight = FontWeight.ExtraBold)
                }
                ScoreBadge(best.bestOverall, size = 72)
            }
            Spacer(Modifier.height(20.dp))

            SectionTitle("By difficulty")
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                QuizDifficulty.entries.forEach { d ->
                    MiniStat(d.displayName, best.byDifficulty(d), Modifier.weight(1f))
                }
            }
            Spacer(Modifier.height(20.dp))

            SectionTitle("By category")
            QuizCategory.entries.forEach { c ->
                Row(
                    Modifier.fillMaxWidth().padding(vertical = 5.dp).clip(RoundedCornerShape(12.dp)).background(WhiteCard).padding(14.dp),
                    horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(c.displayName, style = MaterialTheme.typography.bodyLarge, color = CardDarkText)
                    Text("${best.byCategory(c)}%", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = CardDarkText)
                }
            }
            Spacer(Modifier.height(20.dp))

            SectionTitle("Latest result")
            if (latest == null) {
                NotePanel("No quiz results yet. Start your first football card quiz.")
            } else {
                Row(
                    Modifier.fillMaxWidth().clip(RoundedCornerShape(16.dp)).background(WhiteCard).padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    ScoreBadge(latest.scorePercent, size = 52)
                    Spacer(Modifier.width(12.dp))
                    Column {
                        Text("${latest.category.displayName} • ${latest.difficulty.displayName}", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = CardDarkText)
                        Text("${latest.correctAnswers}/${latest.totalQuestions} correct • ${latest.date} ${latest.time}", style = MaterialTheme.typography.bodyMedium, color = SecondaryGray)
                    }
                }
            }
            Spacer(Modifier.height(24.dp))
        }
    }
}

@Composable
private fun SectionTitle(text: String) {
    Text(text, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = CardDarkText, modifier = Modifier.padding(bottom = 10.dp))
}

@Composable
private fun MiniStat(label: String, value: Int, modifier: Modifier) {
    Column(
        modifier.clip(RoundedCornerShape(14.dp)).background(WhiteCard).padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("$value%", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.ExtraBold, color = CardDarkText)
        Text(label, style = MaterialTheme.typography.labelMedium, color = SecondaryGray)
    }
}
