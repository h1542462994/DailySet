package org.tty.dailyset.model.lifetime.dailyset

import androidx.compose.runtime.MutableState
import org.tty.dailyset.model.entity.DailySetIcon
import org.tty.dailyset.model.entity.DailySetType
import org.tty.dailyset.model.lifetime.DialogState

class DailySetCreateState(
    override val dialogOpen: MutableState<Boolean>,
    val selectIcon: MutableState<Boolean>,
    val icon: MutableState<DailySetIcon?>,
    val type: MutableState<DailySetType>,
    val name: MutableState<String>,

): DialogState(dialogOpen) {

}