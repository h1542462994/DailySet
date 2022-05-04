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
    background2: Color = Gray5,
    background3: Color = Teal300,
    backgroundColor: Color = Blue50,
    backgroundTransparent: Color = BlackTransparent,
    backgroundDialog: Color = Color.White,
    backgroundInvalid: Color = Gray80,
    primary: Color = Gray95,
    primarySurface: Color = Color.White,
    primaryColor: Color = Blue600,
    lineColor: Color = Gray95,
    sub: Color = Gray40,
    subColor: Color = Blue400,
    textColorInValid: Color = Gray20,
    statusGray: Color = Gray20,
    statusGreen: Color = LightGreen,
    statusOrange: Color = LightOrange,
    statusRed: Color = LightRed
) {
    /**
     * background Level 1 Color.
     * it will be used on title space and the first cover over background.
     * if (light) [Gray1] else [Gray90].
     */
    var background1 by mutableStateOf(background1, structuralEqualityPolicy())
        internal set

    var background2 by mutableStateOf(background2, structuralEqualityPolicy())
        internal set

    var background3 by mutableStateOf(background3, structuralEqualityPolicy())
        internal set

    /**
     * the colorful background
     * if (light) [Blue50] else [Blue900]
     */
    var backgroundColor by mutableStateOf(backgroundColor, structuralEqualityPolicy())
        internal set

    /**
     * the transparent color for dialog cover.
     */
    var backgroundTransparent by mutableStateOf(backgroundTransparent, structuralEqualityPolicy())
        internal set

    /**
     * the background color for dialog.
     * if (light) [Color.White] else [Gray85]
     */
    var backgroundDialog by mutableStateOf(backgroundDialog, structuralEqualityPolicy())
        internal set

    /**
     * the background color for invalid area
     */
    var backgroundInvalid by mutableStateOf(backgroundInvalid, structuralEqualityPolicy())
        internal set

    /**
     * primary Color black.
     * it will be used on primary text and icons
     * if (light) [Gray95] else [Gray40]
     */
    var primary by mutableStateOf(primary, structuralEqualityPolicy())
        internal set

    /**
     * primary Color surface
     * if (light) [Color.White] else [Gray95]
     */
    var primarySurface by mutableStateOf(primarySurface, structuralEqualityPolicy())
        internal set

    /**
     * primary Color colorful.
     * it will be used on primary colored text and icons
     * if (light) [Blue600] else [Blue300]
     */
    var primaryColor by mutableStateOf(primaryColor, structuralEqualityPolicy())
        internal set

    /**
     * lineColor, it will be more light over the [sub]
     * if (light) [Gray10] else [Gray80]
     */
    var lineColor by mutableStateOf(lineColor, structuralEqualityPolicy())
        internal set

    /**
     * sub
     * it will be used on text sub of the primary
     * if (light) [Gray40] else [Gray20]
     */
    var sub by mutableStateOf(sub, structuralEqualityPolicy())
        internal set

    /**
     * subColor
     * the subColor light to the primary
     * if (light) [Blue400]
     */
    var subColor by mutableStateOf(subColor, structuralEqualityPolicy())
        internal set

    var textColorInValid by mutableStateOf(textColorInValid, structuralEqualityPolicy())
        internal set

    var statusGray by mutableStateOf(statusGray, structuralEqualityPolicy())
        internal set

    var statusGreen by mutableStateOf(statusGreen, structuralEqualityPolicy())
        internal set

    var statusOrange by mutableStateOf(statusOrange, structuralEqualityPolicy())
        internal set

    var statusRed by mutableStateOf(statusRed, structuralEqualityPolicy())
        internal set

    fun copy(
        background1: Color = this.background1,
        background2: Color = this.background2,
        background3: Color = this.background3,
        backgroundColor: Color = this.backgroundColor,
        backgroundTransparent: Color = this.backgroundTransparent,
        backgroundDialog: Color = this.backgroundDialog,
        backgroundInvalid: Color = this.backgroundInvalid,
        primary: Color = this.primary,
        primarySurface: Color = this.primarySurface,
        primaryColor: Color = this.primaryColor,
        lineColor: Color = this.lineColor,
        sub: Color = this.sub,
        subColor: Color = this.subColor,
        textColorInValid: Color = this.textColorInValid,
        statusGray: Color = this.statusGray,
        statusGreen: Color = this.statusGreen,
        statusOrange: Color = this.statusOrange,
        statusRed: Color = this.statusRed
    ) : Palette = Palette(
        background1,
        background2,
        background3,
        backgroundColor,
        backgroundTransparent,
        backgroundDialog,
        backgroundInvalid,
        primary,
        primarySurface,
        primaryColor,
        lineColor,
        sub,
        subColor,
        textColorInValid,
        statusGray,
        statusGreen,
        statusOrange,
        statusRed
    )
}

internal val LocalPalette = compositionLocalOf<Palette> {
    Palette()
}

internal fun Palette.updatePaletteFrom(other: Palette) {
    background1 = other.background1
    background2 = other.background2
    background3 = other.background3
    backgroundColor = other.backgroundColor
    backgroundTransparent = other.backgroundTransparent
    backgroundDialog = other.backgroundDialog
    backgroundInvalid = other.backgroundInvalid
    primary = other.primary
    primarySurface = other.primarySurface
    primaryColor = other.primaryColor
    lineColor = other.lineColor
    sub = other.sub
    subColor = other.subColor
    textColorInValid = other.textColorInValid
}

