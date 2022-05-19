package org.tty.dailyset.component.common

import kotlinx.coroutines.flow.MutableStateFlow

/**
 * simple implementation for [DialogVM]
 */
class SimpleDialogVMImpl(initialDialogOpen:Boolean = false): DialogVM {
    override val dialogOpen: MutableStateFlow<Boolean> = MutableStateFlow(initialDialogOpen)
}