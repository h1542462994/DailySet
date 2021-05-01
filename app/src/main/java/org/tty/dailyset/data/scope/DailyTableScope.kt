package org.tty.dailyset.data.scope

import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.liveData
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.tty.dailyset.DailySetApplication
import org.tty.dailyset.model.entity.DailyCell
import org.tty.dailyset.model.entity.DailyTRC
import org.tty.dailyset.model.entity.DailyTable
import org.tty.dailyset.model.lifetime.*
import org.tty.dailyset.ui.utils.toWeekStart
import java.time.LocalDate
import java.util.*
import kotlin.collections.ArrayList

@Immutable
interface DailyTableScope: PreferenceScope  {
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



    fun calcDailyTableReadOnly(dailyTable: DailyTable, userState: UserState): Boolean {
        return dailyTable.global || dailyTable.referenceUid != userState.currentUserUid;
    }

    fun calcDailyTableState(dailyTRC: DailyTRC, readOnly: Boolean): DailyTableState {
        fun intArrayToReadOnlyState(array: IntArray): List<WeekDayState> {
            return (1..7).map {
                if (array.contains(it)) {
                    WeekDayState(readOnly = true, checked = true)
                } else {
                    WeekDayState(readOnly = true, checked = false)
                }
            }
        }

        fun intArrayToMutableState(array: IntArray, mutableList: List<Int>): List<WeekDayState> {
            return (1..7).map {
                val checked = array.contains(it)
                val rl = mutableList.contains(it)
                WeekDayState(readOnly = rl, checked = checked)
            }
        }


        val dailyTableState = DailyTableState(dailyTRC = dailyTRC, readOnly = readOnly, false)
        if (readOnly) {
            dailyTableState.dailyTableRowStateList.forEach { dailyTableRowState ->
                dailyTableRowState.weekDays = intArrayToReadOnlyState(dailyTableRowState.dailyRC.dailyRow.weekdays)
            }
        } else {
            val length = dailyTableState.dailyTableRowStateList.size
            val mutableRecordList = mutableListOf<Int>()
            for (i in length-1..0) {
                val current = dailyTableState.dailyTableRowStateList[i]
                // if the weekDay has only one option, it's will be recorded.
                if (current.dailyRC.dailyRow.weekdays.size > 1) {
                    mutableRecordList.addAll(current.dailyRC.dailyRow.weekdays.toList())
                    // if the last has several options, it can add row from it.
                    if (i == length - 1) {
                        dailyTableState.canAddRow = true
                    }
                }
                if (i == length - 1) {
                    // last is readOnly
                    current.weekDays = intArrayToReadOnlyState(current.dailyRC.dailyRow.weekdays)
                    dailyTableState.addRowState = DailyTableAddRowState(mutableStateOf(false))
                    dailyTableState.lastState.addAll((1..7).map {
                        if (current.dailyRC.dailyRow.weekdays.contains(it)) {
                            WeekDayState(readOnly = false, checked = false)
                        } else {
                            WeekDayState(readOnly = true, checked = false)
                        }
                    })

                } else {
                    current.weekDays = intArrayToMutableState(current.dailyRC.dailyRow.weekdays, mutableRecordList)
                }
            }
        }

        return dailyTableState
    }


    /**
     * return the snapshot of the current weekDayState
     */
    @Deprecated("use dailyTableState instead.")
    fun calcWeekDayState(dailyTRC: DailyTRC, index: Int, readOnly: Boolean): List<WeekDayState> {
        val dailyRowCount = dailyTRC.dailyRCs.count()
        val currentWeekDays = dailyTRC.dailyRCs[index].dailyRow.weekdays
        val list = ArrayList<WeekDayState>()

        // TODO: 2021/4/7 完成复杂的实现。
        (1 .. 7).forEach { _index ->
            if (currentWeekDays.contains(_index)){
                list.add(WeekDayState(readOnly = false, checked = true))
            } else {
                list.add(WeekDayState(readOnly = false, checked = false))
            }
        }
        return list
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
    fun dailyTableCreateState(initialName: String, dialogOpen: Boolean = false, onCreate: () -> Unit): DailyTableCreateState {
        val currentDailyTable = currentDailyTable().value

        return DailyTableCreateState(
            dialogOpen = mutableStateOf(remember { dialogOpen }),
            name = mutableStateOf(remember { initialName }),
            dailyTableSummaries = dailyTableSummaries(),
            currentDailyTable = remember {
                mutableStateOf(currentDailyTable)
            },
            onCreate = onCreate
        )
    }

    fun dailyTableCreateFromTemplate(service: DailySetApplication, currentUserUid: String, name: String, cloneFrom: DailyTable, uid: String? = null, onCompletion: () -> Unit){
        val realUid: String = uid ?: UUID.randomUUID().toString()
        val job = GlobalScope.launch {
           service.dailyTableRepository.createFromTemplate(name, cloneFrom, realUid, referenceUid = currentUserUid)
        }
        job.invokeOnCompletion {
            onCompletion()
        }
    }



    fun groupDailyCells(list: List<DailyCell>): Map<Int, List<DailyCell>> {
        return list.groupBy { it.normalType }
    }
}


