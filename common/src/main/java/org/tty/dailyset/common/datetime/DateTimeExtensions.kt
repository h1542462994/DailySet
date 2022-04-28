package org.tty.dailyset.common.datetime

import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoField

/**
 * 一周的天数
 */
internal const val daysOneWeek = 7

internal val defaultStartDayOfWeek = DayOfWeek.MONDAY

internal val defaultWeekCalculation = WeekCalculation.MINOR

internal val epochLocalDateTime = LocalDateTime.of(LocalDate.ofEpochDay(0), LocalTime.MIN)

/**
 * to the start day of the week
 * @param startDayOfWeek the startDay of the week
 */
fun LocalDate.toWeekStart(startDayOfWeek: DayOfWeek = defaultStartDayOfWeek): LocalDate {
    var ordinal = this.dayOfWeek.value - startDayOfWeek.value
    if (ordinal < 0) {
        ordinal += daysOneWeek
    }
    return this.minusDays(ordinal.toLong())
}

/**
 * to the end day of the week
 * @param startWeekDay the startDay of the week
 */
fun LocalDate.toWeekEnd(startWeekDay: DayOfWeek = defaultStartDayOfWeek): LocalDate {
    return this.toWeekStart(startWeekDay).plusDays(daysOneWeek.toLong() - 1)
}

/**
 * the index relative to [startWeekDay]
 */
fun DayOfWeek.indexTo(startWeekDay: DayOfWeek = defaultStartDayOfWeek): Int {
    var index = this.value - startWeekDay.value
    if (index < 0) {
        index += 7
    }
    return index
}

operator fun DayOfWeek.minus(other: DayOfWeek): Int {
    return this.indexTo(other)
}

fun LocalDate.expandToCurrentWeek(startDayOfWeek: DayOfWeek = defaultStartDayOfWeek): DateSpan {
    return DateSpan.ofDate(this, startDayOfWeek = startDayOfWeek)
}

fun epochLocalDateTime(): LocalDateTime {
    return epochLocalDateTime
}

fun LocalDate.yearWeek(startDayOfWeek: DayOfWeek = defaultStartDayOfWeek, weekCalculation: WeekCalculation = defaultWeekCalculation): YearWeek {
    TODO()
}

fun LocalDate.monthWeek(startDayOfWeek: DayOfWeek = defaultStartDayOfWeek, weekCalculation: WeekCalculation = defaultWeekCalculation): MonthWeek {
    TODO()
}

/**
 * like 08:00 (n) | 8:00 (y)
 */
fun LocalTime.toShortString(ignoreFirstZero: Boolean = false): String {
    val r = this.format(DateTimeFormatter.ISO_LOCAL_TIME).substring("00:00".indices)
    return if (!ignoreFirstZero || r[0] != '0') {
        r
    } else {
        r.drop(1)
    }
}

/**
 * like 1/1
 */
fun LocalDate.toShortString(): String {
    return "${this.monthValue}/${this.dayOfMonth}"
}

fun LocalDateTime.toShortString(): String {
    return "${this.toLocalDate().toShortString()} ${this.toLocalTime().toShortString()}"
}

fun LocalDateTime.toStandardString(): String {
    return this.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
}

infix fun LocalTime.minutesTo(end: LocalTime): Long {
    return end.getLong(ChronoField.MINUTE_OF_DAY) - getLong(ChronoField.MINUTE_OF_DAY)
}

fun DayOfWeek.toDisplayString(): String {
    val strArray = listOf("一","二","三","四","五","六","日")
    return  "周${strArray[this.value - 1]}"
}

fun intToDayOfWeek(dayOfWeek: Int): DayOfWeek {
    return DayOfWeek.of(dayOfWeek)
}
