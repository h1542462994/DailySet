package org.tty.dailyset.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

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
    background1 = Gray1,
    background3 = Teal300,
    backgroundColor = Teal50,
    backgroundTransparent = BlackTransparent,
    backgroundDialog = Color.White,
    primary = Gray95,
    primaryColor = Blue600,
    lineColor = Gray10,
    subColor = Gray40,
    textColorInValid = Gray20
)

private val DarkPalette = Palette(
    background1 = Gray90,
    background3 = Teal700,
    backgroundColor = Teal900,
    backgroundTransparent = WhiteTransparent,
    backgroundDialog = Gray85,
    primary = Gray1,
    primaryColor = Blue300,
    lineColor = Gray80,
    subColor = Gray20,
    textColorInValid = Gray90
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