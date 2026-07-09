package com.footballcardquiz.app.ui.screens

import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.footballcardquiz.app.data.model.MatchScheduleSettings
import com.footballcardquiz.app.ui.components.NotePanel
import com.footballcardquiz.app.ui.components.SectionCard
import com.footballcardquiz.app.ui.components.YellowHeader
import com.footballcardquiz.app.ui.theme.CardDarkText
import com.footballcardquiz.app.ui.theme.DeepCharcoal
import com.footballcardquiz.app.ui.theme.IncorrectRed
import com.footballcardquiz.app.ui.theme.SecondaryGray
import com.footballcardquiz.app.ui.theme.StrongYellow
import com.footballcardquiz.app.util.AppDateTime

@Composable
fun MatchScheduleSettingsScreen(
    settings: MatchScheduleSettings,
    hasToken: Boolean,
    onBack: () -> Unit,
    onUpdate: ((MatchScheduleSettings) -> MatchScheduleSettings) -> Unit,
    onClearCache: () -> Unit,
    onApplied: () -> Unit
) {
    var dateFrom by remember { mutableStateOf(settings.dateFrom) }
    var dateTo by remember { mutableStateOf(settings.dateTo) }
    var competition by remember { mutableStateOf(settings.competitionCode) }

    // Validation
    val fromValid = dateFrom.isBlank() || AppDateTime.isValidDate(dateFrom)
    val toValid = dateTo.isBlank() || AppDateTime.isValidDate(dateTo)
    val rangeValid = when {
        dateFrom.isBlank() && dateTo.isBlank() -> true
        !fromValid || !toValid -> false
        dateFrom.isBlank() || dateTo.isBlank() -> true
        else -> AppDateTime.isRangeValid(dateFrom, dateTo)
    }

    Column(Modifier.fillMaxSize()) {
        YellowHeader(title = "Match settings", subtitle = "Secondary feature options", onBack = onBack)
        Column(
            Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            if (!hasToken) {
                NotePanel("No API token is configured, so demo matches are used. Add a token in local.properties to load live data.")
            }

            SectionCard {
                ToggleRow("API enabled", "Allow live requests to football-data.org", settings.apiEnabled) { v ->
                    onUpdate { it.copy(apiEnabled = v) }
                }
                Spacer(Modifier.height(8.dp))
                ToggleRow("Use demo data", "Always show local demo matches", settings.useDemoData) { v ->
                    onUpdate { it.copy(useDemoData = v) }
                }
            }

            SectionCard {
                Text("Date window", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = CardDarkText)
                Text("Leave both blank for the default: today + 9 days.", style = MaterialTheme.typography.bodyMedium, color = SecondaryGray)
                Spacer(Modifier.height(10.dp))
                OutlinedTextField(
                    value = dateFrom,
                    onValueChange = { dateFrom = it.trim() },
                    label = { Text("dateFrom (YYYY-MM-DD)") },
                    singleLine = true,
                    isError = !fromValid,
                    modifier = Modifier.fillMaxWidth()
                )
                if (!fromValid) Text("Enter a valid date like ${AppDateTime.today()}, or leave blank.", color = IncorrectRed, style = MaterialTheme.typography.labelMedium)
                Spacer(Modifier.height(8.dp))
                OutlinedTextField(
                    value = dateTo,
                    onValueChange = { dateTo = it.trim() },
                    label = { Text("dateTo (YYYY-MM-DD)") },
                    singleLine = true,
                    isError = !toValid || !rangeValid,
                    modifier = Modifier.fillMaxWidth()
                )
                if (!toValid) Text("Enter a valid date, or leave blank.", color = IncorrectRed, style = MaterialTheme.typography.labelMedium)
                else if (!rangeValid) Text("dateTo must not be earlier than dateFrom.", color = IncorrectRed, style = MaterialTheme.typography.labelMedium)
            }

            SectionCard {
                Text("Competition code filter", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = CardDarkText)
                Text("Optional. Filters matches locally by competition code (e.g. DEMO).", style = MaterialTheme.typography.bodyMedium, color = SecondaryGray)
                Spacer(Modifier.height(10.dp))
                OutlinedTextField(
                    value = competition,
                    onValueChange = { competition = it.trim() },
                    label = { Text("Competition code (optional)") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            Button(
                onClick = {
                    onUpdate {
                        it.copy(
                            dateFrom = if (fromValid) dateFrom else "",
                            dateTo = if (toValid && rangeValid) dateTo else "",
                            competitionCode = competition
                        )
                    }
                    onApplied()
                    onBack()
                },
                enabled = rangeValid && fromValid && toValid,
                modifier = Modifier.fillMaxWidth().height(52.dp),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(containerColor = DeepCharcoal, contentColor = StrongYellow)
            ) { Text("Save & apply", fontWeight = FontWeight.Bold) }

            OutlinedButton(
                onClick = {
                    dateFrom = ""; dateTo = ""; competition = ""
                    onUpdate { it.copy(dateFrom = "", dateTo = "", competitionCode = "") }
                    onApplied()
                },
                modifier = Modifier.fillMaxWidth().height(50.dp),
                shape = RoundedCornerShape(14.dp)
            ) { Text("Reset to default 10-day window", color = CardDarkText, fontWeight = FontWeight.Bold) }

            OutlinedButton(
                onClick = onClearCache,
                modifier = Modifier.fillMaxWidth().height(50.dp),
                shape = RoundedCornerShape(14.dp)
            ) { Text("Clear match cache", color = IncorrectRed, fontWeight = FontWeight.Bold) }

            NotePanel("API availability may depend on the chosen competitions, date range, and your current API plan.")
            Spacer(Modifier.height(20.dp))
        }
    }
}

@Composable
private fun ToggleRow(title: String, subtitle: String, checked: Boolean, onChange: (Boolean) -> Unit) {
    Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
        Column(Modifier.weight(1f)) {
            Text(title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = CardDarkText)
            Text(subtitle, style = MaterialTheme.typography.bodyMedium, color = SecondaryGray)
        }
        Switch(checked = checked, onCheckedChange = onChange)
    }
}
