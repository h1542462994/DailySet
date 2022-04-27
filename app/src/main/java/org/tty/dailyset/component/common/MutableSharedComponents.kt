package org.tty.dailyset.component.common

import kotlinx.coroutines.CoroutineScope
import org.tty.dailyset.database.DailySetRoomDatabase
import org.tty.dailyset.datasource.DataSourceCollection
import org.tty.dailyset.datasource.DbSourceCollection
import org.tty.dailyset.datasource.NetSourceCollection
import org.tty.dailyset.datasource.runtime.RuntimeDataSource
import org.tty.dailyset.repository.*

class MutableSharedComponents: SharedComponents {

    private lateinit var applicationScopeInternalGet: () -> CoroutineScope
    private val dataSourceCollectionInternal = MutableDataSourceCollection()
    private lateinit var databaseInternalGet: () -> DailySetRoomDatabase
    private val repositoryCollectionInternal = MutableRepositoryCollection()
    private lateinit var stateStoreInternal: StateStore

    override val dataSourceCollection: DataSourceCollection get() = dataSourceCollectionInternal
    override val database: DailySetRoomDatabase by lazy { databaseInternalGet.invoke() }
    override val repositoryCollection: RepositoryCollection by lazy { repositoryCollectionInternal }
    override val stateStore: StateStore by lazy { stateStoreInternal }
    override val applicationScope: CoroutineScope by lazy { applicationScopeInternalGet.invoke() }

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
        internal lateinit var dailySetRepositoryInternalGet: () -> DailySetRepository

        override val userRepository: UserRepository by lazy { userRepositoryInternalGet.invoke() }
        override val preferenceRepository: PreferenceRepository by lazy { preferenceRepositoryInternalGet.invoke() }
        override val dailyTableRepository: DailyTableRepository by lazy { dailyTableRepositoryInternalGet.invoke() }
        override val dailySetRepository: DailySetRepository by lazy { dailySetRepositoryInternalGet.invoke() }
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

    fun useDailySetRepository(func: () -> DailySetRepository) {
        this.repositoryCollectionInternal.dailySetRepositoryInternalGet = func
    }

}