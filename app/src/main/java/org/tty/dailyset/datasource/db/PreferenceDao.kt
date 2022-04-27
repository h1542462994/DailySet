package org.tty.dailyset.datasource.db

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import org.tty.dailyset.bean.entity.Preference


/**
 * DataAccess to the [org.tty.dailyset.bean.entity.Preference]
 */
@Dao
interface PreferenceDao {
    @Query("SELECT * FROM preference WHERE preferenceKey = :name LIMIT 1")
    fun get(name: String): Preference?

    @Query("SELECT * FROM preference WHERE preferenceKey = :name LIMIT 1")
    fun load(name: String): Flow<Preference?>

    @Query("SELECT * FROM preference WHERE preferenceKey = :name LIMIT 1")
    @Deprecated("will produce NullPointerException")
    fun assertLoad(name: String): Flow<Preference>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(preference: Preference)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun update(preference: Preference)
}