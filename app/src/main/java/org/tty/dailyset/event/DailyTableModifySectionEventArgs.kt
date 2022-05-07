package org.tty.dailyset.event

import org.tty.dailyset.bean.entity.DailyTRC

@Deprecated("not used yet.", level = DeprecationLevel.WARNING)
data class DailyTableModifySectionEventArgs(
    val dailyTRC: DailyTRC,
    val rowIndex: Int,
    val counts: IntArray
): EventArgs {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as DailyTableModifySectionEventArgs

        if (dailyTRC != other.dailyTRC) return false
        if (rowIndex != other.rowIndex) return false
        if (!counts.contentEquals(other.counts)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = dailyTRC.hashCode()
        result = 31 * result + rowIndex
        result = 31 * result + counts.contentHashCode()
        return result
    }
}