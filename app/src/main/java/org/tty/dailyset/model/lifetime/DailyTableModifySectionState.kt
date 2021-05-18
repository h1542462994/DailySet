package org.tty.dailyset.model.lifetime

import androidx.compose.runtime.MutableState

class DailyTableModifySectionState(
    override val dialogOpen: MutableState<Boolean>,
    val rowIndex: MutableState<Int>,
    val counts: MutableState<IntArray>
): DialogState(dialogOpen = dialogOpen) {

}