package org.tty.dailyset.model.entity

import androidx.room.Embedded
import androidx.room.Relation

data class DailyRC(
    @Embedded val dailyRow: DailyRow,
    @Relation(
        entity = DailyCell::class,
        parentColumn = "dailyRowUid",
        entityColumn = "dailyCellUid"
    )
    val dailyCells: List<DailyCell>
): Comparable<DailyRC> {
    override fun compareTo(other: DailyRC): Int {
        return dailyRow.compareTo(other.dailyRow)
    }
}

data class DailyTRC(
    @Embedded val dailyTable: DailyTable,
    @Relation(
        entity = DailyRow::class,
        parentColumn = "dailyTableUid",
        entityColumn = "dailyRowUid",
    )
    var dailyRCs: List<DailyRC>
){
    override fun toString(): String {
        return "(${dailyTable.dailyTableUid},${dailyTable.name})"
    }

    companion object {
        fun default(): DailyTRC {
            return DailyTRC(
                dailyTable = DailyTable.default(),
                dailyRCs = listOf(
                    DailyRC(
                        dailyRow = DailyRow.default(),
                        dailyCells = DailyCell.default()
                    )
                )
            )
        }
    }
}
