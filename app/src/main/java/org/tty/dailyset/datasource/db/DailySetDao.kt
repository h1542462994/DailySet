package org.tty.dailyset.datasource.db

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import org.tty.dailyset.bean.entity.DailySet
import org.tty.dailyset.bean.entity.DailySetDurations
import org.tty.dailyset.bean.entity.DailySetType
import org.tty.dailyset.converter.DailyDurationTypeConverter
import org.tty.dailyset.converter.DailySetTypeConverter

/**
 * the dailySet dao object.
 */
@Dao
@TypeConverters(DailySetTypeConverter::class, DailyDurationTypeConverter::class)
interface DailySetDao: DailyDurationDao, DailySetBindingDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun update(dailySet: DailySet)
    @Delete
    suspend fun delete(dailySet: DailySet)

    /**
     * insert query.
     */
    @Query("select count(*) from daily_set where type = :type")
    fun countOfType(type: DailySetType): Int

    @Query("select * from daily_set")
    fun allSets(): Flow<List<DailySet>>

    @Query("select * from daily_set where uid = :dailySetUid")
    fun load(dailySetUid: String): Flow<DailySet?>

    @Query("select * from daily_set where uid = :dailySetUid")
    @Transaction
    fun loadDetail(dailySetUid: String): Flow<DailySetDurations?>
}