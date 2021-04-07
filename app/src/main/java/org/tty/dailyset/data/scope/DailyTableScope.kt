package org.tty.dailyset.data.scope

import android.annotation.SuppressLint
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
import org.tty.dailyset.model.lifetime.DailyTableCreateState
import org.tty.dailyset.model.lifetime.DailyTablePreviewState
import org.tty.dailyset.model.lifetime.DailyTableState
import org.tty.dailyset.model.lifetime.WeekDayState
import org.tty.dailyset.ui.utils.toWeekStart
import org.tty.dailyset.viewmodel.MainViewModel
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

    /**
     * return the snapshot of the current weekDayState
     */
    fun calcWeekDayState(dailyTRC: DailyTRC, index: Int): List<WeekDayState> {
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
        return list;
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


