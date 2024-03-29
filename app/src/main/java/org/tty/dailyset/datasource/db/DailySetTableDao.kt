package org.tty.dailyset.datasource.db

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import org.tty.dailyset.bean.entity.DailySetTable
import org.tty.dailyset.datasource.UpdatableResourceDao

@Dao
interface DailySetTableDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun update(item: DailySetTable)

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
    suspend fun updateBatch(items: List<DailySetTable>)

    @Query("select * from dailyset_table")
    fun anyFlow(): Flow<DailySetTable?>

    @Query("select * from dailyset_table where source_uid in (:sourceUids)")
    suspend fun allBySourceUids(sourceUids: List<String>): List<DailySetTable>
}