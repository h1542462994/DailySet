package org.tty.dailyset.event

import org.tty.dailyset.model.entity.DailyTRC

data class DailyTableRenameEventArgs(
    val dailyTRC: DailyTRC,
    val name: String
): EventArgs