package org.tty.dailyset.bean.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable
import org.tty.dailyset.bean.enums.DailySetPeriodCode

/**
 * **source_course(10)**
 */
@Serializable
@Entity(tableName = "dailyset_course")
data class DailySetCourse(
    @PrimaryKey
    @ColumnInfo(name = "source_uid")
    val sourceUid: String,
    val year: Int,
    /**
     * @see DailySetPeriodCode
     */
    @ColumnInfo(name = "period_code")
    val periodCode: Int,
    val name: String,
    val campus: String,
    val location: String,
    val teacher: String,
    val weeks: String,
    @ColumnInfo(name = "week_day")
    val weekDay: Int,
    @ColumnInfo(name = "section_start")
    val sectionStart: Int,
    @ColumnInfo(name = "section_end")
    val sectionEnd: Int
)