package org.tty.dailyset.data.scope

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import mainViewModel
import org.tty.dailyset.model.entity.User
import org.tty.dailyset.model.lifetime.UserState

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