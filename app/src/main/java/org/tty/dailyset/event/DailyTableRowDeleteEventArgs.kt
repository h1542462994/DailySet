package org.tty.dailyset.event

import org.tty.dailyset.model.entity.DailyTRC

data class DailyTableRowDeleteEventArgs(
    val dailyTRC: DailyTRC,
    val rowIndex: Int): EventArgs