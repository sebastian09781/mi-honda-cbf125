package com.example.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkMotoColorScheme = darkColorScheme(
    primary = MotoGreen,
    onPrimary = Color(0xFF0F1704),
    primaryContainer = MotoGreenContainer,
    onPrimaryContainer = MotoGreenBright,
    secondary = MotoGreenBright,
    onSecondary = Color(0xFF0F1704),
    secondaryContainer = DarkContainerPill,
    onSecondaryContainer = MotoGreenBright,
    tertiary = MotoGreenMuted,
    onTertiary = Color.White,
    background = DarkBackground,
    onBackground = DarkTextPrimary,
    surface = DarkSurface,
    onSurface = DarkTextPrimary,
    surfaceVariant = DarkSurfaceSubtle,
    onSurfaceVariant = DarkTextSecondary,
    outline = DarkBorder,
    error = DarkWarning,
    onError = Color.White,
    errorContainer = DarkWarningContainer,
    onErrorContainer = DarkWarningText
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = true,
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = DarkMotoColorScheme,
        typography = Typography,
        content = content
    )
}
