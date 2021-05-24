package org.tty.dailyset.ui.utils

import org.tty.dailyset.model.lifetime.WeekDayState
import java.sql.Time
import java.sql.Timestamp
import java.time.*
import java.time.temporal.TemporalUnit
import java.util.*

fun Time.toShortString(): String {
    return this.toString().substring(0,5)
}

fun Int.toWeekDayString(): String {
    val strArray = listOf("一","二","三","四","五","六","日")
    return  "周${strArray[this]}"
}

fun LocalDateTime.toWeekStart(): LocalDateTime {
    // the ordinal of the day
    val ordinal = this.dayOfWeek.value - 1
    return this.minusDays(ordinal.toLong())
}

fun LocalDateTime.toShortDateString(): String {
    return "${monthValue}/${dayOfMonth}"
}


// TODO: 2021/3/27 添加扩展支持
fun LocalDate.toWeekStart(): LocalDate {
    // the ordinal of the day
    val ordinal = this.dayOfWeek.value - 1
    return this.minusDays(ordinal.toLong())
}

fun LocalDate.toShortDateString(): String {
    return "${monthValue}/${dayOfMonth}"
}

fun minus(end: LocalDate, start: LocalDate): Long {
    return end.toEpochDay() - start.toEpochDay()
}

fun localTimestampNow(): Timestamp {
    val current = LocalDateTime.now().nano
    val timestamp = Timestamp(0)
    timestamp.nanos = current
    return timestamp
}

fun second(): Int {
    return LocalDateTime.now().second
}

fun List<WeekDayState>.toIntArray(): IntArray {
    val list = mutableListOf<Int>()
    this.forEachIndexed { index, weekDay ->
        if (weekDay.checked) {
            list.add(index + 1)
        }
    }
    return list.toIntArray()
}

fun Time.plus(amountToAdd: Long, unit: TemporalUnit): Time {
    var localTime = LocalTime.ofNanoOfDay(this.time * 1000000)
    localTime = localTime.plus(amountToAdd, unit)
    return Time(localTime.toNanoOfDay() / 1000000)
}

fun Time.hm(): Pair<Int, Int> {
    val trims = toShortString().split(":")
    return Pair(trims[0].toInt(), trims[1].toInt())
}

fun rangeX(min: Int, max: Int, space: Int): List<Int> {
    val list = mutableListOf<Int>()
    var c = min
    while (c < max) {
        list.add(c)
        c += space
    }
    return list
}

fun <T> Map<*, List<T>>.startIndexes(): List<Int> {
    var startIndex = 0;
    val record = mutableListOf<Int>()
    forEach { (_, l) ->
        record.add(startIndex)
        startIndex += l.size
    }
    return record
}

fun spanMinutes(start: Time, end: Time): Long {
    return (end.time - start.time) / (1000 * 60)
}