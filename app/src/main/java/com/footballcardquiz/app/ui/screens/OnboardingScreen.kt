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
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.footballcardquiz.app.ui.components.NotePanel
import com.footballcardquiz.app.ui.components.RefereeCardChip
import com.footballcardquiz.app.ui.theme.CardDarkText
import com.footballcardquiz.app.ui.theme.DeepCharcoal
import com.footballcardquiz.app.ui.theme.PureBlack
import com.footballcardquiz.app.ui.theme.StrongYellow
import com.footballcardquiz.app.ui.theme.WhiteCard

private data class Bullet(val title: String, val body: String)

@Composable
fun OnboardingScreen(onStart: () -> Unit, onSkip: () -> Unit) {
    val bullets = listOf(
        Bullet("Test your football knowledge", "Answer questions about rules, positions, cards, and match situations."),
        Bullet("Pick your level", "Choose Easy, Medium, or Hard for every quiz session."),
        Bullet("Track your best", "Save your best scores locally and browse your quiz history."),
        Bullet("Match Schedule (extra)", "View upcoming football matches as a secondary reference feature."),
        Bullet("Private by design", "No account. No ads. No betting. No official logos.")
    )

    Column(
        Modifier
            .fillMaxSize()
            .background(StrongYellow)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 24.dp, vertical = 32.dp)
    ) {
        // Quiz-card style logo block
        Box(
            Modifier
                .size(88.dp)
                .clip(RoundedCornerShape(18.dp))
                .background(DeepCharcoal),
            contentAlignment = Alignment.Center
        ) {
            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                RefereeCardChip("", red = false)
                RefereeCardChip("", red = true)
            }
        }
        Spacer(Modifier.height(20.dp))
        Text(
            "Football Card Quiz",
            style = MaterialTheme.typography.headlineLarge,
            color = PureBlack,
            fontWeight = FontWeight.ExtraBold
        )
        Text(
            "Football rules quiz & learning",
            style = MaterialTheme.typography.titleMedium,
            color = CardDarkText
        )
        Spacer(Modifier.height(24.dp))

        bullets.forEach { b ->
            Column(
                Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(WhiteCard)
                    .padding(16.dp)
            ) {
                Text(b.title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = CardDarkText)
                Spacer(Modifier.height(4.dp))
                Text(b.body, style = MaterialTheme.typography.bodyMedium, color = CardDarkText)
            }
        }

        NotePanel(
            "Football Card Quiz is a football quiz and learning app. Questions and answers are " +
                "educational and simplified. The app is not an official football rules source, referee " +
                "certification tool, or professional coaching tool."
        )

        Spacer(Modifier.height(20.dp))
        Button(
            onClick = onStart,
            modifier = Modifier.fillMaxWidth().height(54.dp),
            shape = RoundedCornerShape(14.dp),
            colors = ButtonDefaults.buttonColors(containerColor = DeepCharcoal, contentColor = StrongYellow)
        ) {
            Text("Start Quiz", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
        }
        Spacer(Modifier.height(10.dp))
        OutlinedButton(
            onClick = onSkip,
            modifier = Modifier.fillMaxWidth().height(50.dp),
            shape = RoundedCornerShape(14.dp)
        ) {
            Text("Skip", color = PureBlack, fontWeight = FontWeight.Bold)
        }
    }
}
