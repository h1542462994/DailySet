package org.tty.dailyset.bean.lifetime.dailytable

import androidx.compose.runtime.MutableState
import org.tty.dailyset.bean.lifetime.DialogState


open class DailyTableDeleteRowState(
    override val dialogOpen: MutableState<Boolean>,
    val rowIndex: MutableState<Int>
): DialogState(dialogOpen = dialogOpen)