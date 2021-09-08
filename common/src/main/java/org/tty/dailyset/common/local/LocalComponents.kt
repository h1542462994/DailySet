package org.tty.dailyset.common.local

import androidx.lifecycle.ViewModel
import org.tty.dioc.core.local.ComponentLocal
import org.tty.dioc.core.local.ComponentLogger
import org.tty.dioc.core.local.staticComponentLocalOf
import org.tty.dioc.util.Logger

/**
 * declare a [ComponentLocal] for [ViewModel].
 */
val ComponentViewModel = staticComponentLocalOf<ViewModel>()

/**
 * declare a [ComponentLocal] for [Logger]
 */
val ComponentLog = staticComponentLocalOf<Logger> {
    return@staticComponentLocalOf ULog
}

val viewModel get() = ComponentViewModel.current

val logger get() = ComponentLogger.current