package org.tty.dailyset.model.lifetime.dailyset

import org.tty.dailyset.model.entity.DailySetDurations
import org.tty.dailyset.model.entity.weekCountAtIndex
import org.tty.dailyset.weekCount
import java.time.LocalDate

class ClazzDailySetCursors(
    val dailySetDurations: DailySetDurations,
) {
    var list: List<ClazzDailySetCursor> = iterator {
        val durations = dailySetDurations.durations
        for (index in durations.indices) {
            for (weekIndex in 0 until durations.weekCountAtIndex(index)) {
                yield(
                    ClazzDailySetCursor(
                        dailyDurationUid = durations[index].uid,
                        index = weekIndex
                    )
                )
            }
        }
    }.asSequence().toList()
    internal set

    fun findIndex(date: LocalDate): Int {
        val durations = dailySetDurations.durations.sortedBy { it.startDate }
        when {
            date.isBefore(durations.first().startDate) -> {
                return 0
            }
            date.isAfter(durations.last().endDate) -> {
                return list.size - 1
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
            }
        }
        return 0
    }

    companion object {
        fun empty(): ClazzDailySetCursors {
            return ClazzDailySetCursors(
                dailySetDurations = DailySetDurations.empty()
            )
        }
    }
}
