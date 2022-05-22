package org.tty.dailyset.datasource.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import org.tty.dailyset.bean.entity.DailySetCell
import org.tty.dailyset.datasource.UpdatableResourceDao

@Dao
interface DailySetCellDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun update(item: DailySetCell)

    @Delete
    suspend fun delete(dailySetCell: DailySetCell)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateBatch(items: List<DailySetCell>)

    @Query("select * from dailyset_cell limit 1")
    fun anyFlow(): Flow<DailySetCell?>

    @Query("select * from dailyset_cell where source_uid in (:sourceUids)")
    suspend fun allBySourceUids(sourceUids: List<String>): List<DailySetCell>
}