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

val viewModel get() = ComponentViewModel.current

val logger: Logger by lazy {
    ComponentLogger provides ULog
    ComponentLogger.current
}