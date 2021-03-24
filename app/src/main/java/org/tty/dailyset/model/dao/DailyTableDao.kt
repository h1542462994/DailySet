package org.tty.dailyset.model.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import org.tty.dailyset.model.entity.DailyTRC
import org.tty.dailyset.model.entity.DailyTable

@Dao
interface DailyTableDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun update(dailyTable: DailyTable)

    @Query("SELECT * FROM daily_table WHERE uid = :uid LIMIT 1")
    fun get(uid: String): DailyTRC?

    @Query("SELECT * FROM daily_table WHERE uid = :uid LIMIT 1")
    fun load(uid: String): Flow<DailyTRC?>

    @Query("SELECT * FROM daily_table")
    fun all(): Flow<List<DailyTable>>
}