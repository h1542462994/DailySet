package org.tty.dailyset.bean.lifetime

import org.tty.dailyset.bean.enums.DailySetPeriodCode

data class DailySetClazzAutoPageInfoPeriod(
    val year: Int,
    val periodCode: DailySetPeriodCode,
    val startIndex: Int,
    val count: Int
)