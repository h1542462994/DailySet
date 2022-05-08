@file:Suppress("MemberVisibilityCanBePrivate")

package org.tty.dailyset.common.datetime

import java.time.DayOfWeek
import java.time.Duration
import java.time.LocalDate

/**
 * describe a date span from [startDate] to [endDateInclusive]
 */
data class DateSpan(
    /**
     * the startDate of the span.
     */
    val startDate: LocalDate,
    /**
     * the endDateInclusive of the span.
     */
    val endDateInclusive: LocalDate,
    /**
     * the startWeekDay
     */
    val startDayOfWeek: DayOfWeek = defaultStartDayOfWeek
) {

    /**
     * to [Duration]
     */
    fun toDuration(): Duration {
        return Duration.between(startDate.atStartOfDay(), endDateExclusive.atStartOfDay())
    }

    /**
     * expand the [DateSpan] to the full week.
     */
    fun expandToFullWeek(): DateSpan {
        return DateSpan(
            startDate = startDate.toWeekStart(startDayOfWeek),
            endDateInclusive = endDateInclusive.toWeekEnd(startDayOfWeek),
            startDayOfWeek = startDayOfWeek
        )
    }

    operator fun contains(date: LocalDate): Boolean {
        return date.isAfter(startDate.minusDays(1)) && date.isBefore(endDateExclusive)
    }

    override fun toString(): String {
        return "$startDate - $endDateInclusive ${startDayOfWeek.name} (${totalDays} days, $weekCount weeks, $weekCountFull weeks of full)"
    }

    val endDateExclusive: LocalDate = endDateInclusive.plusDays(1)

    val totalDays: Int get() {
        return toDuration().toDays().toInt()
    }

    /**
     * the weekCount of the period, 7 days as a week. not enough calculate as a week.
     */
    val weekCount: Int get() {
        return (totalDays - 1) / 7 + 1
    }

    /**
     * the weekCount of the period
      */
    val weekCountFull: Int get() {
        return this.expandToFullWeek().weekCount
    }

    companion object {
        /**
         * date span of ([startDate],[endDateExclusive]]
         */
        fun ofInclusive(startDate: LocalDate, endDateInclusive: LocalDate, startDayOfWeek: DayOfWeek): DateSpan {
            return DateSpan(startDate, endDateInclusive, startDayOfWeek)
        }

        /**
         * date span of ([startDate],[endDateExclusive])
         */
        fun ofExclusive(startDate: LocalDate, endDateExclusive: LocalDate, startDayOfWeek: DayOfWeek): DateSpan {
            return DateSpan(startDate, endDateExclusive.minusDays(1), startDayOfWeek)
        }

        /**
         * the date week start with [startDayOfWeek] and contains [date].
         */
        fun ofDate(date: LocalDate, startDayOfWeek: DayOfWeek): DateSpan {
            val startDate = date.toWeekStart(startDayOfWeek)
            val endDateInclusive = date.toWeekEnd(startDayOfWeek)
            return DateSpan(
                startDate, endDateInclusive, startDayOfWeek
            )
        }
    }
}