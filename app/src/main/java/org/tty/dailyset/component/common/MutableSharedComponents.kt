package org.tty.dailyset.component.common

import android.content.Context
import android.view.Window
import kotlinx.coroutines.CoroutineScope
import org.tty.dailyset.MainActions
import org.tty.dailyset.Nav
import org.tty.dailyset.actor.ActorCollection
import org.tty.dailyset.actor.DailySetActor
import org.tty.dailyset.actor.PreferenceActor
import org.tty.dailyset.actor.UserActor
import org.tty.dailyset.database.DailySetRoomDatabase
import org.tty.dailyset.datasource.DataSourceCollection
import org.tty.dailyset.datasource.GrpcSourceCollection
import org.tty.dailyset.datasource.NetSourceCollection
import org.tty.dailyset.datasource.runtime.RuntimeDataSource

class MutableSharedComponents : SharedComponents {

    private lateinit var applicationScopeInternal: CoroutineScope
    private val dataSourceCollectionInternal = MutableDataSourceCollection()
    private lateinit var databaseInternal:  DailySetRoomDatabase
    private lateinit var actorCollectionInternal: ActorCollection
    private lateinit var stateStoreInternal: StateStore
    private lateinit var navInternal: Nav<MainActions>
    private lateinit var windowInternal: Window
    private lateinit var activityContextInternal:  Context
    private lateinit var activityScopeInternal: CoroutineScope
    private lateinit var ltsVMSaverInternal: LtsVMSaver

    override val dataSourceCollection: DataSourceCollection get() = dataSourceCollectionInternal
    override val database: DailySetRoomDatabase get() = databaseInternal
    override val actorCollection: ActorCollection get() = actorCollectionInternal
    override val stateStore: StateStore get() = stateStoreInternal
    override val applicationScope: CoroutineScope get() = applicationScopeInternal
    override val nav: Nav<MainActions> get() = navInternal
    override val window: Window get() = windowInternal
    override val activityContext: Context get() = activityContextInternal
    override val activityScope: CoroutineScope get() = activityScopeInternal
    override val ltsVMSaver: LtsVMSaver get() = ltsVMSaverInternal
    class MutableDataSourceCollection: DataSourceCollection {
        internal lateinit var netSourceCollectionInternal: NetSourceCollection
        internal lateinit var runtimeDataSourceInternal: RuntimeDataSource
        internal lateinit var grpcSourceCollectionInternal: GrpcSourceCollection

        override val netSourceCollection: NetSourceCollection get() =  netSourceCollectionInternal
        override val runtimeDataSource: RuntimeDataSource get() = runtimeDataSourceInternal
        override val grpcSourceCollection: GrpcSourceCollection get() = grpcSourceCollectionInternal
    }

    fun useApplicationScope(applicationScope: CoroutineScope) {
        this.applicationScopeInternal = applicationScope
    }

    fun useDatabase(database: DailySetRoomDatabase) {
        this.databaseInternal = database
    }

    fun useNetSourceCollection(netSourceCollection:  NetSourceCollection) {
        this.dataSourceCollectionInternal.netSourceCollectionInternal = netSourceCollection
    }

    fun useActorCollection(actorCollection: ActorCollection) {
        this.actorCollectionInternal = actorCollection
    }

    fun useRuntimeDataSource(runtimeDataSource: RuntimeDataSource) {
        this.dataSourceCollectionInternal.runtimeDataSourceInternal = runtimeDataSource
    }

    fun useGrpcSourceCollection(grpcSourceCollection: GrpcSourceCollection) {
        this.dataSourceCollectionInternal.grpcSourceCollectionInternal = grpcSourceCollection
    }

    fun useStateStore(stateStore: StateStore) {
        this.stateStoreInternal = stateStore
    }

    override fun useNav(nav: Nav<MainActions>) {
        this.navInternal = nav
    }

    fun useLtsVMSaver(ltsVMSaver: LtsVMSaver) {
        this.ltsVMSaverInternal = ltsVMSaver
    }

    override fun useWindow(window: Window) {
        this.windowInternal = window
    }

    override fun useActivityContext(context: Context) {
        this.activityContextInternal = context
    }

    override fun useActivityScope(scope: CoroutineScope) {
        this.activityScopeInternal = scope
    }

}