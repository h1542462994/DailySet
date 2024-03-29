package org.tty.dailyset.database

import org.tty.dailyset.bean.entity.*
import org.tty.dailyset.bean.enums.PreferenceName

/**
 * the seeder of the [database]
 * @param database the database
 */
class DailySetDatabaseSeeder(private val database: DailySetRoomDatabase) {
    /**
     * it will be called by [database], to seed the data.
     * @param oldVersion version before seed
     */
    suspend fun seed(oldVersion: Int) {

    }

    suspend fun up(newVersion: Int){
        database.preferenceDao().insert(Preference(PreferenceName.SEED_VERSION.key, false, newVersion.toString()))
    }

    /**
     * return the currentVersion, it's stored in [org.tty.dailyset.bean.entity.Preference] key: seed_version
     */
    fun currentVersion(): Int {
        return 4
    }

}