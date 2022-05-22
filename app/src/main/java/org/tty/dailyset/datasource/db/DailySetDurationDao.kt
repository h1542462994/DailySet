package org.tty.dailyset.datasource.db

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import org.tty.dailyset.bean.entity.DailySetDuration
import org.tty.dailyset.datasource.UpdatableResourceDao

@Dao
interface DailySetDurationDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun update(item: DailySetDuration)

    @Delete
    suspend fun delete(dailySetDuration: DailySetDuration)

    @Query("select * from dailyset_duration")
    fun allDurations(): Flow<List<DailySetDuration>>

    @Query("select * from dailyset_duration where type = :type")
    fun typedDurations(type: Int): Flow<List<DailySetDuration>>

    @Query("select count(*) from dailyset_duration where type = :type")
    suspend fun countOfType(type: Int): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateBatch(items: List<DailySetDuration>)

    @Query("select * from dailyset_duration where source_uid in (:sourceUids)")
    suspend fun allDurationsBySourceUids(sourceUids: List<String>): List<DailySetDuration>

    @Query("select * from dailyset_duration limit 1")
    fun anyFlow(): Flow<DailySetDuration?>
}