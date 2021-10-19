package org.tty.dailyset

import android.app.Application
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import org.tty.dailyset.data.DailySetRoomDatabase
import org.tty.dailyset.data.repository.DailySetRepository
import org.tty.dailyset.data.repository.DailyTableRepository
import org.tty.dailyset.data.repository.PreferenceRepository
import org.tty.dailyset.data.repository.UserRepository

/**
 * Provide services for application
 */
class DailySetApplication: Application() {
    private val applicationScope = CoroutineScope(SupervisorJob())
    val database by lazy { DailySetRoomDatabase.getDatabase(this, applicationScope) }
    val preferenceRepository by lazy { PreferenceRepository(database.preferenceDao()) }
    val userRepository by lazy { UserRepository(database.userDao()) }
    val dailyTableRepository by lazy {
        DailyTableRepository(
            database.dailyTableDao(),
            database.dailyRowDao(),
            database.dailyCellDao()
        )
    }
    val dailySetRepository by lazy {
        DailySetRepository(
            database.dailySetDao(),
            database.dailyDurationDao(),
            database.dailySetBindingDao()
        )
    }
}