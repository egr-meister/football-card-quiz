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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.footballcardquiz.app.data.local.QuestionBank
import com.footballcardquiz.app.data.model.QuizCategory
import com.footballcardquiz.app.data.model.QuizDifficulty
import com.footballcardquiz.app.ui.components.DifficultyChip
import com.footballcardquiz.app.ui.components.YellowHeader
import com.footballcardquiz.app.ui.components.clickableCard
import com.footballcardquiz.app.ui.theme.CardDarkText
import com.footballcardquiz.app.ui.theme.DeepCharcoal
import com.footballcardquiz.app.ui.theme.SecondaryGray
import com.footballcardquiz.app.ui.theme.StrongYellow
import com.footballcardquiz.app.ui.theme.WhiteCard

@Composable
fun CategorySelectScreen(
    difficulty: QuizDifficulty,
    onBack: () -> Unit,
    onSelect: (QuizCategory?) -> Unit  // null = Mixed Quiz
) {
    Column(Modifier.fillMaxSize()) {
        YellowHeader(title = "Choose category", subtitle = "Then your quiz begins", onBack = onBack)
        LazyColumn(
            Modifier.fillMaxSize().padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("Difficulty:", style = MaterialTheme.typography.bodyMedium, color = SecondaryGray)
                    Spacer(Modifier.size(8.dp))
                    DifficultyChip(difficulty)
                }
            }
            // Mixed quiz (all categories) — highlighted dark card
            item {
                Column(
                    Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(DeepCharcoal)
                        .clickableCard { onSelect(null) }
                        .padding(18.dp)
                ) {
                    Text("Mixed Quiz", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.ExtraBold, color = StrongYellow)
                    Text("Questions from all categories", style = MaterialTheme.typography.bodyMedium, color = WhiteCard)
                }
            }
            items(QuizCategory.entries.size) { index ->
                val c = QuizCategory.entries[index]
                val count = QuestionBank.countFor(difficulty, c)
                Column(
                    Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(WhiteCard)
                        .clickableCard { onSelect(c) }
                        .padding(18.dp)
                ) {
                    Text(c.displayName, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = CardDarkText)
                    Spacer(Modifier.height(2.dp))
                    Text(
                        if (count > 0) "$count question(s) at ${difficulty.displayName}"
                        else "Uses available questions if this level is limited",
                        style = MaterialTheme.typography.labelMedium,
                        color = SecondaryGray
                    )
                }
            }
        }
    }
}
