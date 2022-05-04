package org.tty.dailyset.component.profile

import kotlinx.coroutines.flow.StateFlow
import org.tty.dailyset.bean.entity.User
import org.tty.dailyset.bean.entity.UserTicketInfo

interface ProfileVM {
    val currentUser: StateFlow<User>
    val userTicketInfo: StateFlow<UserTicketInfo>
}