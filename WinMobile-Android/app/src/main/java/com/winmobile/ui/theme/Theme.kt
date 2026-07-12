package com.winmobile.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFF00D9FF),
    onPrimary = Color(0xFF0F172A),
    primaryContainer = Color(0xFF004B5C),
    onPrimaryContainer = Color(0xFF00D9FF),
    secondary = Color(0xFF7C3AED),
    onSecondary = Color(0xFFFFFFFF),
    secondaryContainer = Color(0xFF5B21B6),
    onSecondaryContainer = Color(0xFFF3E8FF),
    tertiary = Color(0xFF00FF00),
    onTertiary = Color(0xFF0F172A),
    error = Color(0xFFFF5252),
    onError = Color(0xFFFFFFFF),
    background = Color(0xFF0F172A),
    onBackground = Color(0xFFFFFFFF),
    surface = Color(0xFF1A1F3A),
    onSurface = Color(0xFFFFFFFF),
)

@Composable
fun WinMobileTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = DarkColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = WinMobileTypography,
        content = content
    )
}
