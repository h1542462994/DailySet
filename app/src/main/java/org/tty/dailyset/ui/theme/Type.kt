package org.tty.dailyset.ui.theme

import androidx.compose.material.Typography
import androidx.compose.runtime.*
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

// Set of Material typography styles to start with
val Typography = Typography(
    body1 = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 18.sp
    )
    /* Other default text styles to override
    button = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.W500,
        fontSize = 14.sp
    ),
    caption = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp
    )
    */
)


class DailyTypography(
    linkText: TextStyle = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Bold,
        fontSize = 18.sp
    ),
    buttonText: TextStyle = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Medium,
        fontSize = 16.sp
    ),
    subTitleText: TextStyle = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Medium,
        fontSize = 16.sp
    )
) {
    var linkText by mutableStateOf(linkText, structuralEqualityPolicy())
        internal set
    var buttonText by mutableStateOf(buttonText, structuralEqualityPolicy())
        internal set
    var subTitleText by mutableStateOf(subTitleText, structuralEqualityPolicy())
        internal set

    fun copy(
        linkText: TextStyle = this.linkText,
        buttonText: TextStyle = this.buttonText,
        subTitleText: TextStyle = this.subTitleText
    ): DailyTypography {
        return DailyTypography(linkText = linkText, buttonText = buttonText, subTitleText = subTitleText)
    }
}


val LocalTypography = staticCompositionLocalOf {
    DailyTypography()
}

