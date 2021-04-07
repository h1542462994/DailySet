package org.tty.dailyset.model.lifetime

import androidx.compose.runtime.snapshots.SnapshotStateList
import org.tty.dailyset.model.entity.DailyTRC

data class DailyTableState(

    val dailyTRC: DailyTRC
) {
    val dailyTableRowStateList: SnapshotStateList<DailyTableRowState>

    init {
        dailyTableRowStateList = SnapshotStateList()
        dailyTRC.dailyRCs.forEach { dailyRC ->
            dailyTableRowStateList.add(DailyTableRowState(dailyRC))
        }
    }

}