package org.tty.dailyset.component.common

import kotlinx.coroutines.flow.MutableStateFlow

class SimpleDialogVMImpl(initialDialogOpen:Boolean = false): DialogVM {
    override val dialogOpen: MutableStateFlow<Boolean> = MutableStateFlow(initialDialogOpen)
}