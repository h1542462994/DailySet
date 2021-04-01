package org.tty.dailyset.model.lifetime

import androidx.compose.runtime.MutableState

open class DialogState(
    open val dialogOpen: MutableState<Boolean>
) {

}