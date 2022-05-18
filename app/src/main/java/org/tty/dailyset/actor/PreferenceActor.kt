package org.tty.dailyset.actor

import org.tty.dailyset.annotation.Injectable
import org.tty.dailyset.bean.entity.Preference
import org.tty.dailyset.bean.enums.PreferenceName
import org.tty.dailyset.component.common.SharedComponents

/**
 * actor for [Preference],
 * interaction between [org.tty.dailyset.component.common.BaseVM] and [org.tty.dailyset.datasource.DataSourceCollection]
 * @see [ActorCollection]
 */
@Injectable
class PreferenceActor(private val sharedComponents: SharedComponents) {
    private val preferenceDao get() = sharedComponents.database.preferenceDao()


    suspend fun save(preferenceName: PreferenceName, value: Any) {
        preferenceDao.update(Preference(preferenceName.key, false, value.toString()))
    }

    /**
     * 直接从数据库读取值。
     */
    @Suppress("UNCHECKED_CAST")
    suspend fun <T> read(preferenceName: PreferenceName, mapper: (String) -> T = { it as T }): T {
        preferenceDao.get(preferenceName.key)?.let {
            return mapper(it.value)
        } ?: return mapper(preferenceName.defaultValue)
    }
}