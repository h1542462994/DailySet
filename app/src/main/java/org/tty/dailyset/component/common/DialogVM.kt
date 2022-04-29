package org.tty.dailyset.component.common

import org.tty.dailyset.common.observable.InitialMutableStateFlow

interface DialogVM {
    val dialogOpen: InitialMutableStateFlow<Boolean>
}