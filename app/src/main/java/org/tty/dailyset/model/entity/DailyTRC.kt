package org.tty.dailyset.model.entity

import androidx.room.Embedded
import androidx.room.Relation

data class DailyRC(
    @Embedded val dailyRow: DailyRow,
    @Relation(
        entity = DailyCell::class,
        parentColumn = "uid",
        entityColumn = "dailyRowUid"
    )
    val dailyCells: List<DailyCell>
)

data class DailyTRC(
    @Embedded val dailyTable: DailyTable,
    @Relation(
        entity = DailyRow::class,
        parentColumn = "uid",
        entityColumn = "dailyTableUid"
    )
    var dailyRCs: List<DailyRC>
){
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
