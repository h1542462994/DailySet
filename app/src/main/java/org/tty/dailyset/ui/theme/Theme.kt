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
    background2 = Gray20,
    backgroundColor = Teal50,
    textColor = Gray80,
    textColorTitle = Teal700,
    textColorDetail = Gray50,
)

private val DarkPalette = Palette(
    background1 = Gray80,
    background2 = Gray5,
    backgroundColor = Teal900,
    textColor = Gray80,
    textColorTitle = Teal200,
    textColorDetail = Gray40
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