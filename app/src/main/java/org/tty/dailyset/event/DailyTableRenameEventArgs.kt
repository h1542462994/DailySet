package org.tty.dailyset.event

import org.tty.dailyset.bean.entity.DailyTRC

@Deprecated("not used yet.", level = DeprecationLevel.WARNING)
data class DailyTableRenameEventArgs(
    val dailyTRC: DailyTRC,
    val name: String
): EventArgs