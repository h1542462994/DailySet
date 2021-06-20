package org.tty.dailyset.model.lifetime.dailytable

import androidx.compose.runtime.MutableState
import org.tty.dailyset.model.lifetime.DialogState

class DailyTableDeleteState(
    override val dialogOpen: MutableState<Boolean>
): DialogState(dialogOpen) {
}