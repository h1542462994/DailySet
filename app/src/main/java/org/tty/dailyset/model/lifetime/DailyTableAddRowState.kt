package org.tty.dailyset.model.lifetime

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.snapshots.SnapshotStateList

class DailyTableAddRowState(
    override val dialogOpen: MutableState<Boolean>,
    var lastState: SnapshotStateList<WeekDayState> = SnapshotStateList(),
    val onAddRow: (List<WeekDayState>) -> Unit
): DialogState(dialogOpen = dialogOpen) {
    fun onItemClick(index: Int) {
        val current = lastState[index]
        if (!current.checked){
            val mutableUnCheckedCount = lastState.count {
                !it.readOnly && !it.checked
            }
            if (mutableUnCheckedCount <= 1){
                return
            }
        }

        lastState[index] = current.copy(
            checked = !current.checked
        )
    }
}