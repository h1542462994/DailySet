package org.tty.dailyset.data

import android.util.Log
import org.tty.dailyset.model.entity.Preference
import org.tty.dailyset.model.entity.PreferenceName

class DailySetDatabaseSeeder(val database: DailySetRoomDatabase) {
    suspend fun seed(oldVersion: Int) {
        if (oldVersion < 1) {
            // insert the preference except the seed_version
            PreferenceName.values().forEach { pref ->
                if (pref != PreferenceName.SEED_VERSION) {
                    database.preferenceDao().insert(Preference(pref.key, true, pref.defaultValue))
                }
            }
        }
    }

    suspend fun up(newVersion: Int){
        database.preferenceDao().insert(Preference(PreferenceName.SEED_VERSION.key, false, newVersion.toString()))
    }

    fun currentVersion(): Int {
        return 1
    }

}