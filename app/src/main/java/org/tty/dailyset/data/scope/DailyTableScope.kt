package org.tty.dailyset.data.scope

import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.lifecycle.liveData
import org.tty.dailyset.MainViewModel
import org.tty.dailyset.model.entity.DailyCell
import org.tty.dailyset.model.entity.DailyRow
import org.tty.dailyset.model.entity.DailyTRC
import org.tty.dailyset.model.entity.DailyTable
import org.tty.dailyset.provider.LocalMainViewModel

@Composable
fun mainViewModel(): MainViewModel {
    return LocalMainViewModel.current
}

@Composable
fun dailyTableSummaries(): State<List<DailyTable>> {
    return mainViewModel().dailyTableSummaries.observeAsState(listOf())
}

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