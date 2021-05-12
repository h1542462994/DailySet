package org.tty.dailyset.data.scope

import android.util.Log
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.lifecycle.liveData
import androidx.lifecycle.map
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.tty.dailyset.DailySetApplication
import org.tty.dailyset.data.processor.DailyTableProcessor2Async
import org.tty.dailyset.data.processor.EventProcessorCallBack
import org.tty.dailyset.event.DailyTableAddRowEventArgs
import org.tty.dailyset.model.entity.DailyCell
import org.tty.dailyset.model.entity.DailyTRC
import org.tty.dailyset.model.entity.DailyTable
import org.tty.dailyset.model.lifetime.*
import org.tty.dailyset.ui.utils.toWeekStart
import java.time.LocalDate
import java.util.*

/**
 * a state related operation for [DailyTable], defines much functions return [State].
 */
@Immutable
interface DailyTableScope: PreferenceScope, UserScope {

    // TODO: 2021/5/11 不要在State内存放内部复杂状态。

    /**
     * state of [org.tty.dailyset.viewmodel.MainViewModel.dailyTableSummaries]
     * (db related).
     * if not initialized, return [listOf].
     */
    @Composable
    fun dailyTableSummaries(): State<List<DailyTable>> {
        return mainViewModel().dailyTableSummaries.observeAsState(listOf())
    }

    /**
     * state of [dailyTableSummaries] and [org.tty.dailyset.viewmodel.MainViewModel.currentDailyTableUid]
     * if not initialized, return [DailyTable.default]
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

    /**
     * state of [org.tty.dailyset.viewmodel.MainViewModel.currentDailyTRC]
     * (temp)
     */
    @Composable
    fun currentDailyTableDetail(): State<DailyTRC> {
        val mainViewModel = mainViewModel()
        val trcLiveData by mainViewModel.currentDailyTRC.observeAsState(liveData { })
        return trcLiveData.map {
            it ?: DailyTRC.default()
        }.observeAsState(initial = DailyTRC.default())
    }

    @Composable
    fun dailyTableState2(): State<DailyTableState2> {
        val mainViewModel = mainViewModel()
        return mainViewModel.currentDailyTableState2.observeAsState(DailyTableState2.default())
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

    @Composable
    fun dailyTableCreateState(
        initialName: String = "",
        dialogOpen: Boolean = false
    ): DailyTableCreateState {
        val currentDailyTable = currentDailyTable().value

        return DailyTableCreateState(
            dialogOpen = mutableStateOf(remember { dialogOpen }),
            name = mutableStateOf(remember { initialName }),
            dailyTableSummaries = dailyTableSummaries(),
            currentDailyTable = remember {
                mutableStateOf(currentDailyTable)
            }
        )
    }

    @Composable
    fun dailyTableDeleteState(
        dialogOpen: Boolean = false
    ): DailyTableDeleteState {
        return DailyTableDeleteState(
            mutableStateOf(remember {
                dialogOpen
            })
        )
    }

    @Composable
    fun dailyTableAddRowState(
        dialogOpen: Boolean = false
    ): DailyTableAddRowState {
        val dailyTableState2 by dailyTableState2()
        val listState by remember {
            derivedStateOf {
                mutableStateOf(dailyTableState2.initialAddRowWeekDays)
            }
        }

        return DailyTableAddRowState(
            remember {
                mutableStateOf(dialogOpen)
            },
            dailyTableState2.initialAddRowWeekDays,
            listState
        )
    }

    @Composable
    fun dailyTableRenameState(
        initialName: String = "",
        dialogOpen: Boolean = false
    ): DailyTableRenameState {
        return DailyTableRenameState(
            dialogOpen = remember {
                mutableStateOf(dialogOpen)
            },
            name = remember {
                mutableStateOf(initialName)
            },
            dailyTable = dailyTableState2().value.dailyTRC.dailyTable
        )
    }

    fun groupDailyCells(list: List<DailyCell>): Map<Int, List<DailyCell>> {
        return list.groupBy { it.normalType }
    }

    companion object {
        private const val TAG = "DailyTableScope"
    }
}


