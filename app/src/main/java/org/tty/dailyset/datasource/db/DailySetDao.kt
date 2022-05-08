package org.tty.dailyset.datasource.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import org.tty.dailyset.bean.entity.DailySet

/**
 * the dailySet dao object.
 */
@Dao
interface DailySetDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun update(dailySet: DailySet)
    @Delete
    suspend fun delete(dailySet: DailySet)

    @Query("select * from dailyset")
    fun allFlow(): Flow<List<DailySet>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateBatch(dailySets: List<DailySet>)

    @Query("select * from dailyset where uid = :uid limit 1")
    suspend fun get(uid: String): DailySet?

    @Query("select * from dailyset")
    suspend fun all(): List<DailySet>

    @Query("select * from dailyset limit 1")
    fun anyFlow(): Flow<DailySet?>
}