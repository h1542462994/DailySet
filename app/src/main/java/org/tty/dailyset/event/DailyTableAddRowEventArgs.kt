package org.tty.dailyset.event

import org.tty.dailyset.model.entity.DailyTRC
import org.tty.dailyset.model.lifetime.WeekDayState

data class DailyTableAddRowEventArgs(
    val dailyTRC: DailyTRC,
    val weekDays: IntArray
): EventArgs {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as DailyTableAddRowEventArgs

        if (dailyTRC != other.dailyTRC) return false
        if (!weekDays.contentEquals(other.weekDays)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = dailyTRC.hashCode()
        result = 31 * result + weekDays.contentHashCode()
        return result
    }
}
