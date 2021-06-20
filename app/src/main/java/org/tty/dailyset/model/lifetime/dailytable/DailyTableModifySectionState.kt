package org.tty.dailyset.model.lifetime.dailytable

import androidx.compose.runtime.MutableState
import org.tty.dailyset.model.lifetime.DialogState

class DailyTableModifySectionState(
    override val dialogOpen: MutableState<Boolean>,
    val rowIndex: MutableState<Int>,
    val counts: MutableState<IntArray>
): DialogState(dialogOpen = dialogOpen) {

}