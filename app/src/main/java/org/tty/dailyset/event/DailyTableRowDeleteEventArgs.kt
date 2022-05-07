package org.tty.dailyset.event

import org.tty.dailyset.bean.entity.DailyTRC

@Deprecated("not used yet.", level = DeprecationLevel.WARNING)
data class DailyTableRowDeleteEventArgs(
    val dailyTRC: DailyTRC,
    val rowIndex: Int): EventArgs