package org.tty.dailyset.component.common

import androidx.compose.runtime.Composable
import org.tty.dailyset.LocalNav
import org.tty.dailyset.MainActions

interface BaseVM {
    val nav: MainActions @Composable get() = LocalNav.current.action
}