package org.tty.dailyset.model.lifetime.dailyset

import org.tty.dailyset.model.entity.DailySetDurations
import org.tty.dailyset.model.entity.weekCountAtIndex
import org.tty.dailyset.weekCount
import java.time.LocalDate

data class ClazzDailySetCursors(
    val dailySetDurations: DailySetDurations,
    val index: Int
): List<ClazzDailySetCursor> by generateList(dailySetDurations) {

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
                        return totalIndex + weekCount(date, start).toInt() - 1
                    }
                    totalIndex += weekCount(end, start).toInt()
                }
                return totalIndex
            }
        }
    }
    val cursor: ClazzDailySetCursor get() = if(index in indices) {
        this[index]
    } else {
        ClazzDailySetCursor.empty()
    }

    override fun toString(): String {
        return "ClazzDailySetCursors(dailySet=${dailySetDurations.dailySet.name},page=[${index}/${this.size - 1}])"
    }

    companion object {
        fun empty(): ClazzDailySetCursors {
            return ClazzDailySetCursors(
                dailySetDurations = DailySetDurations.empty(),
                0
            )
        }

        /**
         * delegate list.
         */
        private fun generateList(dailySetDurations: DailySetDurations): List<ClazzDailySetCursor> {
            return dailySetDurations.durations.flatMapIndexed { index, dailyDuration ->
                val weekCount = dailySetDurations.durations.weekCountAtIndex(index)
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
