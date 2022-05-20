package org.tty.dailyset.component.dailyset.clazzauto

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.withContext
import org.tty.dailyset.bean.entity.DailySetCourse
import org.tty.dailyset.bean.entity.DailySetDuration
import org.tty.dailyset.bean.entity.DefaultEntities
import org.tty.dailyset.bean.enums.DailySetClazzAutoViewType
import org.tty.dailyset.bean.lifetime.*
import org.tty.dailyset.bean.lifetime.DailySetClazzAutoPageInfo.Companion.calculateCurrentIndex
import org.tty.dailyset.bean.lifetime.DailySetClazzAutoPageInfo.Companion.toPageInfoPeriods
import org.tty.dailyset.common.observable.flow2
import org.tty.dailyset.common.observable.flowMulti
import org.tty.dailyset.component.common.SharedComponents
import org.tty.dailyset.component.common.asAppStateFlow
import org.tty.dailyset.component.common.sharedComponents
import java.time.DayOfWeek
import java.time.LocalDate

@Composable
fun rememberClazzAutoDailySetVM(dailySetUid: String): DailySetClazzAutoVM {
    val sharedComponents = sharedComponents()
    return sharedComponents.viewModelStore.getVM("dailyset/clazzauto/${dailySetUid}") {
        DailySetClazzAutoVMImpl(sharedComponents, dailySetUid)
    }
}

class DailySetClazzAutoVMImpl(val sharedComponents: SharedComponents, val dailySetUid: String) :
    DailySetClazzAutoVM {
    override val dailySetSummary: StateFlow<DailySetSummary> =
        produceDailySetSummaryFlow().asAppStateFlow(DefaultEntities.emptyDailySetSummary())
    override val dailySetClazzAutoViewType: MutableStateFlow<DailySetClazzAutoViewType> =
        MutableStateFlow(DailySetClazzAutoViewType.Week)
    override val dailySetClazzAutoPageInfos: StateFlow<List<DailySetClazzAutoPageInfo>> =
        produceDailySetClazzAutoPagerInfosFlow().asAppStateFlow(listOf())
    override val dailySetCurrentPageIndex = MutableStateFlow(-1)
    override val dailySetClazzAutoPageInfosPeriod: StateFlow<List<DailySetClazzAutoPageInfoPeriod>> = dailySetClazzAutoPageInfos.map {
        it.toPageInfoPeriods()
    }.asAppStateFlow(listOf())
    override val dailySetCurrentPageIndexPeriod: StateFlow<Int> = flow2(dailySetClazzAutoPageInfosPeriod, dailySetCurrentPageIndex) { v1, v2 ->
        if (dailySetClazzAutoPageInfos.value.isNotEmpty() && v2 >= 0 && v2 < dailySetClazzAutoPageInfos.value.size) {
            val currentPage = dailySetClazzAutoPageInfos.value[v2]
            return@flow2 v1.indexOfFirst { it.year == currentPage.year && it.periodCode == currentPage.periodCode }
        } else {
            -1
        }
    }.asAppStateFlow(-1)

    override val dailySetTRC: StateFlow<DailySetTRC> =
        produceDailySetTRCFlow().asAppStateFlow(DefaultEntities.emptyDailySetTRC())
    override val dailySetCourses: StateFlow<List<DailySetCourse>> =
        produceDailySetCoursesFlow().asAppStateFlow(emptyList())
    override val dailySetShiftDialogState: DialogState = DialogState(mutableStateOf(false))
    override val now: StateFlow<LocalDate> = sharedComponents.dataSourceCollection.runtimeDataSource.nowDate.asAppStateFlow(
        LocalDate.now())
    override val selectDayOfWeek: MutableStateFlow<DayOfWeek> = MutableStateFlow(LocalDate.now().dayOfWeek)

    override fun toPrev() {
        if (dailySetClazzAutoViewType.value == DailySetClazzAutoViewType.Week) {
            // if the viewType is week, just decrease 1 page.
            if (dailySetClazzAutoPageInfos.value.isNotEmpty() && dailySetCurrentPageIndex.value > 0) {
                dailySetCurrentPageIndex.value = dailySetCurrentPageIndex.value - 1
            }
        } else {
            // if the viewType is term, will calculate the suitable page index.
            if (validPage()) {
                val dailySetClazzAutoPageInfoPeriods = dailySetClazzAutoPageInfos.value.toPageInfoPeriods()
                val currentPageInfo = currentPageInfo()
                val currentPageIndexPeriod = dailySetClazzAutoPageInfoPeriods.indexOfFirst {
                    it.year == currentPageInfo.year && it.periodCode == currentPageInfo.periodCode
                }
                if (currentPageIndexPeriod > 0) {
                    val prevPageInfoPeriod = dailySetClazzAutoPageInfoPeriods[currentPageIndexPeriod - 1]
                    val newSerialIndex = currentPageInfo.serialIndex.coerceIn(0 until prevPageInfoPeriod.count)
                    dailySetCurrentPageIndex.value = prevPageInfoPeriod.startIndex + newSerialIndex
                }
            }
        }

    }

    override fun toNext() {
        if (dailySetClazzAutoViewType.value == DailySetClazzAutoViewType.Week) {
            if (dailySetClazzAutoPageInfos.value.isNotEmpty() && dailySetCurrentPageIndex.value < dailySetClazzAutoPageInfos.value.size - 1) {
                // if the viewType is week, just increase 1 page.
                dailySetCurrentPageIndex.value = dailySetCurrentPageIndex.value + 1
            }
        } else {
            // if the viewType is term, will calculate the suitable page index.
            if (validPage()) {
                val dailySetClazzAutoPageInfoPeriods = dailySetClazzAutoPageInfos.value.toPageInfoPeriods()
                val currentPageInfo = currentPageInfo()
                val currentPageIndexPeriod = dailySetClazzAutoPageInfoPeriods.indexOfFirst {
                    it.year == currentPageInfo.year && it.periodCode == currentPageInfo.periodCode
                }
                if (currentPageIndexPeriod < dailySetClazzAutoPageInfoPeriods.size - 1) {
                    val nextPageInfoPeriod = dailySetClazzAutoPageInfoPeriods[currentPageIndexPeriod + 1]
                    val newSerialIndex = currentPageInfo.serialIndex.coerceIn(0 until nextPageInfoPeriod.count)
                    dailySetCurrentPageIndex.value = nextPageInfoPeriod.startIndex + newSerialIndex
                }
            }
        }
    }

    override fun toIndex(index: Int) {
        if (dailySetClazzAutoViewType.value == DailySetClazzAutoViewType.Week) {
            // if the viewType is week, just to indexedPage
            if (dailySetClazzAutoPageInfos.value.isNotEmpty() && index >= 0 && index <= dailySetClazzAutoPageInfos.value.size - 1) {
                dailySetCurrentPageIndex.value = index
            }
        } else {
            // if the viewType is term, will calculate the suitable page index.
            if (validPage()) {
                val dailySetClazzAutoPageInfoPeriods = dailySetClazzAutoPageInfos.value.toPageInfoPeriods()
                val currentPageInfo = currentPageInfo()
                if (index in dailySetClazzAutoPageInfoPeriods.indices) {
                    val targetPageInfoPeriod = dailySetClazzAutoPageInfoPeriods[index]
                    val newSerialIndex = currentPageInfo.serialIndex.coerceIn(0 until targetPageInfoPeriod.count)
                    dailySetCurrentPageIndex.value = targetPageInfoPeriod.startIndex + newSerialIndex
                }
            }
        }


    }

    override fun toNow() {
        dailySetCurrentPageIndex.value = dailySetClazzAutoPageInfos.value.calculateCurrentIndex()
    }

    private fun validPage(): Boolean {
        return dailySetClazzAutoPageInfos.value.isNotEmpty() && dailySetCurrentPageIndex.value in dailySetClazzAutoPageInfos.value.indices
    }

    private fun currentPageInfo(): DailySetClazzAutoPageInfo {
        return dailySetClazzAutoPageInfos.value[dailySetCurrentPageIndex.value]
    }

    private fun produceDailySetSummaryFlow(): Flow<DailySetSummary> {
        // TODO: 这里利用了Android Room流的原理来完成的功能，写法有点奇怪。
        val comp1 = sharedComponents.database.dailySetDao().anyFlow()
        val comp2 = sharedComponents.database.dailySetVisibleDao().anyFlow()
        val comp3 = sharedComponents.database.dailySetMetaLinksDao().anyFlow()
        val comp4 = sharedComponents.database.dailySetBasicMetaDao().anyFlow()

        return flowMulti(comp1, comp2, comp3, comp4) {
            return@flowMulti withContext(Dispatchers.IO) {
//                logger.d("DailySetClazzAutoVMImpl", "??")
                return@withContext sharedComponents.actorCollection.dailySetActor.getDailySetSummary(
                    dailySetUid = dailySetUid
                )!!
            }
        }
    }

    private fun produceDailySetDurationFlow(): Flow<List<DailySetDuration>> {
        val comp1 = sharedComponents.database.dailySetDao().anyFlow()
        val comp2 = sharedComponents.database.dailySetSourceLinksDao().anyFlow()
        val comp3 = sharedComponents.database.dailySetDurationDao().anyFlow()

        return flowMulti(comp1, comp2, comp3) {
            withContext(Dispatchers.IO) {
                sharedComponents.actorCollection.dailySetActor.getDailySetDurations(dailySetUid = dailySetUid)
            }
        }
    }

    private fun produceDailySetClazzAutoPagerInfosFlow(): Flow<List<DailySetClazzAutoPageInfo>> {
        return produceDailySetDurationFlow().map {
            val pagerInfos = DailySetClazzAutoPageInfo.fromDurations(it)
//            pagerInfos.forEach { info ->
//                logger.d("debug", info.toString())
//            }
            if (dailySetCurrentPageIndex.value == -1) {
                dailySetCurrentPageIndex.value = pagerInfos.calculateCurrentIndex()
            }
            pagerInfos
        }
    }

    private fun produceDailySetTRCFlow(): Flow<DailySetTRC> {
        val comp1 = sharedComponents.database.dailySetDao().anyFlow()
        val comp2 = sharedComponents.database.dailySetSourceLinksDao().anyFlow()
        val comp3 = sharedComponents.database.dailySetTableDao().anyFlow()
        val comp4 = sharedComponents.database.dailySetRowDao().anyFlow()
        val comp5 = sharedComponents.database.dailySetCellDao().anyFlow()

        return flowMulti(comp1, comp2, comp3, comp4, comp5) {
            withContext(Dispatchers.IO) {
                sharedComponents.actorCollection.dailySetActor.getDailySetTRC(dailySetUid = dailySetUid)
                    ?: DefaultEntities.emptyDailySetTRC()
            }
        }
    }

    // FIXME: 当pager处于学期（period）边界时，可能会出现显示与预期不一致的问题。
    private fun produceDailySetCoursesFlow(): Flow<List<DailySetCourse>> {
        val comp1 = sharedComponents.database.dailySetDao().anyFlow()
        val comp2 = sharedComponents.database.dailySetSourceLinksDao().anyFlow()
        val comp3 = sharedComponents.database.dailySetCourseDao().anyFlow()
        val comp4 = dailySetClazzAutoPageInfos.combine(dailySetCurrentPageIndex) { pageInfos, pageIndex ->
            if (pageInfos.isEmpty() || pageIndex < 0 || pageIndex >= pageInfos.size) {
                null
            } else {
                val pageInfo = pageInfos[pageIndex]
                YearPeriod(pageInfo.year, pageInfo.periodCode)
            }
        }.distinctUntilChanged()
        return flowMulti(
            comp1,
            comp2,
            comp3,
            comp4
        ) {
            val yearPeriod = it[3] as YearPeriod?
//            logger.d("debug", yearPeriod.toString())

//            val pageInfos = dailySetClazzAutoPageInfos.value
//            val pageIndex = dailySetCurrentPageIndex.value
            if (yearPeriod == null) {
                emptyList()
            } else {
                val courses = sharedComponents.actorCollection.dailySetActor.getDailySetCourses(
                    dailySetUid = dailySetUid,
                    year = yearPeriod.year,
                    periodCode = yearPeriod.periodCode
                )
//                for (course in courses) {
//                    logger.d("debug", course.toString())
//                }
                courses
            }
        }
    }
}