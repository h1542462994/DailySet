package org.tty.dailyset.bean.lifetime.dailytable

import androidx.compose.runtime.MutableState
import org.tty.dailyset.bean.entity.DailyTable
import org.tty.dailyset.bean.lifetime.DialogState

class DailyTableRenameState(
    override val dialogOpen: MutableState<Boolean>,
    val name: MutableState<String>,
    val dailyTable: DailyTable
): DialogState(dialogOpen) {
}