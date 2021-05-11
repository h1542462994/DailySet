package org.tty.dailyset.model.lifetime

import androidx.compose.runtime.snapshots.SnapshotStateList
import org.tty.dailyset.model.entity.DailyRC

@Deprecated("")
class DailyTableRowState(
    val dailyRC: DailyRC,
    var weekDays: List<WeekDayState>
) {

}