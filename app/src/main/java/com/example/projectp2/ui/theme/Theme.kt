package com.example.projectp2.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = Purple80,
    secondary = PurpleGrey80,
    tertiary = Pink80,
    background = PaletteTokens.Neutral6,
    surface = PaletteTokens.Neutral30,
    surfaceBright = PaletteTokens.Neutral35,
    surfaceDim = PaletteTokens.Neutral24
)

private val LightColorScheme = lightColorScheme(
    primary = Purple40,
    secondary = PurpleGrey40,
    tertiary = Pink40,
    background = PaletteTokens.Neutral98,
    surface = PaletteTokens.Neutral90,
    surfaceBright = PaletteTokens.Neutral87,
    surfaceDim = PaletteTokens.Neutral92,

    /* Other default colors to override
    background = Color(0xFFFFFBFE),
    surface = Color(0xFFFFFBFE),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = Color(0xFF1C1B1F),
    onSurface = Color(0xFF1C1B1F),
    */
)

val ForestTheme = lightColorScheme(
    primary = Color(0xFF2E7D32),         // Deep green
    background = Color(0xFFE8F5E9),      // Light green tint
    surface = Color(0xFFD1E8D2),         // Muted green
    surfaceBright = Color(0xFFB7E0B8),   // Brighter mint
    surfaceDim = Color(0xFFADE1AF),      // Slightly darker green
    onPrimary = Color.White,             // White text on deep green
    onBackground = Color.Black,           // Black text on light background
    onSurface = Color.Black               // Black text on green
)

val SkyTheme = darkColorScheme(
    primary = Color(0xFF90CAF9),         // Soft blue
    background = Color(0xFF121212),      // True dark
    surface = Color(0xFF212121),         // Slightly lifted black
    surfaceBright = Color(0xFF2C2C2C),   // Brighter dark
    surfaceDim = Color(0xFF1E1E1E),      // Darkest tone
    onPrimary = Color.Black,             // Dark text on soft blue
    onBackground = Color.LightGray,           // White text on dark background
    onSurface = Color.LightGray               // White text on dark surface
)

val StrawberryTheme = lightColorScheme(
    primary = Color(0xFFE91E63),         // Vibrant pink
    background = Color(0xFFFFF1F3),      // Creamy white-pink
    surface = Color(0xFFFFDCE0),         // Soft pink tint
    surfaceBright = Color(0xFFFFCBD4),   // Brighter pink
    surfaceDim = Color(0xFFFFC7CE),      // Muted pink
    onPrimary = Color.White,             // White on pink
    onBackground = Color.Black,           // Black on cream
    onSurface = Color.Black               // Black text on pink
)

val OceanTheme = lightColorScheme(
    primary = Color(0xFF0288D1),         // Rich ocean blue
    background = Color(0xFFE1F5FE),      // Light watery blue
    surface = Color(0xFFB3E5FC),         // Sky blue
    surfaceBright = Color(0xFF94DAFC),   // Bright water tone
    surfaceDim = Color(0xFF89D6FC),      // Deeper sea blue
    onPrimary = Color.White,             // White text on rich blue
    onBackground = Color.Black,           // Black text on light background
    onSurface = Color.Black               // Black text on sky blue
)

val SunriseTheme = lightColorScheme(
    primary = Color(0xFFFFA726),         // Orange sunrise
    background = Color(0xFFFFF3E0),      // Pale sunrise cream
    surface = Color(0xFFFFE0B2),         // Warm base
    surfaceBright = Color(0xFFFFCC80),   // Brighter amber
    surfaceDim = Color(0xFFFCC982),      // Rich orange
    onPrimary = Color.Black,             // Black text on light orange
    onBackground = Color.Black,           // Black on warm background
    onSurface = Color.Black               // Black text on warm surface
)

@Composable
fun ProjectP2Theme(
    theme: AppTheme,
    content: @Composable () -> Unit
) {
    val colorScheme = when (theme) {
        AppTheme.System -> if (isSystemInDarkTheme()) DarkColorScheme else LightColorScheme
        AppTheme.Dark -> DarkColorScheme
        AppTheme.Light -> LightColorScheme
        AppTheme.Forest -> ForestTheme
        AppTheme.Sky -> SkyTheme
        AppTheme.Strawberry -> StrawberryTheme
        AppTheme.Ocean -> OceanTheme
        AppTheme.Sunrise -> SunriseTheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}