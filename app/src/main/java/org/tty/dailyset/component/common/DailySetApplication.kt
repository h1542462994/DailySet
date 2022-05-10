package org.tty.dailyset.component.common

import android.app.Application
import android.content.Context
import android.view.Window
import kotlinx.coroutines.*
import org.tty.dailyset.MainActions
import org.tty.dailyset.Nav
import org.tty.dailyset.actor.*
import org.tty.dailyset.common.local.logger
import org.tty.dailyset.database.DailySetRoomDatabase
import org.tty.dailyset.datasource.DataSourceCollection
import org.tty.dailyset.datasource.GrpcSourceCollection
import org.tty.dailyset.datasource.grpc.GrpcSourceCollectionImpl
import org.tty.dailyset.datasource.net.NetSourceCollectionImpl
import org.tty.dailyset.datasource.runtime.RuntimeDataSourceImpl
import org.tty.dailyset.provider.LocalSharedComponents

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
        mutableSharedComponents.useDatabase(
            DailySetRoomDatabase.getDatabase(
                this,
                mutableSharedComponents.applicationScope
            )
        )
        // repository
        mutableSharedComponents.useActorCollection(
            ActorCollection(
                userActor = UserActor(sharedComponents = mutableSharedComponents),
                preferenceActor = PreferenceActor(sharedComponents = mutableSharedComponents),
                dailySetActor = DailySetActor(sharedComponents = mutableSharedComponents),
                messageActor = MessageActor(sharedComponents = mutableSharedComponents)
            )
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
    override val actorCollection: ActorCollection
        get() = mutableSharedComponents.actorCollection
    override val stateStore: StateStore
        get() = mutableSharedComponents.stateStore
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

    override fun useNav(nav: Nav<MainActions>) {
        mutableSharedComponents.useNav(nav)
        initializeStart("nav")
    }

    override fun useWindow(window: Window) {
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
                    withContext(Dispatchers.IO) {
                        mutableSharedComponents.dataSourceCollection.netSourceCollection.init()
                        mutableSharedComponents.dataSourceCollection.grpcSourceCollection.init()

                        mutableSharedComponents.actorCollection.userActor.init()
                    }
                }
            }
        }
    }

}