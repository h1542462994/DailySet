package org.tty.dailyset.component.user

import kotlinx.coroutines.flow.StateFlow
import org.tty.dailyset.bean.entity.User
import org.tty.dailyset.component.common.BaseVM
import org.tty.dailyset.component.common.DialogVM

/**
 * **viewModel** for page **user**
 */
interface UserVM: BaseVM {
    /**
     * the stored users.
     */
    val users: StateFlow<List<User>>

    /**
     * current user.
     */
    val currentUser: StateFlow<User>

    val userShiftDialogVM: UserShiftDialogVM

    val userLogoutDialogVM: DialogVM

    /**
     * logout.
     */
    fun logout()

    /**
     * change current user.
     */
    fun changeCurrentUser(user: User)

    /**
     * re login.
     */
    fun reLogin(user: User)
}