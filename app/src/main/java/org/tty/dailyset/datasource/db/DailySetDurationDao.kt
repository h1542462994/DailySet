package org.tty.dailyset.datasource.db

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import org.tty.dailyset.bean.entity.DailySetDuration

@Dao
interface DailySetDurationDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun update(dailySetDuration: DailySetDuration)
    @Delete
    suspend fun delete(dailySetDuration: DailySetDuration)

    @Query("select * from dailyset_duration")
    fun allDurations(): Flow<List<DailySetDuration>>

    @Query("select * from dailyset_duration where type = :type")
    fun typedDurations(type: Int): Flow<List<DailySetDuration>>

    @Query("select count(*) from dailyset_duration where type = :type")
    suspend fun countOfType(type: Int): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateBatch(dailySetDurations: List<DailySetDuration>)
}