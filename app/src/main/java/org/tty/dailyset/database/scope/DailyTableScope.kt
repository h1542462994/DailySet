package org.tty.dailyset.database.scope

/*
import androidx.compose.runtime.*
import org.tty.dailyset.common.datetime.DateSpan
import org.tty.dailyset.common.local.Tags
import org.tty.dailyset.common.local.logger
import org.tty.dailyset.common.observable.liveDataExtension
import org.tty.dailyset.common.observable.state
import org.tty.dailyset.bean.entity.DailyCell
import org.tty.dailyset.bean.entity.DailyTRC
import org.tty.dailyset.bean.entity.DailyTable
import org.tty.dailyset.bean.lifetime.dailytable.*
import java.time.LocalTime
import org.tty.dailyset.provider.mainViewModel as vm

*/
/**
 * a state related operation for [DailyTable], defines much functions return [State].
 *//*

@Immutable
@Deprecated("instead with component VM.")
interface DailyTableScope : PreferenceScope, UserScope {
    */
/**
     * state of [org.tty.dailyset.viewmodel.MainViewModel.dailyTableSummaries]
     * (db related).
     * if not initialized, return [listOf].
     *//*

    @Composable
    fun dailyTableSummaries(): State<List<DailyTable>> {
        return state(vm.dailyTableSummaries)
    }

    @Composable
    fun dailyTableState2(): State<DailyTableState2> {
        return state(vm.dailyTableState2)
    }

    @Composable
    fun dailyTableUid(): MutableState<String> {
        return state(vm.dailyTableUid)
    }


    @Composable
    fun dailyTablePreviewState(): DailyTablePreviewState {
        val nowDate by nowDate()
        val startWeekDay by startWeekDay()
        val currentWeekDay = state(value = nowDate.dayOfWeek, key1 = nowDate)
        logger.d(Tags.liveDataExtension, "nowDate = $nowDate")

        return DailyTablePreviewState(
            dateSpan = DateSpan.ofDate(nowDate, startWeekDay),
            nowDate(),
            currentWeekDay,
            startWeekDay()
        )
    }

    @Composable
    fun dailyTableCreateState(
        initialName: String = "",
        dialogOpen: Boolean = false
    ): DailyTableCreateState {
        val currentDailyTable = dailyTableState2().value.dailyTRC.dailyTable

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
                        start = LocalTime.parse("08:00:00"),
                        end = LocalTime.parse("08:45:00")
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


*/
