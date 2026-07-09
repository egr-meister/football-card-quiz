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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
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
import com.footballcardquiz.app.data.model.AppData
import com.footballcardquiz.app.data.model.QuizCategory
import com.footballcardquiz.app.data.model.QuizDifficulty
import com.footballcardquiz.app.ui.components.NotePanel
import com.footballcardquiz.app.ui.components.SectionCard
import com.footballcardquiz.app.ui.components.YellowHeader
import com.footballcardquiz.app.ui.components.clickableCard
import com.footballcardquiz.app.ui.theme.CardDarkText
import com.footballcardquiz.app.ui.theme.DeepCharcoal
import com.footballcardquiz.app.ui.theme.IncorrectRed
import com.footballcardquiz.app.ui.theme.SecondaryGray
import com.footballcardquiz.app.ui.theme.SoftYellowPanel
import com.footballcardquiz.app.ui.theme.StrongYellow
import com.footballcardquiz.app.ui.theme.WhiteCard
import com.footballcardquiz.app.BuildConfig

@Composable
fun SettingsScreen(
    appData: AppData,
    onBack: () -> Unit,
    onDefaultDifficulty: (QuizDifficulty) -> Unit,
    onDefaultCategory: (QuizCategory?) -> Unit,
    onCompactMode: (Boolean) -> Unit,
    onMatchSettings: () -> Unit,
    onShowOnboarding: () -> Unit,
    onClearMatchCache: () -> Unit,
    onDeleteHistory: () -> Unit,
    onResetBest: () -> Unit,
    onResetAll: () -> Unit
) {
    val s = appData.settings
    val tokenConfigured = BuildConfig.FOOTBALL_DATA_API_TOKEN.isNotBlank() &&
        BuildConfig.FOOTBALL_DATA_API_TOKEN != "your_api_token_here"

    var confirm by remember { mutableStateOf<ConfirmAction?>(null) }

    Column(Modifier.fillMaxSize()) {
        YellowHeader(title = "Settings", subtitle = "Preferences & privacy", onBack = onBack)
        Column(
            Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // ---- Quiz preferences ----
            SectionCard {
                Text("Quiz preferences", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = CardDarkText)
                Spacer(Modifier.height(10.dp))
                Text("Default difficulty", style = MaterialTheme.typography.labelLarge, color = SecondaryGray)
                Spacer(Modifier.height(6.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    QuizDifficulty.entries.forEach { d ->
                        SelectPill(d.displayName, s.defaultDifficulty == d) { onDefaultDifficulty(d) }
                    }
                }
                Spacer(Modifier.height(12.dp))
                Text("Default category", style = MaterialTheme.typography.labelLarge, color = SecondaryGray)
                Spacer(Modifier.height(6.dp))
                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        SelectPill("Mixed", s.defaultCategory == null) { onDefaultCategory(null) }
                        SelectPill(QuizCategory.FootballRules.displayName, s.defaultCategory == QuizCategory.FootballRules) { onDefaultCategory(QuizCategory.FootballRules) }
                    }
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        SelectPill(QuizCategory.RefereeCards.displayName, s.defaultCategory == QuizCategory.RefereeCards) { onDefaultCategory(QuizCategory.RefereeCards) }
                        SelectPill(QuizCategory.PlayerPositions.displayName, s.defaultCategory == QuizCategory.PlayerPositions) { onDefaultCategory(QuizCategory.PlayerPositions) }
                    }
                }
                Spacer(Modifier.height(12.dp))
                Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                    Column(Modifier.weight(1f)) {
                        Text("Compact mode", style = MaterialTheme.typography.titleMedium, color = CardDarkText, fontWeight = FontWeight.Bold)
                        Text("Slightly tighter spacing", style = MaterialTheme.typography.bodyMedium, color = SecondaryGray)
                    }
                    Switch(checked = s.compactMode, onCheckedChange = onCompactMode)
                }
            }

            // ---- Match schedule ----
            SectionCard {
                Text("Match Schedule", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = CardDarkText)
                Spacer(Modifier.height(4.dp))
                Text(
                    "API status: " + if (tokenConfigured) "Token configured" else "No token — demo data",
                    style = MaterialTheme.typography.bodyMedium, color = SecondaryGray
                )
                Spacer(Modifier.height(10.dp))
                ActionRow("Match schedule settings") { onMatchSettings() }
                ActionRow("Clear match cache") { confirm = ConfirmAction.ClearCache }
            }

            // ---- Data management ----
            SectionCard {
                Text("Data", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = CardDarkText)
                Spacer(Modifier.height(6.dp))
                ActionRow("Show onboarding again") { onShowOnboarding() }
                ActionRow("Delete quiz history", destructive = true) { confirm = ConfirmAction.DeleteHistory }
                ActionRow("Reset best scores", destructive = true) { confirm = ConfirmAction.ResetBest }
                ActionRow("Reset all local data", destructive = true) { confirm = ConfirmAction.ResetAll }
            }

            // ---- App info & disclaimers ----
            SectionCard {
                Text("App information", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = CardDarkText)
                Spacer(Modifier.height(4.dp))
                Text("Football Card Quiz v${BuildConfig.VERSION_NAME}", style = MaterialTheme.typography.bodyMedium, color = SecondaryGray)
            }

            NotePanel(
                "Quiz disclaimer: Football Card Quiz is a football quiz and learning app. Questions and " +
                    "answers are educational and simplified. The app is not an official football rules source, " +
                    "referee certification tool, or professional coaching tool."
            )
            NotePanel(
                "Match schedule disclaimer: Match data is provided by football-data.org. Availability, accuracy, " +
                    "competitions, and update frequency depend on the API provider and the current API plan."
            )
            NotePanel(
                "Privacy note: Football Card Quiz stores quiz results, best scores, settings, and cached match " +
                    "data on this device. The app uses internet only to load football match data from " +
                    "football-data.org. No account, no ads, no analytics, no payments, no Firebase, no location, " +
                    "no notifications, no sensors, no Google Fit, and no Health Connect."
            )
            Spacer(Modifier.height(24.dp))
        }
    }

    confirm?.let { action ->
        AlertDialog(
            onDismissRequest = { confirm = null },
            title = { Text(action.title) },
            text = { Text(action.message) },
            confirmButton = {
                TextButton(onClick = {
                    when (action) {
                        ConfirmAction.ClearCache -> onClearMatchCache()
                        ConfirmAction.DeleteHistory -> onDeleteHistory()
                        ConfirmAction.ResetBest -> onResetBest()
                        ConfirmAction.ResetAll -> onResetAll()
                    }
                    confirm = null
                }) { Text(action.confirmLabel, color = IncorrectRed) }
            },
            dismissButton = { TextButton(onClick = { confirm = null }) { Text("Cancel") } }
        )
    }
}

private enum class ConfirmAction(val title: String, val message: String, val confirmLabel: String) {
    ClearCache("Clear match cache?", "This removes saved match data on this device.", "Clear"),
    DeleteHistory("Delete quiz history?", "This permanently removes all saved quiz results.", "Delete"),
    ResetBest("Reset best scores?", "This clears all best score records.", "Reset"),
    ResetAll("Reset all local data?", "This clears quiz history, best scores, settings, and match cache.", "Reset all")
}

@Composable
private fun SelectPill(label: String, selected: Boolean, onClick: () -> Unit) {
    Box(
        Modifier
            .clip(RoundedCornerShape(20.dp))
            .background(if (selected) DeepCharcoal else SoftYellowPanel)
            .clickableCard(onClick)
            .padding(horizontal = 14.dp, vertical = 8.dp)
    ) {
        Text(label, style = MaterialTheme.typography.labelLarge, color = if (selected) StrongYellow else CardDarkText)
    }
}

@Composable
private fun ActionRow(label: String, destructive: Boolean = false, onClick: () -> Unit) {
    Row(
        Modifier.fillMaxWidth().clip(RoundedCornerShape(10.dp)).clickableCard(onClick).padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            label,
            style = MaterialTheme.typography.bodyLarge,
            color = if (destructive) IncorrectRed else CardDarkText,
            fontWeight = if (destructive) FontWeight.Bold else FontWeight.Normal
        )
    }
}
