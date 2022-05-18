package org.tty.dailyset.component.user

import androidx.compose.runtime.Composable
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.tty.dailyset.MainDestination
import org.tty.dailyset.bean.entity.DefaultEntities
import org.tty.dailyset.bean.entity.User
import org.tty.dailyset.component.common.*
import org.tty.dailyset.component.login.LoginInput

@Composable
fun rememberUserVM(): UserVM {
    val sharedComponents = sharedComponents()
    return sharedComponents.ltsVMSaver.getVM("userVM") {
        UserVMImpl(sharedComponents)
    }
}

class UserVMImpl(private val sharedComponents: SharedComponents): UserVM {
    override val users: StateFlow<List<User>> = sharedComponents.stateStore.users.asActivityColdStateFlow(
        listOf())

    override val currentUser: StateFlow<User> = sharedComponents.stateStore.currentUser.asActivityColdStateFlow(DefaultEntities.emptyUser())

    override val userShiftDialogVM: UserShiftDialogVM = UserShiftDialogVMImpl()

    override val userLogoutDialogVM: DialogVM = SimpleDialogVMImpl(false)

    override fun logout() {

    }

    override fun changeCurrentUser(user: User) {
        sharedComponents.activityScope.launch {
            sharedComponents.actorCollection.userActor.shiftUser(user)
        }
    }

    override fun reLogin(user: User) {
        sharedComponents.nav.action.toLogin(LoginInput(MainDestination.USER_ROUTE, user.userUid))
    }
}

class UserShiftDialogVMImpl(): UserShiftDialogVM {
    override val dialogOpen: MutableStateFlow<Boolean> = MutableStateFlow(false)
    override val shiftToUser: MutableStateFlow<User> = MutableStateFlow(DefaultEntities.emptyUser())
}
