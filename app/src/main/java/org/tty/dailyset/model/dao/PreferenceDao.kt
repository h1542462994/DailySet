package org.tty.dailyset.model.dao

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import org.tty.dailyset.model.entity.Preference


/**
 * DataAccess to the [org.tty.dailyset.model.entity.Preference]
 */
@Dao
interface PreferenceDao {
    @Query("SELECT * FROM preference WHERE name = :name LIMIT 1")
    fun get(name: String): Flow<Preference?>

    @Query("SELECT * FROM preference WHERE name = :name LIMIT 1")
    fun assertGet(name: String): Flow<Preference>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(preference: Preference)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun update(preference: Preference)
}