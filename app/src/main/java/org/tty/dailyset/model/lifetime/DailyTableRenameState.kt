package org.tty.dailyset.model.lifetime

import androidx.compose.runtime.MutableState
import org.tty.dailyset.model.entity.DailyTable

class DailyTableRenameState(
    override val dialogOpen: MutableState<Boolean>,
    val name: MutableState<String>,
    val dailyTable: DailyTable
): DialogState(dialogOpen) {
}