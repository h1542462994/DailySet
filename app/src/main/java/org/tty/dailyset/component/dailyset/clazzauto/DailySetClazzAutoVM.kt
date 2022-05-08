package org.tty.dailyset.component.dailyset.clazzauto

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import org.tty.dailyset.bean.enums.DailySetClazzAutoViewType
import org.tty.dailyset.bean.lifetime.DailySetClazzAutoPageInfo
import org.tty.dailyset.bean.lifetime.DailySetSummary

interface DailySetClazzAutoVM {
    val dailySetSummary: StateFlow<DailySetSummary>
    val dailySetClazzAutoViewType: MutableStateFlow<DailySetClazzAutoViewType>
    val dailySetClazzAutoPageInfos: StateFlow<List<DailySetClazzAutoPageInfo>>
    val dailySetCurrentPageIndex: StateFlow<Int>
}