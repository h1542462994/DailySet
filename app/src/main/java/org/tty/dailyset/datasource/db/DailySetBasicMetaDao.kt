package org.tty.dailyset.datasource.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import org.tty.dailyset.bean.entity.DailySetBasicMeta

@Dao
interface DailySetBasicMetaDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun update(dailySetBasicMeta: DailySetBasicMeta)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun updateBatch(dailySetBasicMetas: List<DailySetBasicMeta>)
}