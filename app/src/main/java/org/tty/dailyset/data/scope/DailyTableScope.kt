package org.tty.dailyset.data.scope

import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.lifecycle.liveData
import androidx.lifecycle.map
import org.tty.dailyset.common.observable.state
import org.tty.dailyset.model.entity.DailyCell
import org.tty.dailyset.model.entity.DailyTRC
import org.tty.dailyset.model.entity.DailyTable
import org.tty.dailyset.model.lifetime.dailytable.*
import org.tty.dailyset.toWeekStart
import java.sql.Time
import java.time.LocalDate

/**
 * a state related operation for [DailyTable], defines much functions return [State].
 */
@Immutable
interface DailyTableScope : PreferenceScope, UserScope {

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
        return state(mainViewModel.currentDailyTRCEnd)
    }

    @Composable
    fun dailyTableState2(): State<DailyTableState2> {
        val mainViewModel = mainViewModel()
        return state(mainViewModel.currentDailyTableState2End)
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
            dialogOpen = state(dialogOpen),
            name = state(initialName),
            dailyTableSummaries = dailyTableSummaries(),
            currentDailyTable = state(currentDailyTable)
        )
    }

    @Composable
    fun dailyTableDeleteState(
        dialogOpen: Boolean = false
    ): DailyTableDeleteState {
        return DailyTableDeleteState(
            state(dialogOpen)
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

    @Composable
    fun dailyTableDeleteRowState(
        dialogOpen: Boolean = false,
        rowIndex: Int = 0
    ): DailyTableDeleteRowState {
        return DailyTableDeleteRowState(
            dialogOpen = remember {
                mutableStateOf(dialogOpen)
            },
            rowIndex = remember {
                mutableStateOf(rowIndex)
            }
        )
    }

    fun groupDailyCells(list: List<DailyCell>): Map<Int, List<DailyCell>> {
        return list.groupBy { it.normalType }
    }

    @Composable
    fun dailyTableModifySectionState(
        dialogOpen: Boolean = false,
        counts: IntArray = intArrayOf(5,4,3),
        rowIndex: Int = 0
    ): DailyTableModifySectionState {
        return DailyTableModifySectionState(
            dialogOpen = remember {
                mutableStateOf(dialogOpen)
            },
            counts = remember {
                mutableStateOf(counts)
            },
            rowIndex = remember {
                mutableStateOf(rowIndex)
            }
        )
    }

    @Composable
    fun dailyTableModifyCellState(
        dialogOpen: Boolean = false
    ): DailyTableModifyCellState {
        return DailyTableModifyCellState(
            dialogOpen = remember {
                mutableStateOf(dialogOpen)
            },
            modifyCellStateWrap = remember {
                mutableStateOf(
                    ModifyCellStateWrap(
                        rowIndex = 0,
                        cellIndex = 0,
                        min = null,
                        start = Time.valueOf("08:00:00"),
                        end = Time.valueOf("08:45:00")
                )
                )
            }
        )
    }

    fun upgradeDailyTableModifyCellState(state: DailyTableModifyCellState, dailyTRC: DailyTRC, rowIndex: Int, cellIndex: Int) {
        fun cell(rowIndex: Int, cellIndex: Int) = dailyTRC.dailyRCs[rowIndex].dailyCells[cellIndex]

        state.modifyCellStateWrap.value = ModifyCellStateWrap(
            rowIndex = rowIndex,
            cellIndex = cellIndex,
            min = if (cellIndex == 0) null else cell(rowIndex, cellIndex - 1).end,
            start = cell(rowIndex, cellIndex).start,
            end = cell(rowIndex, cellIndex).end
        )
    }

    companion object {
        private const val TAG = "DailyTableScope"
    }
}


