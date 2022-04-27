package org.tty.dailyset.bean.lifetime.dailyset

import org.tty.dailyset.common.datetime.DateSpan
import org.tty.dailyset.bean.entity.DailySetDurations
import org.tty.dailyset.bean.entity.weekCountAtIndex
import java.time.DayOfWeek
import java.time.LocalDate

data class ClazzDailySetCursors(
    /**
     * the reference data source.
     */
    val dailySetDurations: DailySetDurations,
    val startWeekDay: DayOfWeek
): List<ClazzDailySetCursor> by generateList(dailySetDurations, startWeekDay) {

    fun findIndex(date: LocalDate): Int {
        val durations = dailySetDurations.durations.sortedBy { it.startDate }
        if (durations.isEmpty()) {
            return 0
        }
        when {
            date.isBefore(durations.first().startDate) -> {
                return 0
            }
            date.isAfter(durations.last().endDate) -> {
                return this.count() - 1
            }
            else -> {
                var totalIndex = 0
                for (index in durations.indices) {
                    val duration = durations[index]
                    val end = if (index < durations.size - 1) {
                        durations[index + 1].startDate
                    } else {
                        durations.last().endDate.plusDays(1)
                    }
                    val start = duration.startDate
                    if (date < end) {
                        return totalIndex + DateSpan(date, start, startWeekDay).weekCountFull
                    }
                    totalIndex += DateSpan(end, start, startWeekDay).weekCountFull
                }
                return totalIndex
            }
        }
    }


    override fun toString(): String {
        return "ClazzDailySetCursors(dailySet=${dailySetDurations.dailySet.name},pageCount=${size})"
    }

    companion object {
        fun empty(): ClazzDailySetCursors {
            return ClazzDailySetCursors(
                dailySetDurations = DailySetDurations.empty(),
                startWeekDay = DayOfWeek.MONDAY
            )
        }

        /**
         * delegate list.
         */
        private fun generateList(dailySetDurations: DailySetDurations, startWeekDay: DayOfWeek): List<ClazzDailySetCursor> {
            return dailySetDurations.durations.flatMapIndexed { index, dailyDuration ->
                val weekCount = dailySetDurations.durations.weekCountAtIndex(index, startWeekDay = startWeekDay)
                (0 until weekCount).map {
                    ClazzDailySetCursor(
                        dailyDuration = dailyDuration,
                        index = it
                    )
                }
            }
        }
    }
}
