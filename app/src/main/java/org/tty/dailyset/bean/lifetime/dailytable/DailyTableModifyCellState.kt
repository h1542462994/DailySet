package org.tty.dailyset.bean.lifetime.dailytable

import androidx.compose.runtime.MutableState
import org.tty.dailyset.bean.lifetime.DialogState
import java.time.LocalTime

class DailyTableModifyCellState(
    override val dialogOpen: MutableState<Boolean>,
    val modifyCellStateWrap: MutableState<ModifyCellStateWrap>
): DialogState(dialogOpen = dialogOpen)

data class ModifyCellStateWrap(
    val rowIndex: Int,
    val cellIndex: Int,
    val min: LocalTime?,
    val start: LocalTime,
    val end: LocalTime
)