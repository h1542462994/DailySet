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


@Database(
    entities = [
        User::class,
        UserTicketInfo::class,
        Preference::class,
        DailySet::class,
        DailySetMetaLinks::class,
        DailySetSourceLinks::class,
        DailySetTable::class,
        DailySetRow::class,
        DailySetCell::class,
        DailySetCourse::class,
        DailySetDuration::class,
        DailySetBasicMeta::class,
        DailySetUsageMeta::class,
        DailySetSchoolInfoMeta::class,
        DailySetStudentInfoMeta::class,
        DailySetVisible::class
    ],
    version = DailySetRoomDatabase.currentVersion,
    exportSchema = false
)
abstract class DailySetRoomDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun preferenceDao(): PreferenceDao
    abstract fun userTicketInfoDao(): UserTicketInfoDao
    abstract fun dailySetDao(): DailySetDao
    abstract fun dailySetSourceLinksDao(): DailySetSourceLinksDao
    abstract fun dailySetMetaLinksDao(): DailySetMetaLinksDao
    abstract fun dailySetTableDao(): DailySetTableDao
    abstract fun dailySetRowDao(): DailySetRowDao
    abstract fun dailySetCellDao(): DailySetCellDao
    abstract fun dailySetDurationDao(): DailySetDurationDao
    abstract fun dailySetCourseDao(): DailySetCourseDao
    abstract fun dailySetBasicMetaDao(): DailySetBasicMetaDao
    abstract fun dailySetUsageMetaDao(): DailySetUsageMetaDao
    abstract fun dailySetSchoolInfoMetaDao(): DailySetSchoolInfoMetaDao
    abstract fun dailySetStudentInfoMetaDao(): DailySetStudentInfoMetaDao
    abstract fun dailySetVisibleDao(): DailySetVisibleDao


    private class DailySetDatabaseCallBack(
        private val scope: CoroutineScope
    ) : RoomDatabase.Callback() {
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
            val oldVersion =
                Preference.defaultOrValue(oldVersionPreference, PreferenceName.SEED_VERSION).toInt()
//            Log.d(TAG, "oldVersion: $oldVersionPreference")
            val newVersion = seeder.currentVersion()
            if (newVersion > oldVersion) {
                seeder.seed(oldVersion)
            }
//            Log.d(TAG, "newVersion: $newVersion")
            database.preferenceDao()
                .insert(Preference(PreferenceName.SEED_VERSION.key, false, newVersion.toString()))
//            oldVersionPreference = database.preferenceDao().get(PreferenceName.SEED_VERSION.key)
            Log.d(TAG, "database seed migrate from $oldVersion -> $newVersion")
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: DailySetRoomDatabase? = null

        private const val TAG = "DailySetRoomDatabase"

        const val currentVersion = 15

        fun getDatabase(context: Context, scope: CoroutineScope): DailySetRoomDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    DailySetRoomDatabase::class.java,
                    "dailyset_database.db"
                ).fallbackToDestructiveMigration() // TODO: implementation with auto migration.
                    .addCallback(DailySetDatabaseCallBack(scope)).build()
                INSTANCE = instance
                instance
            }
        }
    }
}