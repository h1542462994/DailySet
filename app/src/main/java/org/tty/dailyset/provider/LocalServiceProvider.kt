package org.tty.dailyset.provider

import android.app.Application
import android.view.Window
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import org.tty.dailyset.DailySetApplication
import org.tty.dailyset.common.local.ComponentViewModel
import org.tty.dailyset.viewmodel.MainViewModel

/**
 * provide the service [LocalServices] and [LocalMainViewModel],
 * seed the database
 */
@Composable
fun LocalServiceProvider(
    application: Application,
    mainViewModel: MainViewModel,
    window: Window,
    content: @Composable () -> Unit
) {
    val dailySetApplicationService = application as DailySetApplication


    // provides the compositionLocal
    CompositionLocalProvider(
        LocalServices provides dailySetApplicationService,
        LocalMainViewModel provides mainViewModel,
        LocalWindow provides window,
        content = content)
}


internal val LocalServices = compositionLocalOf<DailySetApplication> {
    error("No DailySetService Provided")
}

internal val LocalMainViewModel = compositionLocalOf<MainViewModel> {
    error("No MainViewModel Provided")
}

internal val LocalWindow = compositionLocalOf<Window>() {
    error("No Window Provided")
}

/**
 * [MainViewModel] hold by [ComponentViewModel]
 * you must call in application region.
 */
internal val mainViewModel get() = ComponentViewModel.current() as MainViewModel