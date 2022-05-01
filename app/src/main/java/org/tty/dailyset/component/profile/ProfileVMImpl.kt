package org.tty.dailyset.component.profile

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import kotlinx.coroutines.flow.StateFlow
import org.tty.dailyset.annotation.UseComponent
import org.tty.dailyset.bean.entity.User
import org.tty.dailyset.component.common.asActivityColdStateFlow
import org.tty.dailyset.component.common.asActivityHotStateFlow
import org.tty.dailyset.component.common.sharedComponents

@Composable
@UseComponent
fun rememberProfileVM(): ProfileVM {
    val sharedComponents = sharedComponents()
    return remember {
        object: ProfileVM {
            override val currentUser: StateFlow<User> = sharedComponents.stateStore.currentUser.asActivityHotStateFlow(User.default())
        }
    }
}