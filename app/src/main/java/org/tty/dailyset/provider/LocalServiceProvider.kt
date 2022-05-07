package org.tty.dailyset.provider

import android.view.Window
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import org.tty.dailyset.common.local.ComponentViewModel
//import org.tty.dailyset.component.common.MainViewModel
import org.tty.dailyset.component.common.SharedComponents
import org.tty.dioc.core.local.staticComponentLocalOf

/**
 * provide the service [LocalSharedComponents0] and [LocalMainViewModel],
 * seed the database
 */
@Composable
fun LocalServiceProvider(
    sharedComponents: SharedComponents,
/*    mainViewModel: MainViewModel,*/
    window: Window,
    content: @Composable () -> Unit
) {


    // provides the compositionLocal
    CompositionLocalProvider(
        LocalSharedComponents0 provides sharedComponents,
        /*LocalMainViewModel provides mainViewModel,*/
        LocalWindow provides window,
        content = content)
}

internal val LocalSharedComponents = staticComponentLocalOf<SharedComponents> {
    error("No LocalShared Component0 Provided.")
}

internal val LocalSharedComponents0 = compositionLocalOf<SharedComponents> {
    error("No DailySetService Provided")
}

/*internal val LocalMainViewModel = compositionLocalOf<MainViewModel> {
    error("No MainViewModel Provided")
}*/

internal val LocalWindow = compositionLocalOf<Window> {
    error("No Window Provided")
}


/*

*/
/**
 * [MainViewModel] hold by [ComponentViewModel]
 * you must call in application region.
 *//*

internal val mainViewModel get() = ComponentViewModel.current as MainViewModel*/
