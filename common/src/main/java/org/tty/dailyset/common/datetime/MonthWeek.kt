package org.tty.dailyset.common.datetime

import java.time.LocalDate
import java.time.Month

data class MonthWeek(
    val week: Int,
    val month: Month,
    val weekStart: LocalDate
) {
}