package org.tty.dailyset.datasource.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import org.tty.dailyset.bean.entity.DailySetSchoolInfoMeta
import org.tty.dailyset.datasource.UpdatableResourceDao

@Dao
interface DailySetSchoolInfoMetaDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun update(item: DailySetSchoolInfoMeta)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateBatch(items: List<DailySetSchoolInfoMeta>)
}