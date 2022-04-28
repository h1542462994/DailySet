package org.tty.dailyset.component.common

import androidx.compose.runtime.Composable
import org.tty.dailyset.LocalNav
import org.tty.dailyset.MainActions
import org.tty.dailyset.provider.LocalServices

@Composable
fun sharedComponents(): SharedComponents {
    return LocalServices.current
}

@Composable
fun nav(): MainActions {
    return LocalNav.current.action
}