package org.tty.dailyset.common.widgets

import androidx.compose.runtime.Stable
import androidx.compose.ui.graphics.Color

@Stable
data class ImmersionStyle(
    val statusBarColor: Color,
    val darkIcons: Boolean,
    val navigationBarColor: Color
)

fun lightImmersionStyle(
    statusBarColor: Color = Color.Transparent,
    navigationBarColor: Color = Color.Transparent
): ImmersionStyle {
    return ImmersionStyle(statusBarColor, true, navigationBarColor)
}

fun darkImmersionStyle(
    statusBarColor: Color = Color.Transparent,
    navigationBarColor: Color = Color.Transparent
): ImmersionStyle {
    return ImmersionStyle(statusBarColor, false, navigationBarColor)
}

