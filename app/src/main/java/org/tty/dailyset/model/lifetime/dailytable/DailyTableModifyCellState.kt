package org.tty.dailyset.model.lifetime.dailytable

import androidx.compose.runtime.MutableState
import org.tty.dailyset.model.lifetime.DialogState
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