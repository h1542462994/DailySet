package org.tty.dailyset.datasource.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import org.tty.dailyset.bean.entity.DailySetCourse

@Dao
interface DailySetCourseDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun update(dailySetCourse: DailySetCourse)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateBatch(dailySetCourses: List<DailySetCourse>)
}