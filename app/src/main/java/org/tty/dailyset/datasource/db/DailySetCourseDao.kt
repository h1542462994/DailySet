package org.tty.dailyset.datasource.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import org.tty.dailyset.bean.entity.DailySetCourse

@Dao
interface DailySetCourseDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun update(dailySetCourse: DailySetCourse)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateBatch(dailySetCourses: List<DailySetCourse>)

    @Query("select * from dailyset_course limit 1")
    fun anyFlow(): Flow<DailySetCourse?>

    @Query("select * from dailyset_course where source_uid in (:sourceUids) and year = :year and period_code = :periodCode")
    suspend fun findAllBySourceUidsAndYearPeriodCode(sourceUids: List<String>, year: Int, periodCode: Int): List<DailySetCourse>

}