package org.tty.dailyset.component.dailyset.clazzauto

import kotlinx.coroutines.flow.MutableStateFlow
import org.tty.dailyset.bean.enums.DailySetIcon
import org.tty.dailyset.bean.lifetime.DailySetSummary
import org.tty.dailyset.component.common.DialogVM

interface DailySetRenameDialogVM: DialogVM {
    /**
     * whether select icon panel is open.
     */
    val selectIcon: MutableStateFlow<Boolean>

    val icon: MutableStateFlow<DailySetIcon?>

    val name: MutableStateFlow<String>

    /**
     * modify the dialogState from **immutable** dailySetSummary.
     */
    fun cloneFrom(dailySetSummary: DailySetSummary)
}