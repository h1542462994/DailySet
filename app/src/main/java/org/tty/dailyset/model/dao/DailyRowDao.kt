package org.tty.dailyset.model.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import org.tty.dailyset.model.entity.DailyRow

@Dao
interface DailyRowDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun update(dailyRow: DailyRow)
}