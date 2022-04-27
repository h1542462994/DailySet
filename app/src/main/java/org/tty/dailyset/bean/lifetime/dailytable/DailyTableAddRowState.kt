package org.tty.dailyset.bean.lifetime.dailytable

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import org.tty.dailyset.bean.lifetime.DialogState
import org.tty.dailyset.bean.lifetime.WeekDayState

class DailyTableAddRowState(
    override val dialogOpen: MutableState<Boolean>,
    var _initialLastState: List<WeekDayState> = listOf(),
    var _lastState: MutableState<List<WeekDayState>> = mutableStateOf(listOf())
): DialogState(dialogOpen = dialogOpen) {
    val lastState = _lastState.value

    private fun selectableCount(): Int {
        return _initialLastState.count {
            !it.readOnly
        }
    }

    private fun checkedCount(): Int {
        return _lastState.value.count {
            it.checked
        }
    }

    fun onItemClick(index: Int) {
        val checked = _lastState.value[index].checked
        val selectableCount = selectableCount()
        val checkedCount = checkedCount()

        Log.d("DailyTableAddRowState","$selectableCount,$checkedCount")

        if (!checked && checkedCount + 2 >= selectableCount) {
            //toReadOnly
            _lastState.value = _lastState.value.mapIndexed { i, weekDayState ->
                val currentChecked = if (i == index) { !checked } else weekDayState.checked
                if (currentChecked) {
                    WeekDayState(readOnly = false, checked = true)
                } else {
                    WeekDayState(readOnly = true, checked = false)
                }
            }
        } else {
            //toModify
            _lastState.value = _lastState.value.mapIndexed { i, weekDayState ->
                val currentChecked = if (i == index) { !checked } else weekDayState.checked
                if (currentChecked) {
                    WeekDayState(readOnly = false, checked = true)
                } else  {
                    WeekDayState(readOnly = _initialLastState[i].readOnly, checked = false)
                }
            }
        }
    }
}