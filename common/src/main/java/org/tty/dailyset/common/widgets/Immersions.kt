package org.tty.dailyset.common.widgets

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.ViewCompat

/**
 * TODO: 完成该功能
 */
@Composable
fun ImmersionControl(style: ImmersionStyle) {

    val view = LocalView.current
    val window = window()
    val windowInsetsController = ViewCompat.getWindowInsetsController(view)
    LaunchedEffect(key1 = window) {
        if (window != null) {
            window.statusBarColor = style.statusBarColor.toArgb()
            window.navigationBarColor = style.navigationBarColor.toArgb()
        }
    }
}