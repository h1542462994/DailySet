package org.tty.dailyset.provider

import android.app.Application
import androidx.compose.runtime.*
import org.tty.dailyset.DailySetApplication
import org.tty.dailyset.viewmodel.MainViewModel

/**
 * provide the service [LocalServices] and [LocalMainViewModel],
 * seed the database
 */
@Composable
fun LocalServiceProvider(
    application: Application,
    mainViewModel: MainViewModel,
    content: @Composable () -> Unit
) {
    val dailySetApplicationService = application as DailySetApplication


    // provides the compositionLocal
    CompositionLocalProvider(
        LocalServices provides dailySetApplicationService,
        LocalMainViewModel provides mainViewModel,
        content = content)
}


internal val LocalServices = compositionLocalOf<DailySetApplication> {
    error("No DailySetService Provided")
}

internal val LocalMainViewModel = compositionLocalOf<MainViewModel> {
    error("No MainViewModel Provided")
}
