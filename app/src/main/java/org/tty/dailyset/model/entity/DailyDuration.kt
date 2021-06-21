package org.tty.dailyset.model.entity

import java.sql.Timestamp
import java.time.Instant
import java.time.LocalDate

/**
 * represents a long daily duration
 * the daily duration belong to a user
 */
data class DailyDuration(
    /**
     * the daily duration type.
     */
    val type: DailyDurationType = DailyDurationType.Normal,
    /**
     * the unique uid.
     */
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

    /**
     * the update timestamp
     */
    val updateAt: Timestamp
)