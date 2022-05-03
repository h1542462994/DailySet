package org.tty.dailyset.database

import android.content.Context
import android.util.Log
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.tty.dailyset.datasource.db.*
import org.tty.dailyset.bean.entity.*
import org.tty.dailyset.bean.enums.PreferenceName


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
    abstract fun dailyDurationDao(): DailyDurationDao
    abstract fun dailySetBindingDao(): DailySetBindingDao

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
            val oldVersionPreference = database.preferenceDao().get(PreferenceName.SEED_VERSION.key)
            val oldVersion = Preference.defaultOrValue(oldVersionPreference, PreferenceName.SEED_VERSION).toInt()
//            Log.d(TAG, "oldVersion: $oldVersionPreference")
            val newVersion = seeder.currentVersion()
            if (newVersion > oldVersion) {
                seeder.seed(oldVersion)
            }
//            Log.d(TAG, "newVersion: $newVersion")
            database.preferenceDao().insert(Preference(PreferenceName.SEED_VERSION.key, false, newVersion.toString()))
//            oldVersionPreference = database.preferenceDao().get(PreferenceName.SEED_VERSION.key)
            Log.d(TAG, "database seed migrate from $oldVersion -> $newVersion")
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: DailySetRoomDatabase? = null

        private const val TAG = "DailySetRoomDatabase"

        const val currentVersion = 10

        fun getDatabase(context: Context, scope: CoroutineScope): DailySetRoomDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    DailySetRoomDatabase::class.java,
                    "dailyset_database.db"
                )
                    .fallbackToDestructiveMigration() // TODO: implementation with auto migration.
                    .addCallback(DailySetDatabaseCallBack(scope))
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}