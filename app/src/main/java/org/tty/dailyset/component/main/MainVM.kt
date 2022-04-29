package org.tty.dailyset.component.main

import org.tty.dailyset.common.observable.InitialMutableStateFlow
import org.tty.dailyset.ui.page.MainPageTabs

interface MainVM {
    val mainTab: InitialMutableStateFlow<MainPageTabs>
}