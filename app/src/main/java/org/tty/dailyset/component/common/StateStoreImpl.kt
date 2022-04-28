package org.tty.dailyset.component.common

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.tty.dailyset.bean.entity.PreferenceName
import org.tty.dioc.util.optional
import java.time.DayOfWeek
import java.time.LocalDateTime

class StateStoreImpl(private val sharedComponents: SharedComponents): StateStore {

    override val seedVersion: Flow<Int> = loadPreference(PreferenceName.SEED_VERSION, mapper = { it.toInt() })
    override val currentUserUid: Flow<String> = loadPreference(PreferenceName.CURRENT_USER_UID)
    override val now: Flow<LocalDateTime> = sharedComponents.dataSourceCollection.runtimeDataSource.now
    override val nowDayOfWeek: Flow<DayOfWeek> = sharedComponents.dataSourceCollection.runtimeDataSource.nowDayOfWeek
    override val startDayOfWeek: Flow<DayOfWeek> = loadPreference(PreferenceName.START_DAY_OF_WEEK, mapper = { DayOfWeek.of(it.toInt()) })

    @Suppress("UNCHECKED_CAST")
    private fun <T> loadPreference(preferenceName: PreferenceName, mapper: (it: String) -> T = { it as T }): Flow<T> {
        return sharedComponents.dataSourceCollection.dbSourceCollection.preferenceDao.load(preferenceName.key).map {
            it.optional { mapper(value) } ?: mapper(preferenceName.defaultValue)
        }
    }
}