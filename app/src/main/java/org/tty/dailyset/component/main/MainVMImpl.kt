package org.tty.dailyset.component.main

import androidx.compose.runtime.Composable
import org.tty.dailyset.common.observable.InitialMutableSharedFlow
import org.tty.dailyset.common.observable.initial
import org.tty.dailyset.component.common.sharedComponents
import org.tty.dailyset.ui.page.MainPageTabs

@Composable
fun mainVM(): MainVM {
    val sharedComponents = sharedComponents()

    return object: MainVM {
        override val mainTab: InitialMutableSharedFlow<MainPageTabs> = sharedComponents.stateStore.mainTab.initial(MainPageTabs.SUMMARY)
    }
}