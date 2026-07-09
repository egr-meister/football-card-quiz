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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import com.footballcardquiz.app.ui.components.CategoryChip
import com.footballcardquiz.app.ui.components.DifficultyChip
import com.footballcardquiz.app.ui.components.ScoreBadge
import com.footballcardquiz.app.ui.components.SectionCard
import com.footballcardquiz.app.ui.components.clickableCard
import com.footballcardquiz.app.ui.components.difficultyColor
import com.footballcardquiz.app.ui.theme.CardDarkText
import com.footballcardquiz.app.ui.theme.DeepCharcoal
import com.footballcardquiz.app.ui.theme.PureBlack
import com.footballcardquiz.app.ui.theme.SecondaryGray
import com.footballcardquiz.app.ui.theme.SoftYellowPanel
import com.footballcardquiz.app.ui.theme.StrongYellow
import com.footballcardquiz.app.ui.theme.WhiteCard

@Composable
fun HomeScreen(
    appData: AppData,
    onQuickQuiz: (QuizDifficulty) -> Unit,
    onChooseDifficulty: () -> Unit,
    onCategory: (QuizCategory) -> Unit,
    onHistory: () -> Unit,
    onBest: () -> Unit,
    onMatches: () -> Unit,
    onSettings: () -> Unit
) {
    val latest = appData.quizResults.firstOrNull()

    LazyColumn(
        Modifier.fillMaxSize(),
        contentPadding = androidx.compose.foundation.layout.PaddingValues(bottom = 28.dp)
    ) {
        // ---- Yellow hero header with title + best score badge ----
        item {
            Box(
                Modifier
                    .fillMaxWidth()
                    .background(StrongYellow)
                    .padding(20.dp)
            ) {
                Column {
                    Row(
                        Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.Top
                    ) {
                        Column(Modifier.weight(1f)) {
                            Text(
                                "Football Card Quiz",
                                style = MaterialTheme.typography.headlineMedium,
                                fontWeight = FontWeight.ExtraBold,
                                color = PureBlack
                            )
                            Text(
                                "Football rules quiz",
                                style = MaterialTheme.typography.titleMedium,
                                color = CardDarkText
                            )
                        }
                        IconButton(onClick = onSettings) {
                            Icon(Icons.Filled.Settings, contentDescription = "Settings", tint = PureBlack)
                        }
                    }
                    Spacer(Modifier.height(16.dp))
                    Row(
                        Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(16.dp))
                            .background(DeepCharcoal)
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text("Best overall score", color = StrongYellow, style = MaterialTheme.typography.labelLarge)
                            Text(
                                "${appData.bestScores.bestOverall}%",
                                color = WhiteCard,
                                style = MaterialTheme.typography.headlineLarge,
                                fontWeight = FontWeight.ExtraBold
                            )
                        }
                        ScoreBadge(percent = appData.bestScores.bestOverall, size = 64)
                    }
                }
            }
        }

        // ---- Quick play difficulty cards (horizontal, referee-card feel) ----
        item {
            Column(Modifier.padding(16.dp)) {
                Text("Quick play", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = CardDarkText)
                Spacer(Modifier.height(10.dp))
                Row(
                    Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    QuizDifficulty.entries.forEach { d ->
                        QuickPlayCard(d) { onQuickQuiz(d) }
                    }
                }
            }
        }

        // ---- Category chips ----
        item {
            Column(Modifier.padding(horizontal = 16.dp)) {
                Row(
                    Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Categories", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = CardDarkText)
                    Text(
                        "Choose difficulty",
                        style = MaterialTheme.typography.labelLarge,
                        color = SecondaryGray,
                        modifier = Modifier.clip(RoundedCornerShape(8.dp)).padding(4.dp)
                    )
                }
                Spacer(Modifier.height(10.dp))
                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(QuizCategory.entries) { c ->
                        CategoryChip(category = c, onClick = { onCategory(c) })
                    }
                }
                Spacer(Modifier.height(6.dp))
                Text(
                    "Tap a difficulty card above, or open Choose difficulty for the full flow.",
                    style = MaterialTheme.typography.labelMedium,
                    color = SecondaryGray,
                    modifier = Modifier.clip(RoundedCornerShape(6.dp))
                )
            }
        }

        // ---- Latest result / empty state ----
        item {
            Column(Modifier.padding(16.dp)) {
                Text("Latest result", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = CardDarkText)
                Spacer(Modifier.height(10.dp))
                if (latest == null) {
                    SectionCard {
                        Text("No quiz results yet.", style = MaterialTheme.typography.titleMedium, color = CardDarkText, fontWeight = FontWeight.Bold)
                        Spacer(Modifier.height(4.dp))
                        Text("Start your first football card quiz.", style = MaterialTheme.typography.bodyMedium, color = SecondaryGray)
                    }
                } else {
                    SectionCard(onClick = onHistory) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            ScoreBadge(latest.scorePercent, size = 56)
                            Spacer(Modifier.size(14.dp))
                            Column(Modifier.weight(1f)) {
                                Text(latest.category.displayName, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = CardDarkText)
                                Text("${latest.correctAnswers}/${latest.totalQuestions} correct • ${latest.date} ${latest.time}", style = MaterialTheme.typography.bodyMedium, color = SecondaryGray)
                                Spacer(Modifier.height(6.dp))
                                DifficultyChip(latest.difficulty)
                            }
                        }
                    }
                }
            }
        }

        // ---- Shortcuts row: History + Best ----
        item {
            Row(
                Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                ShortcutCard("Score history", "Browse past quizzes", Modifier.weight(1f), onHistory)
                ShortcutCard("Best scores", "Your top results", Modifier.weight(1f), onBest)
            }
        }

        // ---- Secondary Match Schedule card (small, non-dominant) ----
        item {
            Column(Modifier.padding(16.dp)) {
                SectionCard(onClick = onMatches) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            Modifier.size(44.dp).clip(RoundedCornerShape(12.dp)).background(SoftYellowPanel),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Filled.CalendarMonth, contentDescription = null, tint = DeepCharcoal)
                        }
                        Spacer(Modifier.size(12.dp))
                        Column(Modifier.weight(1f)) {
                            Text("Match Schedule", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = CardDarkText)
                            Text("Optional reference — upcoming matches", style = MaterialTheme.typography.bodyMedium, color = SecondaryGray)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun QuickPlayCard(d: QuizDifficulty, onClick: () -> Unit) {
    Column(
        Modifier
            .width(140.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(WhiteCard)
            .clickableCard(onClick)
            .padding(16.dp)
    ) {
        Box(
            Modifier.size(30.dp).clip(RoundedCornerShape(6.dp)).background(difficultyColor(d))
        )
        Spacer(Modifier.height(12.dp))
        Text(d.displayName, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.ExtraBold, color = CardDarkText)
        Text("Start quiz", style = MaterialTheme.typography.labelMedium, color = SecondaryGray)
    }
}

@Composable
private fun ShortcutCard(title: String, subtitle: String, modifier: Modifier, onClick: () -> Unit) {
    Column(
        modifier
            .clip(RoundedCornerShape(16.dp))
            .background(DeepCharcoal)
            .clickableCard(onClick)
            .padding(16.dp)
    ) {
        Text(title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = StrongYellow)
        Spacer(Modifier.height(2.dp))
        Text(subtitle, style = MaterialTheme.typography.labelMedium, color = WhiteCard)
    }
}
