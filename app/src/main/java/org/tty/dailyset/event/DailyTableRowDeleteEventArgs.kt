package org.tty.dailyset.event

import org.tty.dailyset.bean.entity.DailyTRC

data class DailyTableRowDeleteEventArgs(
    val dailyTRC: DailyTRC,
    val rowIndex: Int): EventArgs