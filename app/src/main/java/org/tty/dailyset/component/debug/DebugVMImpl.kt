package org.tty.dailyset.component.debug

import androidx.compose.runtime.Composable
import kotlinx.coroutines.flow.StateFlow
import org.tty.dailyset.bean.entity.User
import org.tty.dailyset.common.observable.InitialFlow
import org.tty.dailyset.common.observable.initial
import org.tty.dailyset.component.common.asActivityColdStateFlow
import org.tty.dailyset.component.common.sharedComponents
import java.time.DayOfWeek
import java.time.LocalDateTime

@Composable
fun debugVM(): DebugVM {
    val sharedComponents = sharedComponents()

    return object: DebugVM {
        override val seedVersion: StateFlow<Int> = sharedComponents.stateStore.seedVersion.asActivityColdStateFlow(0)
        override val currentUserUid: InitialFlow<String>
            get() = sharedComponents.stateStore.currentUserUid.initial(User.local)
        override val currentUser: InitialFlow<User>
            get() = sharedComponents.stateStore.currentUser.initial(User.default())
        override val now: InitialFlow<LocalDateTime>
            get() = sharedComponents.stateStore.now.initial(LocalDateTime.now())
        override val nowDayOfWeek: InitialFlow<DayOfWeek>
            get() = sharedComponents.stateStore.nowDayOfWeek.initial(LocalDateTime.now().dayOfWeek)
        override val startDayOfWeek: InitialFlow<DayOfWeek>
            get() = sharedComponents.stateStore.startDayOfWeek.initial(DayOfWeek.MONDAY)
    }
}