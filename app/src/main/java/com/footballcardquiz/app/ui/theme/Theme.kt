package com.footballcardquiz.app.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

// Single, stable light color scheme (bold yellow/black identity, no dark mode switch).
private val AppColorScheme = lightColorScheme(
    primary = DeepYellow,
    onPrimary = PureBlack,
    primaryContainer = StrongYellow,
    onPrimaryContainer = PureBlack,
    secondary = DarkGraphite,
    onSecondary = WhiteCard,
    background = LightAppBackground,
    onBackground = CardDarkText,
    surface = WhiteCard,
    onSurface = CardDarkText,
    surfaceVariant = SoftYellowPanel,
    onSurfaceVariant = CardDarkText,
    error = IncorrectRed,
    onError = WhiteCard,
    outline = MutedLabelGray
)

@Composable
fun FootballCardQuizTheme(
    // Force the branded scheme regardless of system dark mode for a consistent identity.
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = AppColorScheme,
        typography = AppTypography,
        content = content
    )
}
