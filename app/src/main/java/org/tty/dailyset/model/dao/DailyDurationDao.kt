package org.tty.dailyset.model.dao

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import org.tty.dailyset.model.converter.DailyDurationTypeConverter
import org.tty.dailyset.model.entity.DailyDuration
import org.tty.dailyset.model.entity.DailyDurationType

@Dao
@TypeConverters(DailyDurationTypeConverter::class)
interface DailyDurationDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun update(dailyDuration: DailyDuration)
    @Delete
    fun delete(dailyDuration: DailyDuration)

    @Query("select * from daily_duration")
    fun allDurations(): Flow<List<DailyDuration>>

    @Query("select * from daily_duration where type = :type")
    fun typedDurations(type: DailyDurationType): Flow<List<DailyDuration>>

    @Query("select count(*) from daily_duration where type = :type")
    fun countOfType(type: DailyDurationType): Int
}