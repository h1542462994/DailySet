package org.tty.dailyset.bean.lifetime.dailytable

import androidx.compose.runtime.MutableState
import org.tty.dailyset.bean.lifetime.DialogState

class DailyTableModifySectionState(
    override val dialogOpen: MutableState<Boolean>,
    val rowIndex: MutableState<Int>,
    val counts: MutableState<IntArray>
): DialogState(dialogOpen = dialogOpen) {

}