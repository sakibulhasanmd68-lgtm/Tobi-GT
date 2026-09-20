package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val TobiDarkColorScheme = darkColorScheme(
    primary = TobiPrimary,
    onPrimary = TobiOnPrimary,
    primaryContainer = TobiPrimaryContainer,
    onPrimaryContainer = TobiOnPrimaryContainer,
    secondary = TobiSecondary,
    onSecondary = TobiOnSecondary,
    secondaryContainer = TobiSecondaryContainer,
    onSecondaryContainer = TobiOnSecondaryContainer,
    background = TobiBackground,
    onBackground = TobiOnBackground,
    surface = TobiSurface,
    onSurface = TobiOnSurface,
    surfaceVariant = TobiSurfaceVariant,
    onSurfaceVariant = TobiOnSurfaceVariant
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false, // Preserve Tobi GT cyberpunk brand palette
    content: @Composable () -> Unit,
) {
    MaterialTheme(
        colorScheme = TobiDarkColorScheme,
        typography = Typography,
        content = content
    )
}

