package com.footballcardquiz.app.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.footballcardquiz.app.data.model.QuizCategory
import com.footballcardquiz.app.data.model.QuizDifficulty
import com.footballcardquiz.app.ui.theme.CardDarkText
import com.footballcardquiz.app.ui.theme.DeepCharcoal
import com.footballcardquiz.app.ui.theme.InfoBlue
import com.footballcardquiz.app.ui.theme.PitchGreen
import com.footballcardquiz.app.ui.theme.PureBlack
import com.footballcardquiz.app.ui.theme.RefereeRedCard
import com.footballcardquiz.app.ui.theme.SoftYellowPanel
import com.footballcardquiz.app.ui.theme.StrongYellow
import com.footballcardquiz.app.ui.theme.WhiteCard

/** Bold yellow header bar with a black title and optional back button. */
@Composable
fun YellowHeader(
    title: String,
    subtitle: String? = null,
    onBack: (() -> Unit)? = null,
    trailing: (@Composable () -> Unit)? = null
) {
    Surface(color = StrongYellow, shadowElevation = 2.dp) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (onBack != null) {
                IconButton(onClick = onBack) {
                    Icon(
                        Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = PureBlack
                    )
                }
            } else {
                Box(Modifier.size(8.dp))
            }
            Column(Modifier.weight(1f).padding(start = 4.dp)) {
                Text(
                    title,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.ExtraBold,
                    color = PureBlack,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                if (!subtitle.isNullOrBlank()) {
                    Text(
                        subtitle,
                        style = MaterialTheme.typography.labelMedium,
                        color = DeepCharcoal
                    )
                }
            }
            if (trailing != null) trailing()
        }
    }
}

/** Small referee-card-style chip (yellow or red). */
@Composable
fun RefereeCardChip(label: String, red: Boolean = false) {
    val bg = if (red) RefereeRedCard else StrongYellow
    val fg = if (red) WhiteCard else PureBlack
    Box(
        Modifier
            .clip(RoundedCornerShape(4.dp))
            .background(bg)
            .padding(horizontal = 8.dp, vertical = 3.dp)
    ) {
        Text(label, style = MaterialTheme.typography.labelMedium, color = fg, fontWeight = FontWeight.Bold)
    }
}

@Composable
fun CategoryChip(category: QuizCategory, selected: Boolean = false, onClick: (() -> Unit)? = null) {
    val bg = if (selected) DeepCharcoal else SoftYellowPanel
    val fg = if (selected) StrongYellow else CardDarkText
    Box(
        Modifier
            .clip(RoundedCornerShape(20.dp))
            .background(bg)
            .then(if (onClick != null) Modifier.clickableNoRipple(onClick) else Modifier)
            .padding(horizontal = 14.dp, vertical = 8.dp)
    ) {
        Text(category.displayName, style = MaterialTheme.typography.labelLarge, color = fg)
    }
}

@Composable
fun DifficultyChip(difficulty: QuizDifficulty) {
    val color = difficultyColor(difficulty)
    Box(
        Modifier
            .clip(RoundedCornerShape(6.dp))
            .background(color)
            .padding(horizontal = 10.dp, vertical = 4.dp)
    ) {
        Text(
            difficulty.displayName,
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.Bold,
            color = if (difficulty == QuizDifficulty.Easy) PureBlack else WhiteCard
        )
    }
}

fun difficultyColor(d: QuizDifficulty): Color = when (d) {
    QuizDifficulty.Easy -> StrongYellow
    QuizDifficulty.Medium -> InfoBlue
    QuizDifficulty.Hard -> PitchGreen
}

/** Yellow circular score badge with a percent number. */
@Composable
fun ScoreBadge(percent: Int, size: Int = 72) {
    Box(
        Modifier
            .size(size.dp)
            .clip(RoundedCornerShape(50))
            .background(StrongYellow),
        contentAlignment = Alignment.Center
    ) {
        Text(
            "$percent%",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.ExtraBold,
            color = PureBlack
        )
    }
}
