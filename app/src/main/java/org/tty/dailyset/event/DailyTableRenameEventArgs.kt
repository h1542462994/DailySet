package org.tty.dailyset.event

import org.tty.dailyset.bean.entity.DailyTRC

data class DailyTableRenameEventArgs(
    val dailyTRC: DailyTRC,
    val name: String
): EventArgs