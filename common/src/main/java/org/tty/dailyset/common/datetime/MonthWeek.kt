package org.tty.dailyset.common.datetime

import java.time.LocalDate
import java.time.Month

data class MonthWeek(
    val month: Month,
    val week: Int,
    val weekStart: LocalDate
) {
}