package org.tty.dailyset.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable

private val DarkColorPalette = darkColors(
    primary = Teal200,
    primaryVariant = Purple700,
    secondary = Purple200
)

private val LightColorPalette = lightColors(
    primary = Teal700,
    primaryVariant = Purple700,
    secondary = Purple700

    /* Other default colors to override
    background = Color.White,
    surface = Color.White,
    onPrimary = Color.White,
    onSecondary = Color.Black,
    onBackground = Color.Black,
    onSurface = Color.Black,
    */
)

private val LightPalette = Palette(
    background1 = Gray5,
    gray20 = Gray20,
    textColor = Gray80,
    gray95 = Gray95,
    textColorTitle = Teal700,
)

private val DarkPalette = Palette(
    background1 = Gray80,
    gray20 = Gray80,
    textColor = Gray20,
    gray95 = Gray5,
    textColorTitle = Teal200
)

/**
 * use [MaterialTheme] and [LocalPalette]
 */
@Composable
fun DailySetTheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable() () -> Unit) {
    val colors = if (darkTheme) {
        DarkColorPalette
    } else {
        LightColorPalette
    }

    val palette = if (darkTheme) {
        DarkPalette
    } else {
        LightPalette
    }

    // use materialTheme
    MaterialTheme(
        colors = colors,
        typography = Typography,
        shapes = Shapes,
    ) {
        // use LocalPalette
        DailySetPalette(palette = palette, content = content)
    }
}