package org.tty.dailyset.component.common

import android.content.Context
import android.view.Window
import androidx.lifecycle.Lifecycle
import kotlinx.coroutines.CoroutineScope
import org.tty.dailyset.MainActions
import org.tty.dailyset.Nav
import org.tty.dailyset.database.DailySetRoomDatabase
import org.tty.dailyset.datasource.DataSourceCollection
import org.tty.dailyset.repository.RepositoryCollection

interface SharedComponents {
    @Deprecated("use activityScope instead.")
    val lifecycle: Lifecycle
    val activityScope: CoroutineScope
    val applicationScope: CoroutineScope
    val database: DailySetRoomDatabase
    val dataSourceCollection: DataSourceCollection
    val repositoryCollection: RepositoryCollection
    val stateStore: StateStore
    val nav: Nav<MainActions>
    val window: Window
    val activityContext: Context
    val ltsVMSaver: LtsVMSaver
    fun useNav(nav: Nav<MainActions>)
    @Deprecated("use activityScope instead.")
    fun useLifecycle(lifecycle: Lifecycle)
    fun useWindow(window: Window)
    fun useActivityContext(context: Context)
    fun useActivityScope(scope: CoroutineScope)
}