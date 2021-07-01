package org.tty.dailyset.model.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import org.tty.dailyset.model.entity.DailySetBinding

@Dao
interface DailySetBindingDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun update(dailySetBinding: DailySetBinding)

    @Delete
    fun delete(dailySetBinding: DailySetBinding)
}