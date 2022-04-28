package org.tty.dailyset.component.dailyset

import org.tty.dailyset.bean.entity.DailySetIcon
import org.tty.dailyset.bean.entity.DailySetType
import org.tty.dailyset.common.observable.InitialFlow
import org.tty.dailyset.common.observable.InitialMutableSharedFlow
import org.tty.dailyset.component.common.DialogVM

interface DailySetCreateDialogVM: DialogVM {
    val selectIcon: InitialMutableSharedFlow<Boolean>
    val icon: InitialMutableSharedFlow<DailySetIcon?>
    val type: InitialMutableSharedFlow<DailySetType>
    val name: InitialMutableSharedFlow<String>
    val currentUserUid: InitialFlow<String>

    fun createDailySet(dailySetName: String, type: DailySetType, icon: DailySetIcon?)
}