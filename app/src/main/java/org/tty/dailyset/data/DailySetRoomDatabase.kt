package org.tty.dailyset.data

import android.content.Context
import android.util.Log
import androidx.lifecycle.asLiveData
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.tty.dailyset.model.dao.*
import org.tty.dailyset.model.entity.*


@Database(entities =
    [Preference::class, User::class, DailyTable::class, DailyRow::class, DailyCell::class, DailySet::class, DailyDuration::class, DailyNode::class, DailySetBinding::class],
    version = DailySetRoomDatabase.currentVersion, exportSchema = false)
abstract class DailySetRoomDatabase: RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun preferenceDao(): PreferenceDao
    abstract fun dailyTableDao(): DailyTableDao
    abstract fun dailyRowDao(): DailyRowDao
    abstract fun dailyCellDao(): DailyCellDao
    abstract fun dailySetDao(): DailySetDao

    private class DailySetDatabaseCallBack(
        private val scope: CoroutineScope
    ): RoomDatabase.Callback() {
        override fun onOpen(db: SupportSQLiteDatabase) {
            super.onOpen(db)
            INSTANCE?.let { database ->
                scope.launch {
                    populateDatabase(database)
                    //seed(database)
                }
            }
        }

        @Deprecated("use [populateDatabase]")
        suspend fun seed(database: DailySetRoomDatabase) {
            val databaseSeeder = DailySetDatabaseSeeder(database)
            databaseSeeder.seed(0)
            databaseSeeder.up(databaseSeeder.currentVersion())
        }

        suspend fun populateDatabase(database: DailySetRoomDatabase) {
            val seeder = DailySetDatabaseSeeder(database)
            // FIXED BUG: couldn't collect the data when onOpen(db)
            var oldVersionPreference = database.preferenceDao().get(PreferenceName.SEED_VERSION.key)
            val oldVersion = Preference.defaultOrValue(oldVersionPreference, PreferenceName.SEED_VERSION).toInt()
            Log.d(TAG, "oldVersion: $oldVersionPreference")
            val newVersion = seeder.currentVersion()
            if (newVersion > oldVersion) {
                seeder.seed(oldVersion)
            }
            Log.d(TAG, "newVersion: $newVersion")
            database.preferenceDao().insert(Preference(PreferenceName.SEED_VERSION.key, false, newVersion.toString()))
            oldVersionPreference = database.preferenceDao().get(PreferenceName.SEED_VERSION.key)
            Log.d(TAG, "migratedVersion: $oldVersionPreference")
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: DailySetRoomDatabase? = null

        private const val TAG = "DailySetRoomDatabase"

        const val currentVersion = 7

        fun getDatabase(context: Context, scope: CoroutineScope): DailySetRoomDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    DailySetRoomDatabase::class.java,
                    "dailyset_database.db"
                )
                    .fallbackToDestructiveMigration() // warning: open destructive migration
                    .addCallback(DailySetDatabaseCallBack(scope))
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}