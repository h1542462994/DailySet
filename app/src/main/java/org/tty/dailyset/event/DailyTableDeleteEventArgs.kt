package org.tty.dailyset.event

import org.tty.dailyset.bean.entity.DailyTRC

data class DailyTableDeleteEventArgs(
    val dailyTRC: DailyTRC
): EventArgs