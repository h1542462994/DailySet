package org.tty.dailyset.datasource.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import org.tty.dailyset.bean.entity.DailyRow

@Dao
interface DailyRowDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun update(dailyRow: DailyRow)

    @Delete
    suspend fun delete(dailyRow: DailyRow)
}