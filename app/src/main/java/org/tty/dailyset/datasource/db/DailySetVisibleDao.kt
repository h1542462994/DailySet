package org.tty.dailyset.datasource.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import org.tty.dailyset.bean.entity.DailySetVisible

@Dao
interface DailySetVisibleDao {
    @Query("select * from dailyset_visible where user_uid = :userUid")
    suspend fun allByUserUid(userUid: String): List<DailySetVisible>

    @Query("select * from dailyset_visible where user_uid = :userUid")
    fun allByUserUidFlow(userUid: String): Flow<List<DailySetVisible>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateBatch(dailySetVisibles: List<DailySetVisible>)

    @Query("select * from dailyset_visible limit 1")
    fun anyFlow(): Flow<DailySetVisible?>
}