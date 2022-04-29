package org.tty.dailyset.component.dailyset

import kotlinx.coroutines.flow.StateFlow
import org.tty.dailyset.bean.entity.DailySet
import org.tty.dailyset.component.common.BaseVM

interface DailySetVM: BaseVM {
    val dailySets: StateFlow<List<DailySet>>

    val dailySetCreateDialogVM: DailySetCreateDialogVM

    fun setCurrentDailySetUid(dailySetUid: String)

}