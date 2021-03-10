package org.tty.dailyset.data

import android.content.Context
import androidx.lifecycle.asLiveData
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
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
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            INSTANCE?.let { database ->
                scope.launch {
                    populateDatabase(database)
                }
            }
        }

        suspend fun populateDatabase(database: DailySetRoomDatabase) {
            val seeder = DailySetDatabaseSeeder(database)
            val oldVersion = database.preferenceDao().get(PreferenceName.SEED_VERSION.key).asLiveData()
            val newVersion = seeder.seed(Preference.defaultOrValue(oldVersion.value, PreferenceName.SEED_VERSION.key).toInt())

            if (oldVersion.value == null) {
                database.preferenceDao().insert(Preference(PreferenceName.SEED_VERSION.key, false, newVersion.toString()))
            } else {
                val instance = oldVersion.value!!
                instance.useDefault = false
                instance.value = newVersion.toString()
                database.preferenceDao().update(instance)
            }
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: DailySetRoomDatabase? = null

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