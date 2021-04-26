package org.tty.dailyset.model.lifetime

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import org.tty.dailyset.model.entity.DailyTRC

data class DailyTableState(
    /**
     * 基于分析的DailyTRC
     */
    val dailyTRC: DailyTRC,
    /**
     * 当前的状态是否是只读状态
     */
    val readOnly: Boolean,
    /**
     * 当前是否能够添加新的DailyRow
     */
    var canAddRow: Boolean,


) {
    val dailyTableRowStateList: SnapshotStateList<DailyTableRowState> = SnapshotStateList()
    var addRowState: DailyTableAddRowState = DailyTableAddRowState(mutableStateOf(false))
    var lastState: SnapshotStateList<WeekDayState> = SnapshotStateList()

    init {
        dailyTRC.dailyRCs.forEach { dailyRC ->
            dailyTableRowStateList.add(DailyTableRowState(dailyRC, listOf()))
        }
    }

}