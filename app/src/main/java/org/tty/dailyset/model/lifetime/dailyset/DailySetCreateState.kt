package org.tty.dailyset.model.lifetime.dailyset

import androidx.compose.runtime.MutableState
import org.tty.dailyset.model.entity.DailySetIcon
import org.tty.dailyset.model.entity.DailySetType
import org.tty.dailyset.model.lifetime.DialogState

/**
 * the state for dialog to create a dailySet.
 */
class DailySetCreateState(
    override val dialogOpen: MutableState<Boolean>,
    val selectIcon: MutableState<Boolean>,
    val icon: MutableState<DailySetIcon?>,
    val type: MutableState<DailySetType>,
    val name: MutableState<String>,

): DialogState(dialogOpen) {
    fun clean() {
        selectIcon.value = false
        icon.value = null
        type.value = DailySetType.Normal
        name.value = ""
    }
}