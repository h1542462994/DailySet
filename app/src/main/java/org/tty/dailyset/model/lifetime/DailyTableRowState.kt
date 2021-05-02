package org.tty.dailyset.model.lifetime

import androidx.compose.runtime.snapshots.SnapshotStateList
import org.tty.dailyset.model.entity.DailyRC

class DailyTableRowState(
    val dailyRC: DailyRC,
    var weekDays: List<WeekDayState>
) {

}