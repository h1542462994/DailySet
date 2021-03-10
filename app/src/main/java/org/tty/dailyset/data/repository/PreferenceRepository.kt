package org.tty.dailyset.data.repository

import kotlinx.coroutines.flow.Flow
import org.tty.dailyset.model.dao.PreferenceDao
import org.tty.dailyset.model.entity.Preference
import org.tty.dailyset.model.entity.PreferenceName

class PreferenceRepository(private val preferenceDao: PreferenceDao) {
    var seedVersion: Flow<Preference?> = preferenceDao.get(PreferenceName.SEED_VERSION.key)
}