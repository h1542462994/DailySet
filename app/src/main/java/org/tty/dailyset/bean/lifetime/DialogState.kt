package org.tty.dailyset.bean.lifetime

import androidx.compose.runtime.MutableState

@Deprecated("use dialogVM instead.")
open class DialogState(
    open val dialogOpen: MutableState<Boolean>
) {

}