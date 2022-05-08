package org.tty.dailyset.bean.lifetime

import org.tty.dailyset.bean.entity.DailySetDuration
import org.tty.dailyset.bean.enums.DailySetPeriodCode
import org.tty.dailyset.common.datetime.DateSpan
import org.tty.dailyset.common.datetime.plusWeeks
import org.tty.dailyset.common.datetime.toWeekEnd
import org.tty.dailyset.common.datetime.toWeekStart
import org.tty.dailyset.toLongDateString
import java.time.LocalDate

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
    }

    override fun toString(): String {
        return "${year}-${periodCode.code} (${serialIndex}) (${startDate.toLongDateString()} -> ${endDate.toLongDateString()})"
    }
}