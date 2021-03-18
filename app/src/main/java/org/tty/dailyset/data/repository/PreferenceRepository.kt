package org.tty.dailyset.data.repository

import androidx.lifecycle.asLiveData
import androidx.lifecycle.liveData
import kotlinx.coroutines.flow.*
import org.tty.dailyset.model.dao.PreferenceDao
import org.tty.dailyset.model.entity.Preference
import org.tty.dailyset.model.entity.PreferenceName

class PreferenceRepository(private val preferenceDao: PreferenceDao) {
    val seedVersionPreference: Flow<Preference?> = preferenceDao.load(PreferenceName.SEED_VERSION.key)
    val seedVersion = seedVersionPreference.map { p ->
        p?.value?.toInt() ?: Preference.default(PreferenceName.SEED_VERSION).value.toInt()
    }

    private val currentUserUidPreference: Flow<Preference?> = preferenceDao.load(PreferenceName.CURRENT_USER_UID.key)
    val currentUserUid = currentUserUidPreference.map { p ->
        p?.value ?: Preference.default(PreferenceName.CURRENT_USER_UID).value
    }
}