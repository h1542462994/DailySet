package org.tty.dailyset.provider

import android.app.Application
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import org.tty.dailyset.MainViewModel
import org.tty.dailyset.data.DailySetRoomDatabase
import org.tty.dailyset.data.repository.PreferenceRepository

/**
 * Provide services for application
 */
class DailySetApplication: Application() {
    val applicationScope = CoroutineScope(SupervisorJob())
    val database by lazy { DailySetRoomDatabase.getDatabase(this, applicationScope) }
    val preferenceRepository by lazy { PreferenceRepository(database.preferenceDao()) }
}

/**
 * provide the service [LocalServices]
 */
@Composable
fun LocalServiceProvider(
    application: Application,
    mainViewModel: MainViewModel,
    content: @Composable () -> Unit
) {
    val dailySetApplicationService = application as DailySetApplication
    CompositionLocalProvider(
        LocalServices provides dailySetApplicationService,
        LocalMainViewModel provides mainViewModel
        , content = content)
}

internal val LocalServices = compositionLocalOf<DailySetApplication> {
    error("No DailySetService Provided")
}

internal val LocalMainViewModel = compositionLocalOf<MainViewModel> {
    error("No MainViewModel Provided")
}
