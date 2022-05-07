package org.tty.dailyset.datasource.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import org.tty.dailyset.bean.entity.DailySetStudentInfoMeta

@Dao
interface DailySetStudentInfoMetaDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun update(dailySetStudentInfoMeta: DailySetStudentInfoMeta)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateBatch(dailySetStudentInfoMetas: List<DailySetStudentInfoMeta>)
}