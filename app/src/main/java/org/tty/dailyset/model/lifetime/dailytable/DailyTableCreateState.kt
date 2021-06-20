package org.tty.dailyset.model.lifetime.dailytable

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import org.tty.dailyset.model.entity.DailyTable
import org.tty.dailyset.model.lifetime.DialogState

/**
 * state used for [org.tty.dailyset.ui.page.DailyTableCreateDialogCover]
 */
class DailyTableCreateState(
    override val dialogOpen: MutableState<Boolean>,
    val name: MutableState<String>,
    val dailyTableSummaries: State<List<DailyTable>>,
    val currentDailyTable: MutableState<DailyTable>,

): DialogState(dialogOpen = dialogOpen)