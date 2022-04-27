package org.tty.dailyset.component.common

import kotlinx.coroutines.CoroutineScope
import org.tty.dailyset.database.DailySetRoomDatabase
import org.tty.dailyset.datasource.DataSourceCollection
import org.tty.dailyset.repository.RepositoryCollection

interface SharedComponents {

    val applicationScope: CoroutineScope
    val database: DailySetRoomDatabase
    val dataSourceCollection: DataSourceCollection
    val repositoryCollection: RepositoryCollection
    val stateStore: StateStore
}