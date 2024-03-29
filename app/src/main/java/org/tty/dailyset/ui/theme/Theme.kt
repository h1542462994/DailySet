package org.tty.dailyset.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

private val DarkColorPalette = darkColors(
    primary = Blue300,
    primaryVariant = Purple700,
    secondary = Purple200
)

private val LightColorPalette = lightColors(
    primary = Blue600,
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
    background2 = Gray5,
    background3 = Teal300,
    backgroundColor = Blue50,
    backgroundTransparent = BlackTransparent,
    backgroundDialog = Color.White,
    backgroundInvalid = Gray5,
    primary = Gray95,
    primarySurface = Gray1,
    primaryColor = Blue600,
    lineColor = Gray10,
    sub = Gray40,
    subColor = Blue400,
    error = LightError,
    textColorInValid = Gray60,
    statusGray = Gray40,
    statusGreen = LightGreen,
    statusOrange = LightOrange,
    statusRed = LightRed,
)

private val DarkPalette = Palette(
    background1 = Gray90,
    background2 = Gray95,
    background3 = Teal700,
    backgroundColor = Blue900,
    backgroundTransparent = WhiteTransparent,
    backgroundDialog = Gray85,
    backgroundInvalid = Gray80,
    primary = Gray1,
    primarySurface = Gray95,
    primaryColor = Blue300,
    lineColor = Gray80,
    subColor = Blue400,
    sub = Gray20,
    error = DarkError,
    textColorInValid = Gray20,
    statusGray = Gray60,
    statusGreen = DarkGreen,
    statusOrange = DarkOrange,
    statusRed = DarkRed,
)

private val MTypography = DailyTypography()

object DailySetTheme {
    val color
        @Composable
        @ReadOnlyComposable
        get() = LocalPalette.current

    val typography
        @Composable
        @ReadOnlyComposable
        get() = LocalTypography.current

    val courseColors
        @Composable
        @ReadOnlyComposable
        get() = LocalCoursePalette.current
}

@Composable
fun DailySetPalette(
    palette: Palette,
    content: @Composable () -> Unit
) {
    val rememberedPalette = remember { palette.copy() }
    CompositionLocalProvider(LocalPalette provides rememberedPalette, content = content)
}


/**
 * use [MaterialTheme] and [LocalPalette]
 */
@Composable
fun DailySetTheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable () -> Unit) {
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

    val coursePalette = if (darkTheme) {
        CoursePalette(
            backgrounds = ForegroundCourses,
            foregrounds = BackgroundCourses
        )
    } else {
        CoursePalette(
            backgrounds = BackgroundCourses,
            foregrounds = ForegroundCourses
        )
    }

    val typography = MTypography

    // use materialTheme
    MaterialTheme(
        colors = colors,
        typography = Typography,
        shapes = Shapes,
    ) {
        val rememberPalette = remember { palette.copy() }.apply {
            updatePaletteFrom(palette)
        }
        val rememberTypography = remember { typography.copy() }
        val rememberCoursePalette = remember { coursePalette }
        CompositionLocalProvider(
            LocalPalette provides rememberPalette,
            LocalTypography provides rememberTypography,
            LocalCoursePalette provides rememberCoursePalette
        ) {
            content()
        }
    }
}