package org.tty.dailyset.component.profile

import androidx.compose.runtime.Composable
import kotlinx.coroutines.flow.StateFlow
import org.tty.dailyset.annotation.UseComponent
import org.tty.dailyset.bean.entity.DefaultEntities
import org.tty.dailyset.bean.entity.User
import org.tty.dailyset.bean.entity.UserTicketInfo
import org.tty.dailyset.component.common.SharedComponents
import org.tty.dailyset.component.common.asAppStateFlow
import org.tty.dailyset.component.common.sharedComponents

@Composable
@UseComponent
fun rememberProfileVM(): ProfileVM {
    val sharedComponents = sharedComponents()
    return sharedComponents.viewModelStore.getVM("profileVM") {
        ProfileVMImpl(sharedComponents)
    }
}

class ProfileVMImpl(private val sharedComponents: SharedComponents): ProfileVM {
    // warning: must use property initializer.
    override val currentUser: StateFlow<User> = sharedComponents.stateStore.currentUser.asAppStateFlow(DefaultEntities.emptyUser())
    override val userTicketInfo: StateFlow<UserTicketInfo> = sharedComponents.stateStore.userTicketInfo.asAppStateFlow(
        DefaultEntities.emptyUserTicketInfo())
}