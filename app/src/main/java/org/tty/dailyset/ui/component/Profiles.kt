package org.tty.dailyset.ui.component

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.tty.dailyset.ui.theme.LocalPalette

@Composable
fun ProfileMenuGroup(
    title: @Composable RowScope.() -> Unit = {},
    body: @Composable ColumnScope.() -> Unit = {}
) {
    Column {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
            content = title
        )
        body()
    }
}

@Composable
fun ProfileMenuGroup(
    title: String,
    body: @Composable ColumnScope.() -> Unit) {
    ProfileMenuGroup(
        title = { Text(text = title, color = LocalPalette.current.background1) },
        body
    )
}