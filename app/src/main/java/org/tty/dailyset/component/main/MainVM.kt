package org.tty.dailyset.component.main

import kotlinx.coroutines.flow.MutableStateFlow
import org.tty.dailyset.ui.page.MainPageTabs

interface MainVM {
    val mainTab: MutableStateFlow<MainPageTabs>
}