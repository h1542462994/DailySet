package org.tty.dailyset.common.local

import androidx.lifecycle.ViewModel
import org.tty.dioc.core.local.ComponentLocal
import org.tty.dioc.core.local.Logger

/**
 * declare a [ComponentLocal] for [ViewModel].
 */
val ComponentViewModel = ComponentLocal<ViewModel>()


/**
 * declare a [ComponentLocal] for [Logger]
 */
val ComponentLog = ComponentLocal<Logger>().apply {
    this provides ULog
}

val viewModel get() = ComponentViewModel.current()

val logger get() = ComponentLog.current()