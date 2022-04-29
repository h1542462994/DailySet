package org.tty.dailyset.component.profile

import kotlinx.coroutines.flow.StateFlow
import org.tty.dailyset.bean.entity.User

interface ProfileVM {
    val currentUser: StateFlow<User>
}