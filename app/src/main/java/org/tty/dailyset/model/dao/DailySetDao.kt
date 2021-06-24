package org.tty.dailyset.model.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import org.tty.dailyset.data.processor.DailySetProcessor2Async
import org.tty.dailyset.event.DailySetCreateEventArgs
import org.tty.dailyset.model.entity.DailySet

@Dao
interface DailySetDao: DailySetProcessor2Async {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun update(dailySet: DailySet)
    @Delete
    suspend fun delete(dailySet: DailySet)

    override suspend fun create(dailySetCreateEventArgs: DailySetCreateEventArgs) {

    }
}