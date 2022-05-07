package org.tty.dailyset.event

import org.tty.dailyset.bean.entity.DailyTRC

@Deprecated("not used yet.", level = DeprecationLevel.WARNING)
data class DailyTableClickWeekDayEventArgs(
    val dailyTRC: DailyTRC,
    val rowIndex: Int,
    val weekDay: Int
): EventArgs