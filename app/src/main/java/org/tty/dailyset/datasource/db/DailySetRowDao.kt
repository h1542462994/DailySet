package org.tty.dailyset.datasource.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import org.tty.dailyset.bean.entity.DailySetRow

@Dao
interface DailySetRowDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun update(dailySetRow: DailySetRow)

    @Delete
    suspend fun delete(dailySetRow: DailySetRow)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateBatch(dailySetRows: List<DailySetRow>)

    @Query("select * from dailyset_row limit 1")
    fun anyFlow(): Flow<DailySetRow?>

    @Query("select * from dailyset_row where source_uid in (:sourceUids)")
    suspend fun allBySourceUids(sourceUids: List<String>): List<DailySetRow>
}