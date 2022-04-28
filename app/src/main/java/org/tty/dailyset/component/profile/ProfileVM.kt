package org.tty.dailyset.component.profile

import org.tty.dailyset.bean.entity.User
import org.tty.dailyset.common.observable.InitialFlow

interface ProfileVM {
    val currentUser: InitialFlow<User>
}