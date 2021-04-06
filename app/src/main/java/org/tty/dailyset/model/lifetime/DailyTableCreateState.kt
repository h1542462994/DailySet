package org.tty.dailyset.model.lifetime

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import org.tty.dailyset.model.entity.DailyTable

class DailyTableCreateState(
    override val dialogOpen: MutableState<Boolean>,
    val name: MutableState<String>,
    val dailyTableSummaries: State<List<DailyTable>>,
    val currentDailyTable: MutableState<DailyTable>,
    val onCreate: () -> Unit
): DialogState(dialogOpen = dialogOpen)