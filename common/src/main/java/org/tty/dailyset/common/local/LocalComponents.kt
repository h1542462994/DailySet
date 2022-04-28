// the file defines several components

package org.tty.dailyset.common.local

import androidx.lifecycle.ViewModel
import org.tty.dioc.core.local.ComponentLocal
import org.tty.dioc.core.local.ComponentLogger
import org.tty.dioc.core.local.staticComponentLocalOf
import org.tty.dioc.util.Logger

/**
 * declare a [ComponentLocal] for [ViewModel].
 */
@Deprecated("viewModel should not be exported.")
val ComponentViewModel = staticComponentLocalOf<ViewModel>()

val ComponentLoggerConfig = staticComponentLocalOf<LoggerConfig>()

/**
 * the [ViewModel] for current module, get from [ComponentViewModel]
 * @see ComponentViewModel
 */
@Deprecated("viewModel should not be exported.", ReplaceWith("ComponentViewModel.current"))
val viewModel get() = ComponentViewModel.current

/**
 * the [Logger] for current module, get from [ComponentLogger].
 * @see [ULog]
 * @see [ComponentLogger]
 */
val logger: Logger by lazy {
    ComponentLogger provides ULog
    ComponentLogger.current
}

val loggerConfig: LoggerConfig by lazy {
    ComponentLoggerConfig provides ULog
    ComponentLoggerConfig.current
}
