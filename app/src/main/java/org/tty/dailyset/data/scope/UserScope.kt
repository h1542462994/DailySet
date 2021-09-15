package org.tty.dailyset.data.scope

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.State
import org.tty.dailyset.common.observable.state
import org.tty.dailyset.model.entity.User
import org.tty.dailyset.model.lifetime.UserState
import org.tty.dailyset.provider.mainViewModel as vm

@Immutable
interface UserScope: PreferenceScope {
    @Composable
    fun users(): State<List<User>> {
        return state(vm.users)
    }

    @Composable
    fun currentUserState(): State<UserState> {
        return state(vm.userState)
    }
}

