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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.footballcardquiz.app.data.model.QuizDifficulty
import com.footballcardquiz.app.ui.components.YellowHeader
import com.footballcardquiz.app.ui.components.clickableCard
import com.footballcardquiz.app.ui.components.difficultyColor
import com.footballcardquiz.app.ui.theme.CardDarkText
import com.footballcardquiz.app.ui.theme.SecondaryGray
import com.footballcardquiz.app.ui.theme.WhiteCard

private fun describe(d: QuizDifficulty): String = when (d) {
    QuizDifficulty.Easy -> "Basic questions about core rules and positions."
    QuizDifficulty.Medium -> "Moderately challenging questions on situations and roles."
    QuizDifficulty.Hard -> "More specific questions on detailed rules and tactics."
}

@Composable
fun DifficultySelectScreen(onBack: () -> Unit, onSelect: (QuizDifficulty) -> Unit) {
    Column(Modifier.fillMaxSize()) {
        YellowHeader(title = "Choose difficulty", subtitle = "Pick how tough your quiz should be", onBack = onBack)
        Column(Modifier.padding(16.dp)) {
            QuizDifficulty.entries.forEach { d ->
                Row(
                    Modifier
                        .fillMaxWidth()
                        .padding(bottom = 14.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(WhiteCard)
                        .clickableCard { onSelect(d) }
                        .padding(18.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(Modifier.size(46.dp).clip(RoundedCornerShape(10.dp)).background(difficultyColor(d)))
                    Spacer(Modifier.size(16.dp))
                    Column(Modifier.weight(1f)) {
                        Text(d.displayName, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.ExtraBold, color = CardDarkText)
                        Spacer(Modifier.height(2.dp))
                        Text(describe(d), style = MaterialTheme.typography.bodyMedium, color = SecondaryGray)
                    }
                }
            }
        }
    }
}
