package org.tty.dailyset.component.main

import androidx.compose.runtime.Composable
import org.tty.dailyset.common.observable.InitialMutableStateFlow
import org.tty.dailyset.common.observable.initial
import org.tty.dailyset.component.common.sharedComponents0
import org.tty.dailyset.ui.page.MainPageTabs

@Composable
fun mainVM(): MainVM {
    val sharedComponents = sharedComponents0()

    return object: MainVM {
        override val mainTab: InitialMutableStateFlow<MainPageTabs> = sharedComponents.stateStore.mainTab.initial(MainPageTabs.SUMMARY)
    }
}