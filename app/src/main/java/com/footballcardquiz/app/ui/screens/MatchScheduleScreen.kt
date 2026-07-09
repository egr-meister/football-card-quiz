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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.footballcardquiz.app.data.model.MatchSource
import com.footballcardquiz.app.data.model.NormalizedMatch
import com.footballcardquiz.app.ui.components.NotePanel
import com.footballcardquiz.app.ui.components.RefereeCardChip
import com.footballcardquiz.app.ui.components.YellowHeader
import com.footballcardquiz.app.ui.theme.CardDarkText
import com.footballcardquiz.app.ui.theme.DeepCharcoal
import com.footballcardquiz.app.ui.theme.PureBlack
import com.footballcardquiz.app.ui.theme.SecondaryGray
import com.footballcardquiz.app.ui.theme.SoftYellowPanel
import com.footballcardquiz.app.ui.theme.StrongYellow
import com.footballcardquiz.app.ui.theme.WhiteCard
import com.footballcardquiz.app.viewmodel.MatchScheduleUi
import com.footballcardquiz.app.viewmodel.MatchScheduleViewModel

@Composable
fun MatchScheduleScreen(
    viewModel: MatchScheduleViewModel,
    onBack: () -> Unit,
    onOpenSettings: () -> Unit
) {
    LaunchedEffect(Unit) { viewModel.loadInitial() }
    val ui by viewModel.ui.collectAsState()

    Column(Modifier.fillMaxSize()) {
        YellowHeader(
            title = "Match Schedule",
            subtitle = "Optional reference feature",
            onBack = onBack,
            trailing = {
                Row {
                    IconButton(onClick = { viewModel.refresh() }) {
                        Icon(Icons.Filled.Refresh, contentDescription = "Refresh", tint = PureBlack)
                    }
                    IconButton(onClick = onOpenSettings) {
                        Icon(Icons.Filled.Tune, contentDescription = "Match settings", tint = PureBlack)
                    }
                }
            }
        )

        when (val s = ui) {
            is MatchScheduleUi.Loading -> {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = DeepCharcoal)
                }
            }
            is MatchScheduleUi.Content -> {
                LazyColumn(Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    item {
                        NotePanel("Match data is provided by football-data.org and may depend on your API plan.")
                    }
                    item {
                        Column(Modifier.fillMaxWidth().clip(RoundedCornerShape(14.dp)).background(WhiteCard).padding(14.dp)) {
                            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                                Text("Date window", style = MaterialTheme.typography.labelMedium, color = SecondaryGray)
                                SourceChip(s.source)
                            }
                            Text(
                                "${s.dateFrom} → ${s.dateTo}",
                                style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = CardDarkText
                            )
                            if (s.usingDefaultWindow) {
                                Text("Today + 9 days (default)", style = MaterialTheme.typography.labelMedium, color = SecondaryGray)
                            }
                            Text("Last updated: ${s.lastUpdated}", style = MaterialTheme.typography.labelMedium, color = SecondaryGray)
                        }
                    }
                    if (s.message.isNotBlank()) {
                        item { NotePanel(s.message) }
                    }
                    if (s.matches.isEmpty()) {
                        item {
                            NotePanel("No matches available.\nTry refreshing or check the API settings.")
                        }
                    } else {
                        items(s.matches.size) { i -> MatchCard(s.matches[i]) }
                    }
                }
            }
        }
    }
}

@Composable
private fun SourceChip(source: MatchSource) {
    val label = when (source) {
        MatchSource.Api -> "Live"
        MatchSource.Cache -> "Cached"
        MatchSource.Demo -> "Demo"
    }
    RefereeCardChip(label)
}

@Composable
private fun MatchCard(match: NormalizedMatch) {
    Column(Modifier.fillMaxWidth().clip(RoundedCornerShape(16.dp)).background(WhiteCard).padding(16.dp)) {
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
            Text(
                match.competitionName.ifBlank { "Unknown competition" },
                style = MaterialTheme.typography.labelMedium, color = SecondaryGray, fontWeight = FontWeight.Bold
            )
            if (match.competitionCode.isNotBlank()) RefereeCardChip(match.competitionCode)
        }
        Spacer(Modifier.height(10.dp))
        Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            Text(match.homeTeam.ifBlank { "Unknown" }, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = CardDarkText, modifier = Modifier.weight(1f))
            Box(
                Modifier.clip(RoundedCornerShape(8.dp)).background(SoftYellowPanel).padding(horizontal = 12.dp, vertical = 6.dp)
            ) {
                val score = if (match.homeScore != null && match.awayScore != null)
                    "${match.homeScore} - ${match.awayScore}" else "vs"
                Text(score, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.ExtraBold, color = DeepCharcoal)
            }
            Text(match.awayTeam.ifBlank { "Unknown" }, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = CardDarkText, modifier = Modifier.weight(1f), textAlign = androidx.compose.ui.text.style.TextAlign.End)
        }
        Spacer(Modifier.height(10.dp))
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text(
                buildString {
                    append(match.date.ifBlank { "Date TBD" })
                    if (match.time.isNotBlank()) append(" • ${match.time}")
                },
                style = MaterialTheme.typography.bodyMedium, color = SecondaryGray
            )
            Text(match.status.ifBlank { "SCHEDULED" }, style = MaterialTheme.typography.labelMedium, color = SecondaryGray, fontWeight = FontWeight.Bold)
        }
    }
}
