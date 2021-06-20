package org.tty.dailyset.ui.theme

import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color

/**
 * an extensional ColorPalette to extern the colors of [androidx.compose.material.MaterialTheme]
 * you can use [org.tty.dailyset.ui.theme.DailySetPalette] to register the provider
 */
@Stable
class Palette(
    background1: Color = Gray1,
    background3: Color = Teal300,
    backgroundColor: Color = Teal50,
    backgroundTransparent: Color = BlackTransparent,
    backgroundDialog: Color = Color.White,
    primary: Color = Gray95,
    primaryColor: Color = Blue600,
    lineColor: Color = Gray95,
    subColor: Color = Gray40,
    textColorInValid: Color = Gray20,
) {
    /**
     * background Level 1 Color.
     * it will be used on title space and the first cover over background.
     * if (light) [Gray1] else [Gray90].
     */
    var background1 by mutableStateOf(background1, structuralEqualityPolicy())
        internal set



    var background3 by mutableStateOf(background3, structuralEqualityPolicy())
        internal set

    var backgroundColor by mutableStateOf(backgroundColor, structuralEqualityPolicy())
        internal set

    var backgroundTransparent by mutableStateOf(backgroundTransparent, structuralEqualityPolicy())
        internal set

    var backgroundDialog by mutableStateOf(backgroundDialog, structuralEqualityPolicy())
        internal set
    /**
     * primary Color black.
     * it will be used on primary text and icons
     * if (light) [Gray95] else [Gray40]
     */
    var primary by mutableStateOf(primary, structuralEqualityPolicy())
        internal set

    /**
     * primary Color colorful.
     * it will be used on primary colored text and icons
     * if (light) [Blue600] else [Blue300]
     */
    var primaryColor by mutableStateOf(primaryColor, structuralEqualityPolicy())
        internal set

    /**
     * lineColor, it will be more light over the [subColor]
     * if (light) [Gray10] else [Gray80]
     */
    var lineColor by mutableStateOf(lineColor, structuralEqualityPolicy())
        internal set

    /**
     * subColor
     * it will be used on text sub of the primary
     * if (light) [Gray40] else [Gray20]
     */
    var subColor by mutableStateOf(subColor, structuralEqualityPolicy())
        internal set

    var textColorInValid by mutableStateOf(textColorInValid, structuralEqualityPolicy())
        internal set

    fun copy(
        background1: Color = this.background1,
        background3: Color = this.background3,
        backgroundColor: Color = this.backgroundColor,
        backgroundTransparent: Color = this.backgroundTransparent,
        backgroundDialog: Color = this.backgroundDialog,
        primary: Color = this.primary,
        primaryColor: Color = this.primaryColor,
        lineColor: Color = this.lineColor,
        subColor: Color = this.subColor,
        textColorInValid: Color = this.textColorInValid
    ) : Palette = Palette(
        background1,
        background3,
        backgroundColor,
        backgroundTransparent,
        backgroundDialog,
        primary,
        primaryColor,
        lineColor,
        subColor,
        textColorInValid
    )
}

internal val LocalPalette = staticCompositionLocalOf<Palette> {
    Palette()
}

@Composable
fun DailySetPalette(
    palette: Palette,
    content: @Composable () -> Unit
) {
    val rememberedPalette = remember { palette.copy() }
    CompositionLocalProvider(LocalPalette provides rememberedPalette, content = content)
}