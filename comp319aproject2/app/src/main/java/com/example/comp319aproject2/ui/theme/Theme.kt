// File: app/src/main/java/com/example/project2/ui/theme/Theme.kt
package com.example.project2.ui.theme

import android.app.Activity
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.graphics.toArgb

/**
 * Application theme with a dark color palette and purple accents.
 * Default is dark mode; pass useDarkTheme = false to use light colors.
 * Also syncs system status & nav bar colors to match the theme.
 */
@Composable
fun Project2Theme(
    useDarkTheme: Boolean = true,
    content: @Composable () -> Unit
) {
    val colors = if (useDarkTheme) DarkColorScheme else LightColorScheme

    // Update system bars to theme colors
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            // Status bar same as background
            window.statusBarColor = colors.background.toArgb()
            // Navigation bar matches surface
            window.navigationBarColor = colors.surface.toArgb()
        }
    }

    MaterialTheme(
        colorScheme = colors,
        typography = Typography
    ) {
        Surface(
            color = colors.background
        ) {
            content()
        }
    }
}