package org.tty.dailyset.datasource.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import org.tty.dailyset.bean.entity.DailySetBasicMeta

@Dao
interface DailySetBasicMetaDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun update(dailySetBasicMeta: DailySetBasicMeta)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun updateBatch(dailySetBasicMetas: List<DailySetBasicMeta>)

    @Query("select * from dailyset_basic_meta limit 1")
    fun anyFlow(): Flow<DailySetBasicMeta?>

    @Query("select * from dailyset_basic_meta where meta_uid = :metaUid limit 1")
    suspend fun anyByMetaUid(metaUid: String): DailySetBasicMeta
}