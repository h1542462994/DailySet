package org.tty.dailyset.datasource.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import org.tty.dailyset.bean.entity.DailySetUsageMeta
import org.tty.dailyset.datasource.UpdatableResourceDao

@Dao
interface DailySetUsageMetaDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun update(item: DailySetUsageMeta)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateBatch(items: List<DailySetUsageMeta>)
}