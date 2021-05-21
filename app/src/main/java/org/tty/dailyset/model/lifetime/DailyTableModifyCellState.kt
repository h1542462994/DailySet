package org.tty.dailyset.model.lifetime

import androidx.compose.runtime.MutableState
import java.sql.Time

class DailyTableModifyCellState(
    override val dialogOpen: MutableState<Boolean>,
    val modifyCellStateWrap: MutableState<ModifyCellStateWrap>
): DialogState(dialogOpen = dialogOpen)

data class ModifyCellStateWrap(
    val rowIndex: Int,
    val cellIndex: Int,
    val min: Time?,
    val start: Time,
    val end: Time
)