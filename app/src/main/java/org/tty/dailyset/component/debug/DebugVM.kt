package org.tty.dailyset.component.debug

import org.tty.dailyset.bean.entity.User
import org.tty.dailyset.common.observable.InitialFlow
import org.tty.dailyset.component.common.BaseVM
import java.time.DayOfWeek
import java.time.LocalDateTime

interface DebugVM: BaseVM {
    val seedVersion: InitialFlow<Int>
    val currentUserUid: InitialFlow<String>
    val currentUser: InitialFlow<User>
    val now: InitialFlow<LocalDateTime>
    val nowDayOfWeek: InitialFlow<DayOfWeek>
    val startDayOfWeek: InitialFlow<DayOfWeek>
}

