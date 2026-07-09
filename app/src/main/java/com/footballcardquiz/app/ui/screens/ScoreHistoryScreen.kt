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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.DeleteSweep
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import com.footballcardquiz.app.data.model.QuizResult
import com.footballcardquiz.app.ui.components.DifficultyChip
import com.footballcardquiz.app.ui.components.NotePanel
import com.footballcardquiz.app.ui.components.ScoreBadge
import com.footballcardquiz.app.ui.components.YellowHeader
import com.footballcardquiz.app.ui.components.clickableCard
import com.footballcardquiz.app.ui.theme.CardDarkText
import com.footballcardquiz.app.ui.theme.IncorrectRed
import com.footballcardquiz.app.ui.theme.PureBlack
import com.footballcardquiz.app.ui.theme.SecondaryGray
import com.footballcardquiz.app.ui.theme.WhiteCard

@Composable
fun ScoreHistoryScreen(
    results: List<QuizResult>,
    onBack: () -> Unit,
    onOpenReview: (String) -> Unit,
    onDelete: (String) -> Unit,
    onClearAll: () -> Unit
) {
    var confirmClear by remember { mutableStateOf(false) }

    Column(Modifier.fillMaxSize()) {
        YellowHeader(
            title = "Score history",
            subtitle = "${results.size} saved result(s)",
            onBack = onBack,
            trailing = {
                if (results.isNotEmpty()) {
                    IconButton(onClick = { confirmClear = true }) {
                        Icon(Icons.Filled.DeleteSweep, contentDescription = "Clear all", tint = PureBlack)
                    }
                }
            }
        )

        if (results.isEmpty()) {
            Column(Modifier.fillMaxSize().padding(20.dp)) {
                NotePanel("No quiz results yet.\nStart your first football quiz to build your history.")
            }
            return@Column
        }

        LazyColumn(Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            items(results.size) { i ->
                val r = results[i]
                Row(
                    Modifier.fillMaxWidth().clip(RoundedCornerShape(16.dp)).background(WhiteCard)
                        .clickableCard { onOpenReview(r.id) }.padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    ScoreBadge(r.scorePercent, size = 52)
                    Spacer(Modifier.size(12.dp))
                    Column(Modifier.weight(1f)) {
                        Text(r.category.displayName, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = CardDarkText)
                        Text("${r.correctAnswers}/${r.totalQuestions} correct • ${r.date} ${r.time}", style = MaterialTheme.typography.bodyMedium, color = SecondaryGray)
                        Spacer(Modifier.size(6.dp))
                        DifficultyChip(r.difficulty)
                    }
                    IconButton(onClick = { onDelete(r.id) }) {
                        Icon(Icons.Filled.Delete, contentDescription = "Delete", tint = IncorrectRed)
                    }
                }
            }
        }
    }

    if (confirmClear) {
        AlertDialog(
            onDismissRequest = { confirmClear = false },
            title = { Text("Clear all history?") },
            text = { Text("This permanently removes all saved quiz results on this device.") },
            confirmButton = {
                TextButton(onClick = { onClearAll(); confirmClear = false }) { Text("Clear all", color = IncorrectRed) }
            },
            dismissButton = { TextButton(onClick = { confirmClear = false }) { Text("Cancel") } }
        )
    }
}
