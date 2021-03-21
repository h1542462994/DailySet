package org.tty.dailyset.ui.page

import androidx.compose.foundation.layout.Column
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import org.tty.dailyset.LocalNav
import org.tty.dailyset.R
import org.tty.dailyset.ui.component.CenterBar
import org.tty.dailyset.ui.component.TopBar

@Composable
fun TimeTablePage() {
    Column {
        CenterBar(true, LocalNav.current.action.upPress) {
            Text("TimeTable")
        }
        Text(text = "TimeTable")
    }
}
