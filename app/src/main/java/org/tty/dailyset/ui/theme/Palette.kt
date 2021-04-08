package org.tty.dailyset.ui.theme

import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color

/**
 * an extensional ColorPalette to extern the colors of [androidx.compose.material.MaterialTheme]
 * you can use [org.tty.dailyset.ui.theme.DailySetPalette] to register the provider
 */
@Stable
class Palette(
    background1: Color = Gray5,
    background2: Color = Gray95,
    background3: Color = Teal300,
    backgroundColor: Color = Teal50,
    backgroundTransparent: Color = BlackTransparent,
    textColor: Color = Gray80,
    textColorTitle: Color = Teal700,
    textColorDetail: Color = Gray50,
) {
    /**
     * background Level 1 Color
     */
    var background1 by mutableStateOf(background1, structuralEqualityPolicy())
        internal set

    /**
     * background Level 2 Color
     */
    var background2 by mutableStateOf(background2, structuralEqualityPolicy())
        internal set

    var background3 by mutableStateOf(background3, structuralEqualityPolicy())
        internal set

    var backgroundColor by mutableStateOf(backgroundColor, structuralEqualityPolicy())
        internal set

    var backgroundTransparent by mutableStateOf(backgroundTransparent, structuralEqualityPolicy())
        internal set

    /**
     * textColor .black
     */
    var textColor by mutableStateOf(textColor, structuralEqualityPolicy())
        internal set

    /**
     * textColorTitle .primary
     */
    var textColorTitle by mutableStateOf(textColorTitle, structuralEqualityPolicy())
        internal set

    /**
     * if Light (204,204,204) (20% black) else (51,51,51)
     */
    var textColorDetail by mutableStateOf(textColorDetail, structuralEqualityPolicy())
        internal set

    fun copy(
        background1: Color = this.background1,
        background2: Color = this.background2,
        background3: Color = this.background3,
        backgroundColor: Color = this.backgroundColor,
        backgroundTransparent: Color = this.backgroundTransparent,
        textColor: Color = this.textColor,
        textColorTitle: Color = this.textColorTitle,
        textColorDetail: Color = this.textColorDetail,
    ) : Palette = Palette(
        background1,
        background2,
        background3,
        backgroundColor,
        backgroundTransparent,
        textColor,
        textColorTitle,
        textColorDetail
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