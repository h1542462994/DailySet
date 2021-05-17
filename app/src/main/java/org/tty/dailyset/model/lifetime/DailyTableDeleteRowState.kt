package org.tty.dailyset.model.lifetime

import androidx.compose.runtime.MutableState


open class DailyTableDeleteRowState(
    override val dialogOpen: MutableState<Boolean>,
    val rowIndex: MutableState<Int>
): DialogState(dialogOpen = dialogOpen)