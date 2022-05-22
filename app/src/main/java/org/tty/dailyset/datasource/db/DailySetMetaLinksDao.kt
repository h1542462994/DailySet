package org.tty.dailyset.datasource.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import org.tty.dailyset.bean.entity.DailySetMetaLinks

@Dao
interface DailySetMetaLinksDao {
    @Query("select * from dailyset_meta_links where dailyset_uid = :dailySetUid and meta_type = :metaType")
    suspend fun allBySetUidAndMetaType(dailySetUid: String, metaType: Int): List<DailySetMetaLinks>

    @Query("select * from dailyset_meta_links where dailyset_uid = :dailySetUid and meta_type = :metaType and meta_uid not like '%.local' limit 1")
    suspend fun anyBySetUidAndMetaTypeNoLocal(dailySetUid: String, metaType: Int): DailySetMetaLinks?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateBatch(dailySetMetaLinks: List<DailySetMetaLinks>)

    @Delete
    suspend fun removeBatch(dailySetMetaLinks: List<DailySetMetaLinks>)

    @Query("select * from dailyset_meta_links limit 1")
    fun anyFlow(): Flow<DailySetMetaLinks?>
}