package org.tty.dailyset.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Typography
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
    textColorInValid = Gray20
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

    textColorInValid = Gray20
)

private val MTypography = DailyTypography(
    linkText = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Bold,
        fontSize = 18.sp
    ),
    buttonText = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Bold,
        fontSize = 18.sp
    )
)

object DailySetTheme {
    val color
        @Composable
        @ReadOnlyComposable
        get() = LocalPalette.current

    val typography
        @Composable
        @ReadOnlyComposable
        get() = LocalTypography.current
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

    val typography = MTypography

    // use materialTheme
    MaterialTheme(
        colors = colors,
        typography = Typography,
        shapes = Shapes,
    ) {
        val rememberPalette = remember { palette.copy() }
        val rememberTypography = remember { typography.copy() }
        CompositionLocalProvider(
            LocalPalette provides rememberPalette,
            LocalTypography provides rememberTypography
        ) {
            content()
        }
    }
}