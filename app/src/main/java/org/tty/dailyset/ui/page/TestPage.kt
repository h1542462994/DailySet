package org.tty.dailyset.ui.page

import androidx.compose.foundation.layout.Column
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.stringResource
import org.tty.dailyset.R
import org.tty.dailyset.LocalNav
import org.tty.dailyset.data.scope.DataScope
import org.tty.dailyset.ui.component.TopBar
import org.tty.dailyset.ui.utils.StatusBarToBackground

/**
 * page for debug and test
 */
@Composable
fun TestPage() {
    StatusBarToBackground()

    with(DataScope) {
        val seedVersion by seedVersion()
        val currentUserUid by currentUserUid()

        Column {
            TopBar(title = stringResource(id = R.string.debug), true, onBackPressed = LocalNav.current.action.upPress)
            Text(text = "seedVersion:${seedVersion}")
            Text(text = "currentUserUid:${currentUserUid}")
        }
    }
}