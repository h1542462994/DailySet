package org.tty.dailyset.event

import org.tty.dailyset.model.entity.DailyTRC
import java.sql.Time


data class DailyTableModifyCellEventArgs(
    val dailyTRC: DailyTRC,
    val rowIndex: Int,
    val cellIndex: Int,
    val start: Time,
    val end: Time
): EventArgs