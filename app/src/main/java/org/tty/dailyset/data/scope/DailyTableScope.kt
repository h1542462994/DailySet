package org.tty.dailyset.data.scope

import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.lifecycle.liveData
import mainViewModel
import org.tty.dailyset.model.entity.DailyCell
import org.tty.dailyset.model.entity.DailyTRC
import org.tty.dailyset.model.entity.DailyTable
import org.tty.dailyset.ui.utils.toWeekStart
import java.time.LocalDate

/**
 * A complex state for DailyTablePreviewPage
 */
data class DailyTablePreviewState(
    /**
     * currentDate, means now.
     */
    val startDate: LocalDate,
    /**
     * the start date of this week.
     */
    val weekDayNow: Int,
    private val _weedDayCurrent: MutableState<Int>
){
    /**
     * the selected weekDay, it's handled by state.
     */
    val weekDayCurrent by _weedDayCurrent
    val setWeekDayCurrent: (Int) -> Unit = { value ->
        _weedDayCurrent.value = value
    }
}


/**
 * get all dailyTables in database.
 */
@Composable
fun dailyTableSummaries(): State<List<DailyTable>> {
    return mainViewModel().dailyTableSummaries.observeAsState(listOf())
}

/**
 * get currentDailyTable for DailyTablePage
 */
@Composable
fun currentDailyTable(): State<DailyTable> {
    val mainViewModel = mainViewModel()
    val dailyTableSummaries by dailyTableSummaries()
    val currentDailyTableUid by mainViewModel.currentDailyTableUid.observeAsState(DailyTable.default)
    return derivedStateOf {
        dailyTableSummaries.find { it.uid == currentDailyTableUid } ?: DailyTable.default()
    }
}

@Composable
fun currentDailyTableDetail(): State<DailyTRC?> {
    val mainViewModel = mainViewModel()
    val trcLiveData by mainViewModel.currentDailyTRC.observeAsState(liveData { })
    return trcLiveData.observeAsState()
}


fun groupDailyCells(list: List<DailyCell>): Map<Int, List<DailyCell>> {
    return list.groupBy { it.normalType }
}

@Composable
fun dailyTablePreviewState(): DailyTablePreviewState {
    // TODO: 2021/3/29 添加对时间的依赖
    // TODO: 2021/3/29 当前仅支持周一开始，需要进行扩展

    val current = LocalDate.now()


    val start = current.toWeekStart()
    val weekDayNow = current.dayOfWeek.value

    return DailyTablePreviewState(
        startDate = start,
        weekDayNow = weekDayNow,
        _weedDayCurrent = remember {
            mutableStateOf(weekDayNow)
        }
    )
}
