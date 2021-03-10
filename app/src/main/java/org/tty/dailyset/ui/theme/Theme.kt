package org.tty.dailyset.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable

private val DarkColorPalette = darkColors(
    primary = Purple200,
    primaryVariant = Purple700,
    secondary = Teal200
)

private val LightColorPalette = lightColors(
    primary = Purple500,
    primaryVariant = Purple700,
    secondary = Teal700

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
    gray5 = Gray5,
    gray20 = Gray20,
    gray80 = Gray80,
    gray95 = Gray95
)

private val DarkPalette = Palette(
    gray5 = Gray95,
    gray20 = Gray80,
    gray80 = Gray20,
    gray95 = Gray5
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