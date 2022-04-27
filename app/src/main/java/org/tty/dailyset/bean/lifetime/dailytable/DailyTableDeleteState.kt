package org.tty.dailyset.bean.lifetime.dailytable

import androidx.compose.runtime.MutableState
import org.tty.dailyset.bean.lifetime.DialogState

class DailyTableDeleteState(
    override val dialogOpen: MutableState<Boolean>
): DialogState(dialogOpen) {
}