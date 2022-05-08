package org.tty.dailyset.component.dailyset

import kotlinx.coroutines.flow.StateFlow
import org.tty.dailyset.bean.lifetime.DailySetSummary
import org.tty.dailyset.component.common.BaseVM

interface DailySetVM: BaseVM {
    val dailySetSummaries: StateFlow<List<DailySetSummary>>

    val dailySetCreateDialogVM: DailySetCreateDialogVM

    fun setCurrentDailySetUid(dailySetUid: String)

}