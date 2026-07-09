package com.footballcardquiz.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.footballcardquiz.app.data.model.QuizReviewItem
import com.footballcardquiz.app.ui.components.NotePanel
import com.footballcardquiz.app.ui.components.RefereeCardChip
import com.footballcardquiz.app.ui.components.YellowHeader
import com.footballcardquiz.app.ui.theme.CardDarkText
import com.footballcardquiz.app.ui.theme.CorrectGreen
import com.footballcardquiz.app.ui.theme.IncorrectRed
import com.footballcardquiz.app.ui.theme.SecondaryGray
import com.footballcardquiz.app.ui.theme.SoftYellowPanel
import com.footballcardquiz.app.ui.theme.StrongYellow
import com.footballcardquiz.app.ui.theme.WhiteCard

@Composable
fun ReviewAnswersScreen(reviewItems: List<QuizReviewItem>, onBack: () -> Unit) {
    var onlyWrong by remember { mutableStateOf(false) }
    val visibleItems = if (onlyWrong) reviewItems.filterNot { it.wasCorrect } else reviewItems

    Column(Modifier.fillMaxSize()) {
        YellowHeader(title = "Review answers", subtitle = "Learn from each question", onBack = onBack)
        if (reviewItems.isEmpty()) {
            Column(Modifier.fillMaxSize().padding(20.dp)) {
                NotePanel("There are no answers to review for this quiz.")
            }
            return@Column
        }
        LazyColumn(Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            item {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    FilterChip(
                        selected = onlyWrong,
                        onClick = { onlyWrong = !onlyWrong },
                        label = { Text("Show only wrong answers") },
                        colors = FilterChipDefaults.filterChipColors(selectedContainerColor = StrongYellow)
                    )
                }
            }
            items(visibleItems.size) { i ->
                ReviewCard(visibleItems[i])
            }
            if (visibleItems.isEmpty()) {
                item { NotePanel("No wrong answers — every question was correct.") }
            }
        }
    }
}

@Composable
private fun ReviewCard(item: QuizReviewItem) {
    Column(Modifier.fillMaxWidth().clip(RoundedCornerShape(16.dp)).background(WhiteCard).padding(16.dp)) {
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
            RefereeCardChip(item.category.displayName)
            RefereeCardChip(if (item.wasCorrect) "Correct" else "Wrong", red = !item.wasCorrect)
        }
        Spacer(Modifier.size(10.dp))
        Text(item.question, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = CardDarkText)
        Spacer(Modifier.size(10.dp))
        LabeledLine("Your answer", item.userAnswer.ifBlank { "—" }, if (item.wasCorrect) CorrectGreen else IncorrectRed)
        LabeledLine("Correct answer", item.correctAnswer.ifBlank { "—" }, CorrectGreen)
        if (item.explanation.isNotBlank()) {
            Spacer(Modifier.size(10.dp))
            Column(Modifier.fillMaxWidth().clip(RoundedCornerShape(10.dp)).background(SoftYellowPanel).padding(12.dp)) {
                Text("Why", style = MaterialTheme.typography.labelMedium, color = SecondaryGray, fontWeight = FontWeight.Bold)
                Text(item.explanation, style = MaterialTheme.typography.bodyMedium, color = CardDarkText)
            }
        }
    }
}

@Composable
private fun LabeledLine(label: String, value: String, color: androidx.compose.ui.graphics.Color) {
    Row(Modifier.fillMaxWidth().padding(vertical = 3.dp)) {
        Text("$label: ", style = MaterialTheme.typography.bodyMedium, color = SecondaryGray)
        Text(value, style = MaterialTheme.typography.bodyMedium, color = color, fontWeight = FontWeight.Bold)
    }
}
