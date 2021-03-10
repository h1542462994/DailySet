package org.tty.dailyset.ui.theme

import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color

/**
 * an extensional ColorPalette to extern the colors of [androidx.compose.material.MaterialTheme]
 * you can use [org.tty.dailyset.ui.theme.DailySetPalette] to register the provider
 */
@Stable
class Palette(
    gray5: Color = Gray5,
    gray20: Color = Gray20,
    gray80: Color = Gray80,
    gray95: Color = Gray95
) {
    /**
     * if Light (242,242,242) (5% black) else (13,13,13)
     */
    var gray5 by mutableStateOf(gray5, structuralEqualityPolicy())
        internal set

    /**
     * if Light (204,204,204) (20% black) else (51,51,51)
     */
    var gray20 by mutableStateOf(gray20, structuralEqualityPolicy())
        internal set
    /**
     * if Light (51,51,51) (80% black) else (204,204,204)
     */
    var gray80 by mutableStateOf(gray80, structuralEqualityPolicy())
        internal set

    /**
     * if Light (13,13,13) (5% black) else (242,242,242)
     */
    var gray95 by mutableStateOf(gray95, structuralEqualityPolicy())
        internal set

    fun copy(
        gray5: Color = this.gray5,
        gray20: Color = this.gray20,
        gray80: Color = this.gray80,
        gray95: Color = this.gray95
    ) : Palette = Palette(
        gray5,
        gray20,
        gray80,
        gray95
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