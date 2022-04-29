package org.tty.dailyset.component.dailyset

import org.tty.dailyset.bean.entity.DailySetIcon
import org.tty.dailyset.bean.entity.DailySetType
import org.tty.dailyset.common.observable.InitialFlow
import org.tty.dailyset.common.observable.InitialMutableStateFlow
import org.tty.dailyset.component.common.DialogVM

interface DailySetCreateDialogVM: DialogVM {
    val selectIcon: InitialMutableStateFlow<Boolean>
    val icon: InitialMutableStateFlow<DailySetIcon?>
    val type: InitialMutableStateFlow<DailySetType>
    val name: InitialMutableStateFlow<String>
    val currentUserUid: InitialFlow<String>

    fun createDailySet(dailySetName: String, type: DailySetType, icon: DailySetIcon?)
}