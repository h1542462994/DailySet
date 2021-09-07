package org.tty.dailyset.data.scope

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.State
import androidx.compose.runtime.livedata.observeAsState
import org.tty.dailyset.model.entity.Preference
import org.tty.dailyset.model.entity.PreferenceName

@Immutable
interface PreferenceScope: GlobalScope {
    @Composable
    fun seedVersion(): State<Int> {
        return mainViewModel().seedVersion.observeAsState(0)
    }

    @Composable
    fun currentUserUid(): State<String> {
        return mainViewModel().currentUserUid.observeAsState(Preference.default(PreferenceName.CURRENT_USER_UID).value)
    }

    //companion object: PreferenceScope
}

