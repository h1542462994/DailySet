package org.tty.dailyset.ui.utils

import android.os.Build
import androidx.annotation.RequiresApi
import java.sql.Time
import java.sql.Timestamp
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneOffset

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
    val current = LocalDateTime.now()
    return Timestamp(current.nano.toLong() * 1000)
}