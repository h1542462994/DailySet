package org.tty.dailyset.model.lifetime

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import org.tty.dailyset.model.entity.DailyTable

/**
 * state used for [org.tty.dailyset.ui.page.DailyTableCreateDialogCover]
 */
class DailyTableCreateState(
    override val dialogOpen: MutableState<Boolean>,
    val name: MutableState<String>,
    val dailyTableSummaries: State<List<DailyTable>>,
    val currentDailyTable: MutableState<DailyTable>,
    /**
     * operation after create DailyTable successfully.
     * it will be call on backThread.
     */
    val onCreate: (String) -> Unit
): DialogState(dialogOpen = dialogOpen)