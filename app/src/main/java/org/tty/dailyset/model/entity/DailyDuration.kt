package org.tty.dailyset.model.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import org.tty.dailyset.model.converter.DailyDurationTagConverter
import org.tty.dailyset.model.converter.DailyDurationTypeConverter
import org.tty.dailyset.model.converter.LongTimeStampConverter
import org.tty.dailyset.model.converter.StringLocalDateConverter
import java.sql.Timestamp
import java.time.Instant
import java.time.LocalDate

/**
 * represents a long daily duration
 * the daily duration belong to a user
 */
@Entity
@TypeConverters(DailyDurationTypeConverter::class, StringLocalDateConverter::class, DailyDurationTagConverter::class, LongTimeStampConverter::class)
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
    val startDate: LocalDate = LocalDate.from(Instant.EPOCH),

    /**
     * the endDate
     */
    val endDate: LocalDate = LocalDate.from(Instant.EPOCH),
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
     * the property only used on [DailyDurationType.Clazz]
     * the binding DailyTable
     */
    val bindingDailyTableUid: String = "",

    /**
     * the property only used on [DailyDurationType.Clazz]
     * the binging PeriodCode
     * usually 0 means 上学期, 7 means 下学期, 13 means 短学期
     */
    val bindingPeriodCode: Int = 0,

    val serialIndex: Int = 0,

    /**
     * the update timestamp
     */
    val updateAt: Timestamp
)