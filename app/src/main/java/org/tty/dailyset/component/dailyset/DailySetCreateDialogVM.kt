package org.tty.dailyset.component.dailyset

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import org.tty.dailyset.bean.enums.DailySetIcon
import org.tty.dailyset.bean.enums.DailySetType
import org.tty.dailyset.component.common.DialogVM

interface DailySetCreateDialogVM: DialogVM {
    val selectIcon: MutableStateFlow<Boolean>
    val icon: MutableStateFlow<DailySetIcon?>
    val type: MutableStateFlow<DailySetType>
    val name: MutableStateFlow<String>
    val currentUserUid: StateFlow<String>

//    fun createDailySet(dailySetName: String, type: DailySetType, icon: DailySetIcon?)
}