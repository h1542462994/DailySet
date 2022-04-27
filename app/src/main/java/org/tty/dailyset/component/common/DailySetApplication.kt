package org.tty.dailyset.component.common

import android.app.Application
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import org.tty.dailyset.database.DailySetRoomDatabase
import org.tty.dailyset.datasource.DataSourceCollection
import org.tty.dailyset.datasource.DbSourceCollection
import org.tty.dailyset.datasource.db.*
import org.tty.dailyset.repository.*

/**
 * Provide services for application
 */
class DailySetApplication: Application(), SharedComponents {

    private var mutableSharedComponents: MutableSharedComponents = MutableSharedComponents()

    init {
        mutableSharedComponents.useApplicationScope { CoroutineScope(SupervisorJob()) }
        mutableSharedComponents.useDatabase { DailySetRoomDatabase.getDatabase(this, mutableSharedComponents.applicationScope) }
        mutableSharedComponents.useDbSourceCollection { object: DbSourceCollection {
            override val userDao: UserDao get() = mutableSharedComponents.database.userDao()
            override val preferenceDao: PreferenceDao get() = mutableSharedComponents.database.preferenceDao()
            override val dailySetDao: DailySetDao get() = mutableSharedComponents.database.dailySetDao()
            override val dailyTableDao: DailyTableDao get() = mutableSharedComponents.database.dailyTableDao()
            override val dailyRowDao: DailyRowDao get() = mutableSharedComponents.database.dailyRowDao()
            override val dailyCellDao: DailyCellDao get() = mutableSharedComponents.database.dailyCellDao()
            override val dailyDurationDao: DailyDurationDao get() = mutableSharedComponents.database.dailyDurationDao()
            override val dailySetBindingDao: DailySetBindingDao get() = mutableSharedComponents.database.dailySetBindingDao()
        } }
        mutableSharedComponents.useUserRepository {
            UserRepository(mutableSharedComponents)
        }
        mutableSharedComponents.usePreferenceRepository {
            PreferenceRepository(mutableSharedComponents)
        }
        mutableSharedComponents.useDailyTableRepository {
            DailyTableRepository(mutableSharedComponents)
        }
        mutableSharedComponents.useDailySetRepository {
            DailySetRepository(mutableSharedComponents)
        }
    }

    override val applicationScope: CoroutineScope
        get() = mutableSharedComponents.applicationScope
    override val database: DailySetRoomDatabase
        get() = mutableSharedComponents.database
    override val dataSourceCollection: DataSourceCollection
        get() = mutableSharedComponents.dataSourceCollection
    override val repositoryCollection: RepositoryCollection
        get() = mutableSharedComponents.repositoryCollection
    override val stateStore: StateStore
        get() = mutableSharedComponents.stateStore


}