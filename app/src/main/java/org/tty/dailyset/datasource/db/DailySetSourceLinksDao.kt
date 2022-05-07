package org.tty.dailyset.datasource.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import org.tty.dailyset.bean.entity.DailySetSourceLinks

@Dao
interface DailySetSourceLinksDao {
    @Query("select * from dailyset_source_links where dailyset_uid = :dailySetUid and source_type = :sourceType")
    suspend fun allBySetUidAndSourceType(dailySetUid: String, sourceType: Int): List<DailySetSourceLinks>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateBatch(dailySetSourceLinks: List<DailySetSourceLinks>)

    @Delete
    suspend fun removeBatch(dailySetSourceLinks: List<DailySetSourceLinks>)
}