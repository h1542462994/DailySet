package org.tty.dailyset.datasource.db

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import org.tty.dailyset.bean.entity.DailySetTable

@Dao
interface DailySetTableDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun update(dailySetTable: DailySetTable)

    @Query("SELECT * FROM dailyset_table WHERE source_uid = :sourceUid LIMIT 1")
    @Deprecated("use load")
    fun get(sourceUid: String): DailySetTable?

    @Query("SELECT * FROM dailyset_table WHERE source_uid = :sourceUid LIMIT 1")
    fun load(sourceUid: String): Flow<DailySetTable?>

    @Query("SELECT * FROM dailyset_table")
    fun all(): Flow<List<DailySetTable>>

    @Delete
    fun delete(dailySetTable: DailySetTable)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateBatch(dailySetTables: List<DailySetTable>)
}