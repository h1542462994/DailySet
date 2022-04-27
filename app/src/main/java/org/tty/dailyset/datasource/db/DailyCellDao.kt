package org.tty.dailyset.datasource.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import org.tty.dailyset.bean.entity.DailyCell

@Dao
interface DailyCellDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun update(dailyCell: DailyCell)
    @Delete
    suspend fun delete(dailyCell: DailyCell)
}