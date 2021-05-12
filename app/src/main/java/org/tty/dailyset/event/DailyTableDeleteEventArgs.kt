package org.tty.dailyset.event

import org.tty.dailyset.model.entity.DailyTRC

data class DailyTableDeleteEventArgs(
    val dailyTRC: DailyTRC
): EventArgs