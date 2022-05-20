package org.tty.dailyset.component.dailyset.clazzauto

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import org.tty.dailyset.bean.entity.DailySetCourse
import org.tty.dailyset.bean.enums.DailySetClazzAutoViewType
import org.tty.dailyset.bean.lifetime.*
import org.tty.dailyset.component.common.DialogVM
import java.time.DayOfWeek
import java.time.LocalDate

interface DailySetClazzAutoVM {
    /**
     * **hold** rename dialogVM
     */
    val dailySetRenameDialogVM: DailySetRenameDialogVM

    /**
     * **hold** shift dialogVM
     */
    val dailySetShiftDialogVM: DialogVM

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
    val nowDate: StateFlow<LocalDate>
    val selectDayOfWeek: MutableStateFlow<DayOfWeek>

    /**
     * **intent** : to prev page.
     */
    fun toPrev()
    /**
     * **intent** : to next page.
     */
    fun toNext()
    /**
     * **intent** : to page index of ?
     */
    fun toIndex(index: Int)
    /**
     * **intent**: to page on now
     */
    fun toNow()

    /**
     * **intent** open renameDialog
     */
    fun openRenameDialog()
}