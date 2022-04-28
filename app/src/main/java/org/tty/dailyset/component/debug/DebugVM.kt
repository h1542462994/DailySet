package org.tty.dailyset.component.debug

import org.tty.dailyset.common.observable.InitialFlow
import java.time.DayOfWeek
import java.time.LocalDateTime

interface DebugVM {
    val seedVersion: InitialFlow<Int>
    val currentUserUid: InitialFlow<String>
    val now: InitialFlow<LocalDateTime>
    val nowDayOfWeek: InitialFlow<DayOfWeek>
    val startDayOfWeek: InitialFlow<DayOfWeek>
}

