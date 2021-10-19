package org.tty.dailyset

import org.tty.dailyset.model.lifetime.WeekDayState
import java.sql.Time
import java.sql.Timestamp
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.temporal.ChronoField
import java.time.temporal.TemporalUnit

fun Time.toShortString(): String {
    return this.toString().substring(0,5)
}

fun Int.toWeekDayString(): String {
    val strArray = listOf("一","二","三","四","五","六","日")
    return  "周${strArray[this]}"
}

fun LocalDate.toShortDateString(): String {
    return "${monthValue}/${dayOfMonth}"
}

fun LocalDate.toLongDateString(): String {
    val weekOfDays = listOf("一","")
    return "${year}/${monthValue}/${dayOfMonth} (${(this.dayOfWeek.value - 1).toWeekDayString()})"
}

fun minus(end: LocalDate, start: LocalDate): Long {
    return end.toEpochDay() - start.toEpochDay()
}

fun localTimestampNow(): Timestamp {
    val instant = LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant()
    val time = instant.toEpochMilli()
    val timestamp = Timestamp(time)
    timestamp.nanos = instant.nano

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
    val instant = this.toInstant()
    val zoneId = ZoneId.systemDefault()
    val localDateTime = instant.atZone(zoneId).toLocalDateTime()
        .plus(amountToAdd, unit)
    return Time(Time.from(localDateTime.atZone(zoneId).toInstant()).time)
}

fun Time.day(): Long {
    val instant = this.toInstant()
    val zoneId = ZoneId.systemDefault()
    val localDateTime = instant.atZone(zoneId).toLocalDateTime()
    return localDateTime.getLong(ChronoField.EPOCH_DAY)
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
    var startIndex = 0
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

fun LocalDate.toEpochMilli(): Long {
    return this.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
}