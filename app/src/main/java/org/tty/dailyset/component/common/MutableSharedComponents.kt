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

    private lateinit var applicationScopeInternalGet: () -> CoroutineScope
    private val dataSourceCollectionInternal = MutableDataSourceCollection()
    private lateinit var databaseInternalGet: () -> DailySetRoomDatabase
    private val repositoryCollectionInternal = MutableRepositoryCollection()
    private lateinit var stateStoreInternalGet: () -> StateStore
    private lateinit var lifecycleInternalGet: () -> Lifecycle
    private lateinit var navInternalGet: () -> Nav<MainActions>
    private lateinit var windowInternalGet: () -> Window
    private lateinit var activityContextInternalGet: () -> Context
    private lateinit var activityScopeInternalGet: () -> CoroutineScope

    override val dataSourceCollection: DataSourceCollection get() = dataSourceCollectionInternal
    override val database: DailySetRoomDatabase by lazy { databaseInternalGet.invoke() }
    override val repositoryCollection: RepositoryCollection by lazy { repositoryCollectionInternal }
    override val stateStore: StateStore by lazy { stateStoreInternalGet.invoke() }
    override val applicationScope: CoroutineScope by lazy { applicationScopeInternalGet.invoke() }
    override val lifecycle: Lifecycle by lazy { lifecycleInternalGet.invoke() }
    override val nav: Nav<MainActions> by lazy { navInternalGet.invoke() }
    override val window: Window by lazy { windowInternalGet.invoke() }
    override val activityContext: Context by lazy { activityContextInternalGet.invoke() }
    override val activityScope: CoroutineScope by lazy { activityScopeInternalGet.invoke() }

    class MutableDataSourceCollection: DataSourceCollection {
        internal lateinit var dbSourceCollectionInternalGet: () -> DbSourceCollection
        internal lateinit var netSourceCollectionInternalGet: () -> NetSourceCollection
        internal lateinit var runtimeDataSourceInternalGet: () -> RuntimeDataSource

        override val dbSourceCollection: DbSourceCollection by lazy { dbSourceCollectionInternalGet.invoke() }
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

    fun useApplicationScope(func: () -> CoroutineScope) {
        this.applicationScopeInternalGet = func
    }

    fun useDatabase(func: () -> DailySetRoomDatabase) {
        this.databaseInternalGet = func
    }

    fun useDbSourceCollection(func: () -> DbSourceCollection) {
        this.dataSourceCollectionInternal.dbSourceCollectionInternalGet = func
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

    override fun useLifecycle(action: () -> Lifecycle) {
        this.lifecycleInternalGet = action
    }

    fun useStateStore(func: () -> StateStore) {
        this.stateStoreInternalGet = func
    }

    override fun useNav(nav: () -> Nav<MainActions>) {
        this.navInternalGet = nav
    }

    override fun useWindow(window: () -> Window) {
        this.windowInternalGet = window
    }

    override fun useActivityContext(context: () -> Context) {
        this.activityContextInternalGet = context
    }

    override fun useActivityScope(scope: () -> CoroutineScope) {
        this.activityScopeInternalGet = scope
    }

}