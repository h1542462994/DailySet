package org.tty.dailyset.common.widgets

import android.app.Activity
import android.content.ContextWrapper
import android.view.Window
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

@Composable
fun window(): Window? {
    var context = LocalContext.current
    while (context is ContextWrapper) {
        if (context is Activity) return context.window
        context = context.baseContext
    }
    return null
}