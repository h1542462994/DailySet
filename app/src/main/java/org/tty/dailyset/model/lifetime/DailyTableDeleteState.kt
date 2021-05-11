package org.tty.dailyset.model.lifetime

import androidx.compose.runtime.MutableState
import org.tty.dailyset.model.entity.DailyTRC

class DailyTableDeleteState(
    override val dialogOpen: MutableState<Boolean>
): DialogState(dialogOpen) {
}