package org.tty.dailyset.component.common

import kotlinx.coroutines.flow.MutableStateFlow

interface DialogVM {
    val dialogOpen: MutableStateFlow<Boolean>
}