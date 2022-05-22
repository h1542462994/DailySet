package org.tty.dailyset.datasource.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import org.tty.dailyset.bean.entity.DailySetBasicMeta
import org.tty.dailyset.datasource.UpdatableResourceDao

@Dao
interface DailySetBasicMetaDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun update(item: DailySetBasicMeta)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateBatch(items: List<DailySetBasicMeta>)

    @Query("select * from dailyset_basic_meta limit 1")
    fun anyFlow(): Flow<DailySetBasicMeta?>

    @Query("select * from dailyset_basic_meta where meta_uid = :metaUid limit 1")
    suspend fun anyByMetaUid(metaUid: String): DailySetBasicMeta?
}