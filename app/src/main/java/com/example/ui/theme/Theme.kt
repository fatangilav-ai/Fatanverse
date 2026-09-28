package com.example.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val FatanverseDarkColorScheme = darkColorScheme(
    primary = CyanCore,
    onPrimary = Color.Black,
    primaryContainer = SlateDark800,
    onPrimaryContainer = CyanCore,
    secondary = GoldenSun,
    onSecondary = Color.Black,
    secondaryContainer = SlateDark700,
    onSecondaryContainer = GoldenSun,
    tertiary = RoseCrimson,
    onTertiary = Color.White,
    background = SpaceDark950,
    onBackground = TextPrimaryDark,
    surface = SlateDark900,
    onSurface = TextPrimaryDark,
    surfaceVariant = SlateDark800,
    onSurfaceVariant = TextSecondaryDark,
    error = MalignantRed,
    onError = Color.White
)

private val FatanverseLightColorScheme = lightColorScheme(
    primary = CyanCoreVariant,
    onPrimary = Color.White,
    primaryContainer = Color(0xFFE0F2FE),
    onPrimaryContainer = Color(0xFF0369A1),
    secondary = Color(0xFFD97706),
    onSecondary = Color.White,
    tertiary = RoseCrimson,
    onTertiary = Color.White,
    background = Color(0xFFF8FAFC),
    onBackground = Color(0xFF0F172A),
    surface = Color.White,
    onSurface = Color(0xFF0F172A),
    surfaceVariant = Color(0xFFE2E8F0),
    onSurfaceVariant = Color(0xFF475569)
)

@Composable
fun FatanverseTheme(
    darkTheme: Boolean = true, // Sci-fi default dark
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) FatanverseDarkColorScheme else FatanverseLightColorScheme
    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}

// Backwards compatibility alias
@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = true,
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    FatanverseTheme(darkTheme = darkTheme, content = content)
}
