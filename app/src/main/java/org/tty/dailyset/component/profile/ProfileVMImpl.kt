package org.tty.dailyset.component.profile

import androidx.compose.runtime.Composable
import org.tty.dailyset.annotation.UseComponent
import org.tty.dailyset.bean.entity.User
import org.tty.dailyset.common.observable.InitialFlow
import org.tty.dailyset.common.observable.initial
import org.tty.dailyset.component.common.sharedComponents

@Composable
@UseComponent
fun profileVM(): ProfileVM {
    val sharedComponents = sharedComponents()
    return object: ProfileVM {
        override val currentUser: InitialFlow<User> = sharedComponents.stateStore.currentUser.initial(User.default())
    }
}