package org.tty.dailyset.data

import android.content.Context
import android.util.Log
import androidx.lifecycle.asLiveData
import androidx.lifecycle.liveData
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.tty.dailyset.model.dao.PreferenceDao
import org.tty.dailyset.model.dao.UserDao
import org.tty.dailyset.model.entity.Preference
import org.tty.dailyset.model.entity.PreferenceName
import org.tty.dailyset.model.entity.User


@Database(entities = [Preference::class, User::class], version = 1, exportSchema = false)
abstract class DailySetRoomDatabase: RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun preferenceDao(): PreferenceDao

    private class DailySetDatabaseCallBack(
        private val scope: CoroutineScope
    ): RoomDatabase.Callback() {
        override fun onOpen(db: SupportSQLiteDatabase) {
            super.onOpen(db)
            INSTANCE?.let { database ->
                scope.launch {
                    //populateDatabase(database)
                    seed(database)
                }
            }
        }

        suspend fun seed(database: DailySetRoomDatabase) {
            val databaseSeeder = DailySetDatabaseSeeder(database)
            databaseSeeder.seed(0)
            databaseSeeder.up(databaseSeeder.currentVersion())
        }

        @Deprecated("couldn't collect the data")
        suspend fun populateDatabase(database: DailySetRoomDatabase) {
            val seeder = DailySetDatabaseSeeder(database)
            // TODO BUG: couldn't collect the data when onOpen(db)
            var oldVersion = database.preferenceDao().get(PreferenceName.SEED_VERSION.key).asLiveData(context = scope.coroutineContext).value

            Log.d(TAG, "oldVersion: $oldVersion")
            seeder.seed(Preference.defaultOrValue(oldVersion, PreferenceName.SEED_VERSION).toInt())
            val newVersion = seeder.currentVersion()
            Log.d(TAG, "newVersion: $newVersion")
            if (oldVersion == null) {
                val pref = Preference(PreferenceName.SEED_VERSION.key, false, newVersion.toString())
                database.preferenceDao().insert(pref)
                oldVersion = pref
            } else {
                val instance = oldVersion
                instance.useDefault = false
                instance.value = newVersion.toString()
                database.preferenceDao().update(instance)
            }
            Log.d(TAG, "migratedVersion: $oldVersion")

        }
    }

    companion object {
        @Volatile
        private var INSTANCE: DailySetRoomDatabase? = null

        private val TAG = "DailySetRoomDatabase"

        fun getDatabase(context: Context, scope: CoroutineScope): DailySetRoomDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    DailySetRoomDatabase::class.java,
                    "dailyset_database.db"
                )
                    .addCallback(DailySetDatabaseCallBack(scope))
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}