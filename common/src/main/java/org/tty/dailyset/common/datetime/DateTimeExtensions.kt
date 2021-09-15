package org.tty.dailyset.common.datetime

import java.time.DayOfWeek
import java.time.LocalDate

/**
 * 一周的天数
 */
internal const val daysOneWeek = 7

/**
 * to the start day of the week
 * @param startDayOfWeek the startDay of the week
 */
fun LocalDate.toWeekStart(startDayOfWeek: DayOfWeek = DayOfWeek.MONDAY): LocalDate {
    var ordinal = this.dayOfWeek.value - startDayOfWeek.value
    if (ordinal < 0) {
        ordinal += daysOneWeek
    }
    return this.minusDays(ordinal.toLong())
}

/**
 * 相对于DayOfWeek的index
 */
fun DayOfWeek.indexTo(startDayOfWeek: DayOfWeek): Int {
    var index = this.value - startDayOfWeek.value
    if (index < 0) {
        index += 7
    }
    return index
}

fun LocalDate.yearWeek(startDayOfWeek: DayOfWeek = DayOfWeek.MONDAY, weekCalculation: WeekCalculation = WeekCalculation.MINOR): YearWeek {
    TODO()
}

fun LocalDate.monthWeek(startDayOfWeek: DayOfWeek = DayOfWeek.MONDAY, weekCalculation: WeekCalculation = WeekCalculation.MINOR): MonthWeek {
    TODO()
}