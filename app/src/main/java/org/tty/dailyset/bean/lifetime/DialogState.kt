package org.tty.dailyset.bean.lifetime

import androidx.compose.runtime.MutableState

open class DialogState(
    open val dialogOpen: MutableState<Boolean>
) {

}