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

class MutableSharedComponents() : SharedComponents {

    private lateinit var applicationScopeInternal: CoroutineScope
    private val dataSourceCollectionInternal = MutableDataSourceCollection()
    private lateinit var databaseInternal:  DailySetRoomDatabase
    private val repositoryCollectionInternal = MutableRepositoryCollection()
    private lateinit var stateStoreInternalGet: () -> StateStore
    private lateinit var lifecycleInternal: Lifecycle
    private lateinit var navInternal: Nav<MainActions>
    private lateinit var windowInternal: Window
    private lateinit var activityContextInternal:  Context
    private lateinit var activityScopeInternal: CoroutineScope
    private lateinit var ltsVMSaverInternal: LtsVMSaver

    override val dataSourceCollection: DataSourceCollection get() = dataSourceCollectionInternal
    override val database: DailySetRoomDatabase get() = databaseInternal
    override val repositoryCollection: RepositoryCollection by lazy { repositoryCollectionInternal }
    override val stateStore: StateStore by lazy { stateStoreInternalGet.invoke() }
    override val applicationScope: CoroutineScope get() = applicationScopeInternal
    override val lifecycle: Lifecycle get() = lifecycleInternal
    override val nav: Nav<MainActions> get() = navInternal
    override val window: Window get() = windowInternal
    override val activityContext: Context get() = activityContextInternal
    override val activityScope: CoroutineScope get() = activityScopeInternal
    override val ltsVMSaver: LtsVMSaver get() = ltsVMSaverInternal
    class MutableDataSourceCollection: DataSourceCollection {
        internal lateinit var dbSourceCollectionInternal: DbSourceCollection
        internal lateinit var netSourceCollectionInternalGet: () -> NetSourceCollection
        internal lateinit var runtimeDataSourceInternalGet: () -> RuntimeDataSource

        override val dbSourceCollection: DbSourceCollection get() =  dbSourceCollectionInternal
        override val netSourceCollection: NetSourceCollection by lazy { netSourceCollectionInternalGet.invoke() }
        override val runtimeDataSource: RuntimeDataSource by lazy { runtimeDataSourceInternalGet.invoke() }
    }

    class MutableRepositoryCollection: RepositoryCollection {
        internal lateinit var userRepositoryInternalGet: () -> UserRepository
        internal lateinit var preferenceRepositoryInternalGet: () -> PreferenceRepository
        internal lateinit var dailyTableRepositoryInternalGet: () -> DailyTableRepository
        internal lateinit var dailySetRepositoryInternalGet: () -> DailyRepository

        override val userRepository: UserRepository by lazy { userRepositoryInternalGet.invoke() }
        override val preferenceRepository: PreferenceRepository by lazy { preferenceRepositoryInternalGet.invoke() }
        override val dailyTableRepository: DailyTableRepository by lazy { dailyTableRepositoryInternalGet.invoke() }
        override val dailySetRepository: DailyRepository by lazy { dailySetRepositoryInternalGet.invoke() }
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

    fun useNetSourceCollection(func: () -> NetSourceCollection) {
        this.dataSourceCollectionInternal.netSourceCollectionInternalGet = func
    }

    fun useUserRepository(func: () -> UserRepository) {
        this.repositoryCollectionInternal.userRepositoryInternalGet = func
    }

    fun usePreferenceRepository(func: () -> PreferenceRepository) {
        this.repositoryCollectionInternal.preferenceRepositoryInternalGet = func
    }

    fun useDailyTableRepository(func: () -> DailyTableRepository) {
        this.repositoryCollectionInternal.dailyTableRepositoryInternalGet = func
    }

    fun useDailySetRepository(func: () -> DailyRepository) {
        this.repositoryCollectionInternal.dailySetRepositoryInternalGet = func
    }

    fun useRuntimeDataSource(func: () -> RuntimeDataSource) {
        this.dataSourceCollectionInternal.runtimeDataSourceInternalGet = func
    }

    override fun useLifecycle(lifecycle: Lifecycle) {
        this.lifecycleInternal = lifecycle
    }

    fun useStateStore(func: () -> StateStore) {
        this.stateStoreInternalGet = func
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