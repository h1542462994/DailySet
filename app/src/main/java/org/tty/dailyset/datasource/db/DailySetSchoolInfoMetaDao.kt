package org.tty.dailyset.datasource.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import org.tty.dailyset.bean.entity.DailySetSchoolInfoMeta

@Dao
interface DailySetSchoolInfoMetaDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun update(dailySetSchoolInfoMeta: DailySetSchoolInfoMeta)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun updateBatch(dailySetSchoolInfoMetas: List<DailySetSchoolInfoMeta>)
}