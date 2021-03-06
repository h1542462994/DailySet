package org.tty.dailyset.model.dao

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import org.tty.dailyset.model.entity.DailySetBinding

@Dao
interface DailySetBindingDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun update(dailySetBinding: DailySetBinding)

    @Delete
    fun delete(dailySetBinding: DailySetBinding)

    @Query("select * from daily_set_binding where dailySetUid = :dailySetUid and dailyDurationUid = :dailyDurationUid")
    fun loadDailySetBinding(dailySetUid: String, dailyDurationUid: String): Flow<DailySetBinding?>
}