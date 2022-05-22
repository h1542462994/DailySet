package org.tty.dailyset.datasource.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import org.tty.dailyset.bean.entity.DailySetStudentInfoMeta
import org.tty.dailyset.datasource.UpdatableResourceDao

@Dao
interface DailySetStudentInfoMetaDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun update(item: DailySetStudentInfoMeta)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateBatch(items: List<DailySetStudentInfoMeta>)
}