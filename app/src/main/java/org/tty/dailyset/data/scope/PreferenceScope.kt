package org.tty.dailyset.data.scope

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.State
import org.tty.dailyset.common.observable.state
import org.tty.dailyset.provider.mainViewModel as vm

@Immutable
interface PreferenceScope: GlobalScope {
    @Composable
    fun seedVersion(): State<Int> {
        return state(vm.seedVersion)
    }

    @Composable
    fun currentUserUid(): State<String> {
        return state(vm.userUid)
    }

    //companion object: PreferenceScope
}

