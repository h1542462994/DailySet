package org.tty.dailyset.data.scope

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.livedata.observeAsState
import mainViewModel
import org.tty.dailyset.model.entity.Preference
import org.tty.dailyset.model.entity.PreferenceName

@Composable
fun seedVersionPreference(): State<Preference?> {
    return mainViewModel().seedVersionPreference.observeAsState()
}

@Composable
fun seedVersion(): State<Int> {
    return mainViewModel().seedVersion.observeAsState(0)
}

@Composable
fun currentUserUid(): State<String> {
    return mainViewModel().currentUserUid.observeAsState(Preference.default(PreferenceName.CURRENT_USER_UID).value)
}