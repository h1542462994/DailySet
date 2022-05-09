package org.tty.dailyset.component.dailyset.clazzauto

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import org.tty.dailyset.bean.entity.DailySetCourse
import org.tty.dailyset.bean.enums.DailySetClazzAutoViewType
import org.tty.dailyset.bean.lifetime.DailySetClazzAutoPageInfo
import org.tty.dailyset.bean.lifetime.DailySetSummary
import org.tty.dailyset.bean.lifetime.DailySetTRC
import org.tty.dailyset.bean.lifetime.DialogState

interface DailySetClazzAutoVM {
    val dailySetSummary: StateFlow<DailySetSummary>
    val dailySetClazzAutoViewType: MutableStateFlow<DailySetClazzAutoViewType>
    val dailySetClazzAutoPageInfos: StateFlow<List<DailySetClazzAutoPageInfo>>
    val dailySetCurrentPageIndex: StateFlow<Int>
    val dailySetTRC: StateFlow<DailySetTRC>
    val dailySetCourses: StateFlow<List<DailySetCourse>>
    val dailySetShiftDialogState: DialogState

    fun toPrev()
    fun toNext()
    fun toIndex(index: Int)
    fun toNow()
}