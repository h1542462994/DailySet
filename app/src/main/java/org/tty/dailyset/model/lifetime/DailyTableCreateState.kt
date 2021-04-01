package org.tty.dailyset.model.lifetime

import androidx.compose.runtime.MutableState

class DailyTableCreateState(
    override val dialogOpen: MutableState<Boolean>,
    val name: MutableState<String>,
    val onCreate: () -> Unit
): DialogState(dialogOpen = dialogOpen)