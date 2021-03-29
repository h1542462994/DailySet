package org.tty.dailyset.model.lifetime

import org.tty.dailyset.model.entity.DailyTRC
import java.time.LocalDate

data class DailyTableHandler(
    val dailyTRC: DailyTRC,
    val startDate: LocalDate,
    val indexDiff: Int
)