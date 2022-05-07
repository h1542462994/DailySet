package org.tty.dailyset.datasource.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import org.tty.dailyset.bean.entity.DailySetMetaLinks

@Dao
interface DailySetMetaLinksDao {
    @Query("select * from dailyset_meta_links where dailyset_uid = :dailySetUid and meta_type = :metaType")
    suspend fun allBySetUidAndMetaType(dailySetUid: String, metaType: Int): List<DailySetMetaLinks>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateBatch(dailySetMetaLinks: List<DailySetMetaLinks>)

    @Delete
    suspend fun removeBatch(dailySetMetaLinks: List<DailySetMetaLinks>)
}