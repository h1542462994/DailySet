package org.tty.dailyset.provider

import android.app.Application
import android.util.Log
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import org.tty.dailyset.MainViewModel
import org.tty.dailyset.data.DailySetDatabaseSeeder
import org.tty.dailyset.data.DailySetRoomDatabase
import org.tty.dailyset.data.repository.PreferenceRepository
import org.tty.dailyset.model.entity.Preference
import org.tty.dailyset.model.entity.PreferenceName

/**
 * Provide services for application
 */
class DailySetApplication: Application() {
    val applicationScope = CoroutineScope(SupervisorJob())
    val database by lazy { DailySetRoomDatabase.getDatabase(this, applicationScope) }
    val preferenceRepository by lazy { PreferenceRepository(database.preferenceDao()) }
}

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
