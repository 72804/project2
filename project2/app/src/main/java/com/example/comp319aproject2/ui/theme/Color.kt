// File: app/src/main/java/com/example/project2/ui/theme/Colors.kt
package com.example.project2.ui.theme

import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color

// Purple accents
val Purple80 = Color(0xFFD0BCFF)
val PurpleGrey80 = Color(0xFFCCC2DC)

// Teal for contrast
val Teal200 = Color(0xFF03DAC5)

// Dark theme palette (slightly lighter background & surface)
val DarkColorScheme = darkColorScheme(
    primary = Purple80,
    onPrimary = Color.Black,
    secondary = Teal200,
    onSecondary = Color.Black,
    background = Color(0xFF181818),    // lighter than 121212
    onBackground = Color.White,
    surface = Color(0xFF242424),       // lighten surface a bit
    onSurface = Color.White,
    tertiary = PurpleGrey80,
    onTertiary = Color.White
)

// Light theme palette (unchanged)
val LightColorScheme = lightColorScheme(
    primary = Purple80,
    onPrimary = Color.White,
    secondary = Teal200,
    onSecondary = Color.Black,
    background = Color(0xFFFFFFFF),
    onBackground = Color.Black,
    surface = Color(0xFFF2F2F2),
    onSurface = Color.Black,
    tertiary = PurpleGrey80,
    onTertiary = Color.Black
)


