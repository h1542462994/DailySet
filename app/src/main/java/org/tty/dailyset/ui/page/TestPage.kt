package org.tty.dailyset.ui.page

import androidx.compose.foundation.layout.Column
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import org.tty.dailyset.LocalNav
import org.tty.dailyset.data.scope.DataScope
import org.tty.dailyset.ui.component.TopBar

/**
 * page for debug and test
 */
@Composable
fun TestPage() {
    with(DataScope) {
        val seedVersion by seedVersion()
        val currentUserUid by currentUserUid()

        Column {
            TopBar(title = "测试", true, onBackPressed = LocalNav.current.action.upPress)
            Text(text = "seedVersion:${seedVersion}")
            Text(text = "currentUserUid:${currentUserUid}")
        }
    }
}