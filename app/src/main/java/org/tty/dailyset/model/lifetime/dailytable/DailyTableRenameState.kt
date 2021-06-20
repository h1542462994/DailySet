package org.tty.dailyset.model.lifetime.dailytable

import androidx.compose.runtime.MutableState
import org.tty.dailyset.model.entity.DailyTable
import org.tty.dailyset.model.lifetime.DialogState

class DailyTableRenameState(
    override val dialogOpen: MutableState<Boolean>,
    val name: MutableState<String>,
    val dailyTable: DailyTable
): DialogState(dialogOpen) {
}