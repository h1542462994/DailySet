package org.tty.dailyset.model.lifetime.dailytable

import androidx.compose.runtime.MutableState
import org.tty.dailyset.model.lifetime.DialogState


open class DailyTableDeleteRowState(
    override val dialogOpen: MutableState<Boolean>,
    val rowIndex: MutableState<Int>
): DialogState(dialogOpen = dialogOpen)