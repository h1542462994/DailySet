package org.tty.dailyset.component.debug

import androidx.compose.runtime.Composable
import kotlinx.coroutines.flow.Flow
import org.tty.dailyset.bean.entity.User
import org.tty.dailyset.common.observable.InitialFlow
import org.tty.dailyset.common.observable.initial
import org.tty.dailyset.provider.LocalServices
import java.time.DayOfWeek
import java.time.LocalDateTime

@Composable
fun debugVM(): DebugVM {
    val sharedComponents = LocalServices.current

    return object: DebugVM {
        override val seedVersion: InitialFlow<Int>
            get() = sharedComponents.stateStore.seedVersion.initial(0)
        override val currentUserUid: InitialFlow<String>
            get() = sharedComponents.stateStore.currentUserUid.initial(User.local)
        override val now: InitialFlow<LocalDateTime>
            get() = sharedComponents.stateStore.now.initial(LocalDateTime.now())
        override val nowDayOfWeek: InitialFlow<DayOfWeek>
            get() = sharedComponents.stateStore.nowDayOfWeek.initial(LocalDateTime.now().dayOfWeek)
        override val startDayOfWeek: InitialFlow<DayOfWeek>
            get() = sharedComponents.stateStore.startDayOfWeek.initial(DayOfWeek.MONDAY)
    }
}