package org.tty.dailyset.component.dailyset

import org.tty.dailyset.bean.entity.DailySet
import org.tty.dailyset.bean.entity.DailySetIcon
import org.tty.dailyset.bean.entity.DailySetType
import org.tty.dailyset.common.observable.InitialFlow
import org.tty.dailyset.component.common.BaseVM

interface DailySetVM: BaseVM {
    val dailySets: InitialFlow<List<DailySet>>

    val dailySetCreateDialogVM: DailySetCreateDialogVM

    fun setCurrentDailySetUid(dailySetUid: String)

}