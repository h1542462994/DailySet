package org.tty.dailyset.component.debug

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.tty.dailyset.annotation.UseComponent
import org.tty.dailyset.bean.entity.User
import org.tty.dailyset.bean.enums.PreferenceName
import org.tty.dailyset.component.common.SharedComponents
import org.tty.dailyset.component.common.asActivityColdStateFlow
import org.tty.dailyset.component.common.sharedComponents
import java.time.DayOfWeek
import java.time.LocalDateTime



@Composable
@UseComponent
fun rememberDebugVM(): DebugVM {
    val sharedComponents = sharedComponents()
    return sharedComponents.ltsVMSaver.getVM("debugVM") {
        DebugVMImpl(sharedComponents)
    }

//    return remember { DebugVMImpl(sharedComponents)  }
}

class DebugVMImpl(private val sharedComponents: SharedComponents): DebugVM {
    override val seedVersion: StateFlow<Int> = sharedComponents.stateStore.seedVersion.asActivityColdStateFlow(0)
    override val currentUserUid: StateFlow<String> = sharedComponents.stateStore.currentUserUid.asActivityColdStateFlow(User.local)
    override val currentUser: StateFlow<User> = sharedComponents.stateStore.currentUser.asActivityColdStateFlow(User.default())
    override val now: StateFlow<LocalDateTime> = sharedComponents.stateStore.now.asActivityColdStateFlow(LocalDateTime.now())
    override val nowDayOfWeek: StateFlow<DayOfWeek> = sharedComponents.stateStore.nowDayOfWeek.asActivityColdStateFlow(LocalDateTime.now().dayOfWeek)
    override val startDayOfWeek: StateFlow<DayOfWeek> = sharedComponents.stateStore.startDayOfWeek.asActivityColdStateFlow(DayOfWeek.MONDAY)
    override val users: StateFlow<List<User>> = sharedComponents.stateStore.users.asActivityColdStateFlow(
        listOf())
    override val currentHttpServerAddress: StateFlow<String> = sharedComponents.stateStore.currentHttpServerAddress.asActivityColdStateFlow("")
    override val deviceCode: StateFlow<String> = sharedComponents.stateStore.deviceCode

    override fun setFirstLoadUser(value: Boolean) {
        sharedComponents.applicationScope.launch {
            sharedComponents.repositoryCollection.preferenceRepository.save(PreferenceName.FIRST_LOAD_USER, value)
        }
    }
}