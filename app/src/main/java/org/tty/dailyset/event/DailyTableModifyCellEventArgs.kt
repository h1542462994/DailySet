package org.tty.dailyset.event

import org.tty.dailyset.bean.entity.DailyTRC
import java.time.LocalTime


data class DailyTableModifyCellEventArgs(
    val dailyTRC: DailyTRC,
    val rowIndex: Int,
    val cellIndex: Int,
    val start: LocalTime,
    val end: LocalTime
): EventArgs