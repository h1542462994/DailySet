package org.tty.dailyset.bean.lifetime

import org.tty.dailyset.bean.enums.DailySetPeriodCode

data class YearPeriod(
    val year: Int,
    val periodCode: DailySetPeriodCode
)
