package org.tty.dailyset.ui.utils

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.unit.Dp

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