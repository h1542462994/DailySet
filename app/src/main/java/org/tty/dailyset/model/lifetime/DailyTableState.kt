package org.tty.dailyset.model.lifetime

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import org.tty.dailyset.model.entity.DailyTRC

@Deprecated("the class is too heavy. use [DailyTable2] instead")
class DailyTableState(
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
    var canAddRow: Boolean
) {
    var dailyTableRowStateList: List<DailyTableRowState> = listOf()
    var addRowState: DailyTableAddRowState = DailyTableAddRowState(mutableStateOf(false))
    var deleteState: DailyTableDeleteState = DailyTableDeleteState(mutableStateOf(false))

    init {
        dailyTableRowStateList = dailyTRC.dailyRCs.map { dailyRC ->
            DailyTableRowState(dailyRC = dailyRC, weekDays = listOf())
        }
    }

}