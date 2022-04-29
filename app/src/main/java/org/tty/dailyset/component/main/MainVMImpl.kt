package org.tty.dailyset.component.main

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import kotlinx.coroutines.flow.MutableStateFlow
import org.tty.dailyset.annotation.UseComponent
import org.tty.dailyset.component.common.sharedComponents
import org.tty.dailyset.ui.page.MainPageTabs

@Composable
@UseComponent
fun rememberMainVM(): MainVM {
    val sharedComponents = sharedComponents()

    return remember {
        object: MainVM {
            override val mainTab: MutableStateFlow<MainPageTabs> = sharedComponents.stateStore.mainTab
        }
    }
}