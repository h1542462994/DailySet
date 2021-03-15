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
    gray20: Color = Gray20,
    textColor: Color = Gray80,
    gray95: Color = Gray95,
    textColorTitle: Color = Pink900,
) {
    /**
     * if Light (242,242,242) else (51,51,51)
     */
    var background1 by mutableStateOf(background1, structuralEqualityPolicy())
        internal set

    /**
     * if Light (204,204,204) (20% black) else (51,51,51)
     */
    var gray20 by mutableStateOf(gray20, structuralEqualityPolicy())
        internal set
    /**
     * if Light (51,51,51) else (204,204,204)
     */
    var textColor by mutableStateOf(textColor, structuralEqualityPolicy())
        internal set

    /**
     * if Light (13,13,13) (5% black) else (242,242,242)
     */
    var gray95 by mutableStateOf(gray95, structuralEqualityPolicy())
        internal set

    var textColorTitle by mutableStateOf(textColorTitle, structuralEqualityPolicy())
        internal set


    fun copy(
        background1: Color = this.background1,
        gray20: Color = this.gray20,
        textColor: Color = this.textColor,
        gray95: Color = this.gray95,
        textColorTitle: Color = this.textColorTitle
    ) : Palette = Palette(
        background1,
        gray20,
        textColor,
        gray95,
        textColorTitle
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