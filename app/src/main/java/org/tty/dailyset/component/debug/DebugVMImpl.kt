package org.tty.dailyset.component.debug

import androidx.compose.runtime.Composable
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.tty.dailyset.annotation.UseComponent
import org.tty.dailyset.bean.entity.EntityDefaults
import org.tty.dailyset.bean.entity.User
import org.tty.dailyset.bean.enums.PreferenceName
import org.tty.dailyset.component.common.SharedComponents
import org.tty.dailyset.component.common.asAppStateFlow
import org.tty.dailyset.component.common.sharedComponents
import java.time.DayOfWeek
import java.time.LocalDateTime



@Composable
@UseComponent
fun rememberDebugVM(): DebugVM {
    val sharedComponents = sharedComponents()
    // FIXME: 这里的代码主要是为了将VM与Application的生命周期绑定，从而防止数据显示异常或者丢失的情况。
    return sharedComponents.viewModelStore.getVM("debugVM") {
        DebugVMImpl(sharedComponents)
    }

//    return remember { DebugVMImpl(sharedComponents)  }
}

class DebugVMImpl(private val sharedComponents: SharedComponents): DebugVM {
    override val seedVersion: StateFlow<Int> = sharedComponents.stateStore.seedVersion.asAppStateFlow(0)
    override val currentUserUid: StateFlow<String> = sharedComponents.stateStore.currentUserUid.asAppStateFlow("")
    override val currentUser: StateFlow<User> = sharedComponents.stateStore.currentUser.asAppStateFlow(EntityDefaults.emptyUser())
    override val now: StateFlow<LocalDateTime> = sharedComponents.stateStore.now.asAppStateFlow(LocalDateTime.now())
    override val nowDayOfWeek: StateFlow<DayOfWeek> = sharedComponents.stateStore.nowDayOfWeek.asAppStateFlow(LocalDateTime.now().dayOfWeek)
    override val startDayOfWeek: StateFlow<DayOfWeek> = sharedComponents.stateStore.startDayOfWeek.asAppStateFlow(DayOfWeek.MONDAY)
    override val users: StateFlow<List<User>> = sharedComponents.stateStore.users.asAppStateFlow(
        listOf())
    override val currentHttpServerAddress: StateFlow<String> = sharedComponents.stateStore.currentHttpServerAddress.asAppStateFlow("")
    override val deviceCode: StateFlow<String> = sharedComponents.stateStore.deviceCode
    override val currentHost: StateFlow<String> = sharedComponents.stateStore.currentHost.asAppStateFlow("")
    override fun setFirstLoadUser(value: Boolean) {
        sharedComponents.applicationScope.launch {
            sharedComponents.actorCollection.preferenceActor.save(PreferenceName.FIRST_LOAD_USER, value)
        }
    }

    override fun testHello() {
        sharedComponents.applicationScope.launch {
            sharedComponents.actorCollection.userActor.testHello()
        }
    }
}