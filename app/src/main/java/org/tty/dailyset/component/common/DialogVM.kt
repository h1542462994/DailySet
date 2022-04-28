package org.tty.dailyset.component.common

import org.tty.dailyset.common.observable.InitialMutableSharedFlow

interface DialogVM {
    val dialogOpen: InitialMutableSharedFlow<Boolean>
}