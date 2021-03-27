package org.tty.dailyset.ui.utils

import java.sql.Time

fun Time.toShortString(): String {
    return this.toString().substring(0,5)
}

fun Int.toWeekDayString(): String {
    val strArray = listOf("一","二","三","四","五","六","日")
    return  "周${strArray[this]}"
}