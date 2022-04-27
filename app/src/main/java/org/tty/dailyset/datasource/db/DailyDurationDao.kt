package org.tty.dailyset.datasource.db

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import org.tty.dailyset.converter.DailyDurationTypeConverter
import org.tty.dailyset.bean.entity.DailyDuration
import org.tty.dailyset.bean.entity.DailyDurationType

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