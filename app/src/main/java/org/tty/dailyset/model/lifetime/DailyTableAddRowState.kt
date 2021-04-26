package org.tty.dailyset.model.lifetime

import androidx.compose.runtime.MutableState

class DailyTableAddRowState(
    override val dialogOpen: MutableState<Boolean>
): DialogState(dialogOpen = dialogOpen)