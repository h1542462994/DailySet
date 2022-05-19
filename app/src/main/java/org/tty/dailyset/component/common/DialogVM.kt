package org.tty.dailyset.component.common

import kotlinx.coroutines.flow.MutableStateFlow

/**
 * dialog ViewModel, base viewModel for dialog
 * @see [SimpleDialogVMImpl]
 */
interface DialogVM {
    val dialogOpen: MutableStateFlow<Boolean>
}