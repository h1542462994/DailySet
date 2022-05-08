package org.tty.dailyset.bean.lifetime

import org.tty.dailyset.bean.entity.DailySetDuration
import org.tty.dailyset.bean.enums.DailySetPeriodCode
import org.tty.dailyset.common.datetime.*
import org.tty.dailyset.toLongDateString
import java.time.LocalDate
import java.time.LocalDateTime

data class DailySetClazzAutoPageInfo(
    val year: Int,
    val periodCode: DailySetPeriodCode,
    val serialIndex: Int,
    val startDate: LocalDate,
    val endDate: LocalDate
) {
    companion object {
        fun fromDurations(dailySetDurations: List<DailySetDuration>): List<DailySetClazzAutoPageInfo> {
            return buildList {
                val durations = dailySetDurations.sortedBy { it.startDate }
                durations.forEach { duration ->
                    val start = duration.startDate.toWeekStart()
                    val end = duration.endDate.toWeekEnd()
                    val dateSpan = DateSpan(start, end)
                    for (week in 0 until dateSpan.weekCount) {
                        this.add(DailySetClazzAutoPageInfo(
                            year = duration.bindingYear,
                            periodCode = DailySetPeriodCode.of(duration.bindingPeriodCode),
                            serialIndex = week,
                            startDate = start.plusWeeks(week),
                            endDate = start.plusWeeks(week).toWeekEnd()
                        ))
                    }
                }
            }
        }

        fun List<DailySetClazzAutoPageInfo>.calculateCurrentIndex(): Int {
            if (this.isEmpty()) {
                return -1
            }

            val now = LocalDate.now()
            if (now.isBefore(this.first().startDate)) {
                return 0
            }
            if (now.isAfter(this.last().endDate)) {
                return this.size - 1
            }
            for (item in this.indices) {
                if (this[item].startDate > now) {
                    return item - 1
                }
            }
            return this.size - 1
        }
    }



    override fun toString(): String {
        return "${year}-${periodCode.code} (${serialIndex}) (${startDate.toLongDateString()} -> ${endDate.toLongDateString()})"
    }
}