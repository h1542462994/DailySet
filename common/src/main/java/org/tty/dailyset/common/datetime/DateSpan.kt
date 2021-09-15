package org.tty.dailyset.common.datetime

import java.time.DayOfWeek
import java.time.LocalDate
import java.time.Period

/**
 * describe a date span from [startDate] to [endDateInclusive]
 */
data class DateSpan(
    val startDate: LocalDate,
    val endDateInclusive: LocalDate,
    val startDayOfWeek: DayOfWeek = DayOfWeek.MONDAY
) {
    /**
     * 将其转化为period
     */
    fun toPeriod(): Period {
        return Period.between(startDate, endDateInclusive.plusDays(1))
    }

    /**
     * 将日期扩展到整周
     */
    fun expandToFullWeek(): DateSpan {
        return DateSpan(
            startDate = startDate.toWeekStart(startDayOfWeek),
            endDateInclusive = endDateInclusive.toWeekStart(startDayOfWeek).plusDays((daysOneWeek - 1).toLong()),
            startDayOfWeek = startDayOfWeek
        )
    }

    operator fun contains(date: LocalDate): Boolean {
        return date.isAfter(startDate.minusDays(1)) && date.isBefore(endDateInclusive.plusDays(1))
    }

    /**
     * 获取这段时间所占用的周数
     */
    val weekCount: Int get() {
        val period = Period.between(
            startDate.toWeekStart(startDayOfWeek), endDateInclusive.toWeekStart(startDayOfWeek).plusDays(1)
        )
        return period.days / 7 + 1
    }

    companion object {
        /**
         * 获取[date]所在的星期的时间段
         */
        fun ofDate(date: LocalDate, startDayOfWeek: DayOfWeek): DateSpan {
            val startDate = date.toWeekStart(startDayOfWeek)
            val endDateInclusive = startDate.plusDays((daysOneWeek - 1).toLong())
            return DateSpan(
                startDate, endDateInclusive, startDayOfWeek
            )
        }
    }
}