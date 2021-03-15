package org.tty.dailyset.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.tty.dailyset.ui.theme.LocalPalette

@Composable
fun ProfileMenuGroup(
    title: @Composable RowScope.() -> Unit = {},
    body: @Composable ColumnScope.() -> Unit = {}
) {
    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(LocalPalette.current.background1)
                .padding(horizontal = 16.dp, vertical = 8.dp),
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
        title = { Text(text = title, color = LocalPalette.current.textColorTitle, fontWeight = FontWeight.Bold) },
        body
    )
}

@Composable
fun ProfileMenuItem(
    icon: @Composable () -> Unit,
    title: @Composable () -> Unit,
    content: @Composable () -> Unit,
    next: Boolean = false,
) {
    Row(
        Modifier
            .height(56.dp)
            .fillMaxWidth()
            .background(LocalPalette.current.gray20)
    ) {
        Column(
            Modifier
                .width(56.dp)
                .fillMaxHeight()
                .padding(12.dp)
                .background(MaterialTheme.colors.background)
        ) {
            icon()
        }
        Column(
            Modifier
                .fillMaxHeight()
                .wrapContentHeight(align = Alignment.CenterVertically)
        ) {
            title()
        }
        content()
        if (next) {
            Column(
                Modifier
                    .width(24.dp)
                    .background(MaterialTheme.colors.background)
            ){
                Icon(imageVector = Icons.Filled.ArrowForward, contentDescription = null)
            }
        }
    }
}

@Composable
fun ProfileMenuItem(
    icon: ImageVector,
    title: String,
    content: String,
    next: Boolean = false,
) {
    ProfileMenuItem(
        icon = { Icon(imageVector = icon, contentDescription = null, modifier = Modifier.fillMaxSize()) },
        title = { Text(text = title, color = LocalPalette.current.textColor, fontSize = 20.sp) },
        content = { Text(text = content) },
        next
    )
}