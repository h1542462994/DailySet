package org.tty.dailyset.component.common

import android.content.Context
import android.view.Window
import androidx.lifecycle.Lifecycle
import kotlinx.coroutines.CoroutineScope
import org.tty.dailyset.MainActions
import org.tty.dailyset.Nav
import org.tty.dailyset.database.DailySetRoomDatabase
import org.tty.dailyset.datasource.DataSourceCollection
import org.tty.dailyset.datasource.DbSourceCollection
import org.tty.dailyset.datasource.NetSourceCollection
import org.tty.dailyset.datasource.runtime.RuntimeDataSource
import org.tty.dailyset.repository.*

class MutableSharedComponents : SharedComponents {

    private lateinit var applicationScopeInternal: CoroutineScope
    private val dataSourceCollectionInternal = MutableDataSourceCollection()
    private lateinit var databaseInternal:  DailySetRoomDatabase
    private val repositoryCollectionInternal = MutableRepositoryCollection()
    private lateinit var stateStoreInternal: StateStore
    private lateinit var lifecycleInternal: Lifecycle
    private lateinit var navInternal: Nav<MainActions>
    private lateinit var windowInternal: Window
    private lateinit var activityContextInternal:  Context
    private lateinit var activityScopeInternal: CoroutineScope
    private lateinit var ltsVMSaverInternal: LtsVMSaver

    override val dataSourceCollection: DataSourceCollection get() = dataSourceCollectionInternal
    override val database: DailySetRoomDatabase get() = databaseInternal
    override val repositoryCollection: RepositoryCollection by lazy { repositoryCollectionInternal }
    override val stateStore: StateStore get() = stateStoreInternal
    override val applicationScope: CoroutineScope get() = applicationScopeInternal
    override val lifecycle: Lifecycle get() = lifecycleInternal
    override val nav: Nav<MainActions> get() = navInternal
    override val window: Window get() = windowInternal
    override val activityContext: Context get() = activityContextInternal
    override val activityScope: CoroutineScope get() = activityScopeInternal
    override val ltsVMSaver: LtsVMSaver get() = ltsVMSaverInternal
    class MutableDataSourceCollection: DataSourceCollection {
        internal lateinit var dbSourceCollectionInternal: DbSourceCollection
        internal lateinit var netSourceCollectionInternal: NetSourceCollection
        internal lateinit var runtimeDataSourceInternal: RuntimeDataSource

        override val dbSourceCollection: DbSourceCollection get() =  dbSourceCollectionInternal
        override val netSourceCollection: NetSourceCollection get() =  netSourceCollectionInternal
        override val runtimeDataSource: RuntimeDataSource get() = runtimeDataSourceInternal
    }

    class MutableRepositoryCollection: RepositoryCollection {
        internal lateinit var userRepositoryInternal:  UserRepository
        internal lateinit var preferenceRepositoryInternal:  PreferenceRepository
        internal lateinit var dailyTableRepositoryInternal:  DailyTableRepository
        internal lateinit var dailySetRepositoryInternal: DailyRepository

        override val userRepository: UserRepository get() = userRepositoryInternal
        override val preferenceRepository: PreferenceRepository get() = preferenceRepositoryInternal
        override val dailyTableRepository: DailyTableRepository get() = dailyTableRepositoryInternal
        override val dailySetRepository: DailyRepository get() = dailySetRepositoryInternal
    }

    fun useApplicationScope(applicationScope: CoroutineScope) {
        this.applicationScopeInternal = applicationScope
    }

    fun useDatabase(database: DailySetRoomDatabase) {
        this.databaseInternal = database
    }

    fun useDbSourceCollection(dbSourceCollection: DbSourceCollection) {
        this.dataSourceCollectionInternal.dbSourceCollectionInternal = dbSourceCollection
    }

    fun useNetSourceCollection(netSourceCollection:  NetSourceCollection) {
        this.dataSourceCollectionInternal.netSourceCollectionInternal = netSourceCollection
    }

    fun useUserRepository(userRepository: UserRepository) {
        this.repositoryCollectionInternal.userRepositoryInternal = userRepository
    }

    fun usePreferenceRepository(preferenceRepository: PreferenceRepository) {
        this.repositoryCollectionInternal.preferenceRepositoryInternal = preferenceRepository
    }

    fun useDailyTableRepository(dailyTableRepository: DailyTableRepository) {
        this.repositoryCollectionInternal.dailyTableRepositoryInternal = dailyTableRepository
    }

    fun useDailyRepository(dailyRepository:  DailyRepository) {
        this.repositoryCollectionInternal.dailySetRepositoryInternal = dailyRepository
    }

    fun useRuntimeDataSource(runtimeDataSource: RuntimeDataSource) {
        this.dataSourceCollectionInternal.runtimeDataSourceInternal = runtimeDataSource
    }

    override fun useLifecycle(lifecycle: Lifecycle) {
        this.lifecycleInternal = lifecycle
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