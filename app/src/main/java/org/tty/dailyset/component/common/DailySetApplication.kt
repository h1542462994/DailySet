package org.tty.dailyset.component.common

import android.app.Application
import android.content.Context
import android.view.Window
import androidx.lifecycle.Lifecycle
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import org.tty.dailyset.MainActions
import org.tty.dailyset.Nav
import org.tty.dailyset.common.local.logger
import org.tty.dailyset.database.DailySetRoomDatabase
import org.tty.dailyset.datasource.DataSourceCollection
import org.tty.dailyset.datasource.DbSourceCollection
import org.tty.dailyset.datasource.GrpcSourceCollection
import org.tty.dailyset.datasource.db.*
import org.tty.dailyset.datasource.grpc.GrpcSourceCollectionImpl
import org.tty.dailyset.datasource.net.NetSourceCollectionImpl
import org.tty.dailyset.datasource.runtime.RuntimeDataSourceImpl
import org.tty.dailyset.provider.LocalSharedComponents
import org.tty.dailyset.repository.*

/**
 * Provide services for application
 */
class DailySetApplication : Application(), SharedComponents {

    private var mutableSharedComponents: MutableSharedComponents = MutableSharedComponents()

    override fun onCreate() {
        super.onCreate()
        // WARNING: 你必须在这里进行初始化声明，否则会报错。
        LocalSharedComponents provides this

        logger.d("DailySetApplication", "called DailySetApplication.onCreate()")
        mutableSharedComponents.useApplicationScope(CoroutineScope(SupervisorJob()))
        mutableSharedComponents.useDatabase(DailySetRoomDatabase.getDatabase(this, mutableSharedComponents.applicationScope))
        mutableSharedComponents.useDbSourceCollection(object: DbSourceCollection {
            override val userDao: UserDao get() = mutableSharedComponents.database.userDao()
            override val preferenceDao: PreferenceDao get() = mutableSharedComponents.database.preferenceDao()
            override val dailySetDao: DailySetDao get() = mutableSharedComponents.database.dailySetDao()
            override val dailyTableDao: DailyTableDao get() = mutableSharedComponents.database.dailyTableDao()
            override val dailyRowDao: DailyRowDao get() = mutableSharedComponents.database.dailyRowDao()
            override val dailyCellDao: DailyCellDao get() = mutableSharedComponents.database.dailyCellDao()
            override val dailyDurationDao: DailyDurationDao get() = mutableSharedComponents.database.dailyDurationDao()
            override val dailySetBindingDao: DailySetBindingDao get() = mutableSharedComponents.database.dailySetBindingDao()
        })
        // repository
        mutableSharedComponents.useUserRepository(
            UserRepository(mutableSharedComponents)
        )
        mutableSharedComponents.usePreferenceRepository(
            PreferenceRepository(mutableSharedComponents)
        )
        mutableSharedComponents.useDailyTableRepository(
            DailyTableRepository(mutableSharedComponents)
        )
        mutableSharedComponents.useDailyRepository(
            DailyRepository(mutableSharedComponents)
        )
        mutableSharedComponents.useRuntimeDataSource(
            RuntimeDataSourceImpl(mutableSharedComponents)
        )
        mutableSharedComponents.useStateStore(
            StateStoreImpl(mutableSharedComponents)
        )
        mutableSharedComponents.useNetSourceCollection(
            NetSourceCollectionImpl(mutableSharedComponents)
        )
        useGrpcSourceCollection(
            GrpcSourceCollectionImpl(mutableSharedComponents)
        )
        mutableSharedComponents.useLtsVMSaver(LtsVMSaver())
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
    override val lifecycle: Lifecycle
        get() = mutableSharedComponents.lifecycle
    override val activityScope: CoroutineScope
        get() = mutableSharedComponents.activityScope
    override val nav: Nav<MainActions>
        get() = mutableSharedComponents.nav
    override val window: Window
        get() = mutableSharedComponents.window
    override val activityContext: Context
        get() = mutableSharedComponents.activityContext
    override val ltsVMSaver: LtsVMSaver
        get() = mutableSharedComponents.ltsVMSaver

    override fun useLifecycle(lifecycle: Lifecycle) {
        mutableSharedComponents.useLifecycle(lifecycle)
//        mutableSharedComponents.dataSourceCollection.runtimeDataSource.init()
        initializeStart("lifecycle")
    }

    override fun useNav(nav: Nav<MainActions>) {
        mutableSharedComponents.useNav(nav)
        initializeStart("nav")
    }

    override fun useWindow(window:  Window) {
        mutableSharedComponents.useWindow(window)
        initializeStart("window")
    }

    override fun useActivityContext(context: Context) {
        mutableSharedComponents.useActivityContext(context)
        initializeStart("activityContext")
    }

    override fun useActivityScope(scope: CoroutineScope) {
        mutableSharedComponents.useActivityScope(scope)
        initializeStart("activityScope")
    }

    private fun useGrpcSourceCollection(grpcSourceCollection: GrpcSourceCollection) {
        mutableSharedComponents.useGrpcSourceCollection(grpcSourceCollection)
        initializeStart("grpcSourceCollection")
    }

    private val componentAvailable = mutableMapOf(
        "lifecycle" to false,
        "nav" to false,
        "window" to false,
        "activityContext" to false,
        "activityScope" to false,
        "grpcSourceCollection" to false
    )
    private var initialized = false

    private fun initializeStart(component: String) {
        componentAvailable[component] = true

        if (componentAvailable.entries.all { it.value }) {
            if (!initialized) {
                initialized = true

                applicationScope.launch {

                    mutableSharedComponents.repositoryCollection.userRepository.init()
                    mutableSharedComponents.dataSourceCollection.netSourceCollection.init()
                    mutableSharedComponents.dataSourceCollection.grpcSourceCollection.init()
                }
            }
        }
    }

}