package org.tty.dailyset.datasource.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import org.tty.dailyset.bean.entity.DailySetUsageMeta

@Dao
interface DailySetUsageMetaDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun update(dailySetUsageMeta: DailySetUsageMeta)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun updateBatch(dailySetUsageMetas: List<DailySetUsageMeta>)
}