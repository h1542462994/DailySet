package org.tty.dailyset.datasource.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import org.tty.dailyset.bean.entity.DailySetCell

@Dao
interface DailySetCellDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun update(dailySetCell: DailySetCell)
    @Delete
    suspend fun delete(dailySetCell: DailySetCell)
}