package org.tty.dailyset.component.dailyset.clazzauto

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import org.tty.dailyset.bean.entity.DailySetCourse
import org.tty.dailyset.bean.enums.DailySetClazzAutoViewType
import org.tty.dailyset.bean.lifetime.*
import java.time.DayOfWeek
import java.time.LocalDate

interface DailySetClazzAutoVM {
    /**
     * the current dailySetSummary
     */
    val dailySetSummary: StateFlow<DailySetSummary>

    /**
     * the current dailySetClazzAutoViewType, **week** or **term**
     */
    val dailySetClazzAutoViewType: MutableStateFlow<DailySetClazzAutoViewType>
    val dailySetClazzAutoPageInfos: StateFlow<List<DailySetClazzAutoPageInfo>>
    val dailySetCurrentPageIndex: StateFlow<Int>
    val dailySetClazzAutoPageInfosPeriod: StateFlow<List<DailySetClazzAutoPageInfoPeriod>>
    val dailySetCurrentPageIndexPeriod: StateFlow<Int>
    val dailySetTRC: StateFlow<DailySetTRC>
    val dailySetCourses: StateFlow<List<DailySetCourse>>
    val dailySetShiftDialogState: DialogState
    val now: StateFlow<LocalDate>
    val selectDayOfWeek: MutableStateFlow<DayOfWeek>


    fun toPrev()
    fun toNext()
    fun toIndex(index: Int)
    fun toNow()
}