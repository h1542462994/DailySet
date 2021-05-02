package org.tty.dailyset.data.scope

import android.util.Log
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.liveData
import androidx.lifecycle.map
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

/**
 * a state related operation for [DailyTable], defines much functions return [State].
 */
@Immutable
interface DailyTableScope: PreferenceScope, UserScope  {

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
    fun currentDailyTableDetail(): State<DailyTRC> {
        val mainViewModel = mainViewModel()
        val trcLiveData by mainViewModel.currentDailyTRC.observeAsState(liveData { })
        return trcLiveData.map {
            it ?: DailyTRC.default()
        }.observeAsState(initial = DailyTRC.default())
//
//        return derivedStateOf {
//            trcLiveData.value ?: DailyTRC.default()
//        }
    }

    /**
     * the state of the DailyTable, see [calcDailyTableReadOnly],[calcDailyTableState]
     */
    @Composable
    fun dailyTableState(
        onDelete: (DailyTRC) -> Unit,
        onAddRow: (List<WeekDayState>) -> Unit
    ): DailyTableState {
        val dailyTRC by currentDailyTableDetail()
        val userState by currentUserState()

        val readOnly =
            calcDailyTableReadOnly(dailyTable = dailyTRC.dailyTable, userState = userState)
        return calcDailyTableState(dailyTRC = dailyTRC, readOnly = readOnly, onDelete = onDelete, onAddRow = onAddRow)
    }

    fun calcDailyTableReadOnly(dailyTable: DailyTable, userState: UserState): Boolean {
        return dailyTable.global || dailyTable.referenceUid != userState.currentUserUid;
    }

    /**
     * calculate the state for DailyTable
     * @param dailyTRC dailyTRC
     * @param readOnly dailyTRC is readOnly
     * @param onDelete operation onDelete, invoke by button.click
     */
    fun calcDailyTableState(
        dailyTRC: DailyTRC,
        readOnly: Boolean,
        onDelete: (DailyTRC) -> Unit,
        onAddRow: (List<WeekDayState>) -> Unit
    ): DailyTableState {
        /**
         * transfer the weekDays from db to listState (readOnly)
         * @param array weekDays form db
         */
        fun intArrayToReadOnlyState(array: IntArray): List<WeekDayState> {
            return (1..7).map {
                if (array.contains(it)) {
                    WeekDayState(readOnly = true, checked = true)
                } else {
                    WeekDayState(readOnly = true, checked = false)
                }
            }
        }

        /**
         * transfer the weekDays from db to listState (mutable)
         * @param array weekDays from db.
         * @param mutableList recorded mutable selections.
         */
        fun intArrayToMutableState(array: IntArray, mutableList: List<Int>): List<WeekDayState> {
            return (1..7).map {
                val checked = array.contains(it)
                val rl = mutableList.contains(it)
                WeekDayState(readOnly = rl, checked = checked)
            }
        }


        val dailyTableState = DailyTableState(dailyTRC = dailyTRC, readOnly = readOnly, canAddRow = false, onDelete = onDelete, onAddRow = onAddRow)

        if (readOnly) {
            dailyTableState.dailyTableRowStateList.forEach { dailyTableRowState ->
                dailyTableRowState.weekDays.clear()
                dailyTableRowState.weekDays.addAll(intArrayToReadOnlyState(dailyTableRowState.dailyRC.dailyRow.weekdays))
            }
        } else {

            val length = dailyTableState.dailyTableRowStateList.size
            val mutableRecordList = mutableListOf<Int>()
            var index = length -1
            while (index >= 0) {
                val current = dailyTableState.dailyTableRowStateList[index]
                // if the weekDay has only one option, it's will be recorded.
                if (current.dailyRC.dailyRow.weekdays.size > 1) {
                    mutableRecordList.addAll(current.dailyRC.dailyRow.weekdays.toList())
                    // if the last has several options, it can add row from it.
                    if (index == length - 1) {
                        dailyTableState.canAddRow = true
                    }
                }
                if (index == length - 1) {
                    // last is readOnly
                    current.weekDays.clear()
                    current.weekDays.addAll(intArrayToReadOnlyState(current.dailyRC.dailyRow.weekdays))
                    dailyTableState.addRowState = DailyTableAddRowState(mutableStateOf(false), onAddRow = onAddRow)
                    dailyTableState.addRowState.lastState.addAll((1..7).map {
                        if (current.dailyRC.dailyRow.weekdays.contains(it)) {
                            WeekDayState(readOnly = false, checked = false)
                        } else {
                            WeekDayState(readOnly = true, checked = false)
                        }
                    })

                } else {
                    current.weekDays.clear()
                    current.weekDays.addAll(intArrayToMutableState(current.dailyRC.dailyRow.weekdays, mutableRecordList))
                }
                index--
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
    fun dailyTableCreateState(
        initialName: String,
        dialogOpen: Boolean = false,
        onCreate: (String, DailyTable) -> Unit
    ): DailyTableCreateState {
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

    /**
     * create the DailyTable from the template.
     * db related function, ensure call in [kotlinx.coroutines.CoroutineScope].
     * @param service the application service, see also [org.tty.dailyset.provider.LocalServices]
     * @param currentUserUid current user uid
     * @param name displayed name for dailyTable
     * @param cloneFrom which dailyTable as the template
     * @param uid optional, the uid for this dailyTable, if not assigned, it will be auto generated by [UUID.randomUUID]
     * @param onCompletion operation after success, it will deliver the dailyTable's uid
     */
    fun dailyTableCreateFromTemplate(service: DailySetApplication, currentUserUid: String, name: String, cloneFrom: DailyTable, uid: String? = null, onCompletion: (String) -> Unit){
        val realUid: String = uid ?: UUID.randomUUID().toString()
        Log.d(TAG, "create DailyTable uid=${realUid},name=${name}")
        // TODO: 2021/5/2 这很可能导致内存泄漏，需要找到和应用绑定的CoroutineScope
        val job = GlobalScope.launch {
           service.dailyTableRepository.createFromTemplate(name, cloneFrom, realUid, referenceUid = currentUserUid)
        }
        job.invokeOnCompletion {
            onCompletion(realUid)
        }
    }

    /**
     * delete the DailyTable
     * db related function, ensure call in [kotlinx.coroutines.CoroutineScope]
     * @param service the application service, see also [org.tty.dailyset.provider.LocalServices]
     * @param dailyTRC the deleted dailyTable, and more data
     * @param onBefore the action before the operation, ensure change current dailyTable
     */
    fun dailyTableDelete(service: DailySetApplication, dailyTRC: DailyTRC, onBefore: () -> Unit) {
        Log.d(TAG, "delete DailyTable, uid=${dailyTRC.dailyTable.uid},name=${dailyTRC.dailyTable.name}")
        onBefore()
        val job = GlobalScope.launch {
            service.dailyTableRepository.delete(dailyTRC = dailyTRC)
        }
    }

    /**
     * addRow from the DailyTable
     * db related function, ensure call in [kotlinx.coroutines.CoroutineScope]
     * @param service the application service, see also [org.tty.dailyset.provider.LocalServices]
     * @param dailyTRC the deleted dailyTable, and more data
     * @param onCompletion operation after success
     */
    fun dailyTableAddRow(service: DailySetApplication, dailyTRC: DailyTRC, weekDays: IntArray, onCompletion: () -> Unit) {
        Log.d(TAG, "addRow DailyTable, uid=${dailyTRC.dailyTable.uid},name=${dailyTRC.dailyTable.name}")
        val job = GlobalScope.launch {
            service.dailyTableRepository.addRow(dailyTRC = dailyTRC, weekDays = weekDays)
        }
        job.invokeOnCompletion {
            onCompletion()
        }
    }

    fun groupDailyCells(list: List<DailyCell>): Map<Int, List<DailyCell>> {
        return list.groupBy { it.normalType }
    }

    companion object {
        private const val TAG = "DailyTableScope"
    }
}


