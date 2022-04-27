package org.tty.dailyset.bean.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import org.tty.dailyset.common.datetime.epochLocalDateTime
import org.tty.dailyset.converter.DailyDurationTagConverter
import org.tty.dailyset.converter.DailyDurationTypeConverter
import org.tty.dailyset.converter.StringLocalDateConverter
import org.tty.dailyset.converter.StringLocalDateTimeConverter
import java.time.LocalDate
import java.time.LocalDateTime

/**
 * represents a long daily duration
 * the daily duration belong to a user
 */
@Entity(tableName = "daily_duration")
@TypeConverters(DailyDurationTypeConverter::class, StringLocalDateConverter::class, DailyDurationTagConverter::class, StringLocalDateTimeConverter::class)
data class DailyDuration(
    /**
     * the daily duration type.
     */
    val type: DailyDurationType = DailyDurationType.Normal,
    /**
     * the unique uid.
     */
    @PrimaryKey
    val uid: String = "",
    /**
     * the owner uid
     */
    val ownerUid: String = "",

    /**
     * the startDate
     */
    val startDate: LocalDate = LocalDate.ofEpochDay(0),

    /**
     * the endDate
     */
    val endDate: LocalDate = LocalDate.ofEpochDay(0),
    /**
     * the displayName
     */
    val name: String = "",

    /**
     * the property only used on [DailyDurationType.Normal]
     * the tag of the dailyDuration
     */
    val tag: DailyDurationTag = DailyDurationTag.Normal,

    /**
     * the serialIndex
     */
    val serialIndex: Int = 0,

    /**
     * the property only used on [DailyDurationType.Clazz]
     * usually 0/.. means unspecified, 1 means 上学期, 7 means 下学期, 13 means 短学期
     * 2 means 上学期期末 8 means 下学期期末
     * 4 means 寒假 14 means 暑假
     */
    val bindingPeriodCode: Int = 0,

    /**
     * the update timestamp
     */
    val updateAt: LocalDateTime = epochLocalDateTime()
) {
    companion object {
        fun empty(): DailyDuration {
            return DailyDuration(updateAt = epochLocalDateTime())
        }
    }
}