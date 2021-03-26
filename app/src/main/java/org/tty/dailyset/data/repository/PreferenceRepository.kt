package org.tty.dailyset.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.tty.dailyset.model.dao.PreferenceDao
import org.tty.dailyset.model.entity.Preference
import org.tty.dailyset.model.entity.PreferenceName

class PreferenceRepository(private val preferenceDao: PreferenceDao) {
    val seedVersionPreference: Flow<Preference?> = preferenceDao.load(PreferenceName.SEED_VERSION.key)
    val seedVersion = seedVersionPreference.map { p ->
        p?.value?.toInt()
    }

    private val currentUserUidPreference: Flow<Preference?> = preferenceDao.load(PreferenceName.CURRENT_USER_UID.key)
    val currentUserUid = currentUserUidPreference.map { p ->
        p?.value
    }
}