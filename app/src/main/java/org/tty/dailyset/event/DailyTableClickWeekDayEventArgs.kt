package org.tty.dailyset.event

import org.tty.dailyset.model.entity.DailyTRC

data class DailyTableClickWeekDayEventArgs(
    val dailyTRC: DailyTRC,
    val rowIndex: Int,
    val weekDay: Int
): EventArgs