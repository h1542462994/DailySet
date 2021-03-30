package org.tty.dailyset.data.scope

import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import org.tty.dailyset.model.entity.User
import org.tty.dailyset.model.lifetime.UserState

@Immutable
interface UserScope: PreferenceScope {
    @Composable
    fun users(): State<List<User>> {
        return mainViewModel().users.observeAsState(listOf())
    }

    @Composable
    fun currentUser(): State<User?> {
        val users by users()
        val currentUserUid by currentUserUid()

        return derivedStateOf {
            users.find { it.name == currentUserUid }
        }
    }

    @Composable
    fun currentUserState(): State<UserState> {
        val currentUser by currentUser()
        val currentUserUid by currentUserUid()

        return derivedStateOf {
            UserState(currentUser, currentUserUid)
        }
    }
}

