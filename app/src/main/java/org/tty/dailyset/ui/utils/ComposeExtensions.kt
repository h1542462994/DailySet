package org.tty.dailyset.ui.utils

import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.unit.Dp
import org.tty.dailyset.provider.LocalWindow
import org.tty.dailyset.ui.theme.LocalPalette

@Composable
fun measuredWidthDp(): Dp {
    return toDp(px = measuredWidth())
}

@Composable
fun measuredWidth(): Float {
    return LocalView.current.measuredWidth.toFloat()
    //return LocalView.current.measuredWidth.toFloat()
}

@Composable
fun measuredHeightDp(): Dp {
    return toDp(px = measuredHeight())
}

@Composable
fun measuredHeight(): Float {
    return LocalView.current.measuredHeight.toFloat()
}

@Composable
fun toPx(dp: Dp): Float {
    return with(LocalDensity.current) {
        dp.toPx()
    }
}

@Composable
fun toDp(px: Float): Dp {
    return with(LocalDensity.current) {
        px.toDp()
    }
}

@Composable
fun StatusBarToBackground1() {
    val window = LocalWindow.current
    val background1 = LocalPalette.current.background1
    LaunchedEffect(key1 = LocalPalette.current.background1) {
        window.navigationBarColor = background1.toArgb()
    }
}

@Composable
fun StatusBarToBackground() {
    val window = LocalWindow.current
    val background1 = LocalPalette.current.background1
    val background = MaterialTheme.colors.background
    LaunchedEffect(key1 = LocalPalette.current.background1) {
        window.navigationBarColor = background.toArgb()
    }
}