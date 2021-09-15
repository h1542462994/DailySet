package org.tty.dailyset.common.datetime

import java.time.LocalDate

data class YearWeek(
    val year: Int,
    val week: Int,
    val weekStart: LocalDate
)
