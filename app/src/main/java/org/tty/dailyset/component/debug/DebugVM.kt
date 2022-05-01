package org.tty.dailyset.component.debug

import kotlinx.coroutines.flow.StateFlow
import org.tty.dailyset.bean.entity.User
import org.tty.dailyset.component.common.BaseVM
import java.time.DayOfWeek
import java.time.LocalDateTime

interface DebugVM: BaseVM {
    val seedVersion: StateFlow<Int>
    val currentUserUid: StateFlow<String>
    val currentUser: StateFlow<User>
    val now: StateFlow<LocalDateTime>
    val nowDayOfWeek: StateFlow<DayOfWeek>
    val startDayOfWeek: StateFlow<DayOfWeek>
    val users: StateFlow<List<User>>
    val currentHttpServerAddress: StateFlow<String>
    val deviceCode: StateFlow<String>

    fun setFirstLoadUser(value: Boolean)
}

