package org.tty.dailyset.model.dao

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import org.tty.dailyset.model.entity.DailyTRC
import org.tty.dailyset.model.entity.DailyTable

@Dao
interface DailyTableDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun update(dailyTable: DailyTable)

    @Transaction
    @Query("SELECT * FROM daily_table WHERE uid = :uid LIMIT 1")
    fun get(uid: String): DailyTRC?

    @Transaction
    @Query("SELECT * FROM daily_table WHERE uid = :uid LIMIT 1")
    fun load(uid: String): Flow<DailyTRC?>

    @Query("SELECT * FROM daily_table")
    fun all(): Flow<List<DailyTable>>

    @Delete
    fun delete(dailyTable: DailyTable)
}