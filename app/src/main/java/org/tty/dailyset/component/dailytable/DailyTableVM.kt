package org.tty.dailyset.component.dailytable

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import org.tty.dailyset.bean.entity.DailyTRC
import org.tty.dailyset.bean.entity.DailyTable
import org.tty.dailyset.bean.entity.User
import org.tty.dailyset.bean.lifetime.dailytable.DailyTableState2

interface DailyTableVM {
    val dropDownTitleOpen: MutableStateFlow<Boolean>
    val dropDownExtensionOpen: MutableStateFlow<Boolean>
    val dailyTableUid: MutableStateFlow<String>
    val dailyTables: StateFlow<List<DailyTable>>
    val dailyTRC: StateFlow<DailyTRC>
    val currentUser: StateFlow<User>
    val dailyTableState2: StateFlow<DailyTableState2>
}