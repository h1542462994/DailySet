package org.tty.dailyset.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.tty.dailyset.datasource.db.PreferenceDao
import org.tty.dailyset.bean.entity.Preference
import org.tty.dailyset.bean.entity.PreferenceName
import org.tty.dailyset.component.common.SharedComponents
import org.tty.dioc.util.optional

/**
 * repository for [Preference],
 * it is used in [org.tty.dailyset.DailySetApplication],
 * it will use db service, see also [org.tty.dailyset.database.DailySetRoomDatabase]
 */
class PreferenceRepository(private val sharedComponents: SharedComponents) {
    private val preferenceDao get() = sharedComponents.dataSourceCollection.dbSourceCollection.preferenceDao
    private val seedVersionPreference: Flow<Preference?> = preferenceDao.load(PreferenceName.SEED_VERSION.key)
    val seedVersion = seedVersionPreference.map { s ->
        s.optional { value.toInt() } ?: Preference.default(PreferenceName.SEED_VERSION).value.toInt()
    }

    private val currentUserUidPreference: Flow<Preference?> = preferenceDao.load(PreferenceName.CURRENT_USER_UID.key)
    val currentUserUid = currentUserUidPreference.map { p ->
        p.optional { value } ?: Preference.default(PreferenceName.CURRENT_USER_UID).value
    }
}