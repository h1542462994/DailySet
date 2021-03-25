package org.tty.dailyset.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.MailOutline
import androidx.compose.material.icons.filled.Phone
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.tty.dailyset.ui.theme.DailySetTheme
import org.tty.dailyset.ui.theme.LocalPalette

/**
 * ProfileMenuGroup, provides slots to title and body.
 */
@Composable
fun ProfileMenuGroup(
    title: @Composable () -> Unit = {},
    body: @Composable () -> Unit = {}
) {
    Column {
        BoxWithConstraints(
            modifier = Modifier
                .fillMaxWidth()
                .background(LocalPalette.current.background1)
                .padding(horizontal = 16.dp, vertical = 8.dp),
        ){
            title()
        }
        body()
    }
}

/**
 * ProfileMenuGroup, provides a specialized title and a slot to body.
 */
@Composable
fun ProfileMenuGroup(
    title: String,
    body: @Composable () -> Unit) {
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
    onClick: () -> Unit = {},
    showIcon: Boolean = true,
) {
    Row(
        Modifier
            .height(56.dp)
            .fillMaxWidth()
            .background(MaterialTheme.colors.background)
            .clickable(onClick = onClick)
    ) {
        if (showIcon) {
            BoxWithConstraints(
                Modifier
                    .width(56.dp)
                    .fillMaxHeight()
                    .padding(12.dp)
            ) {
                icon()
            }
        }
        BoxWithConstraints(
            Modifier
                .weight(1f)
                .fillMaxHeight()
                .wrapContentHeight(align = Alignment.CenterVertically)
                .padding(horizontal = 8.dp)
        ) {
            title()
        }
        BoxWithConstraints(
            Modifier
                .fillMaxHeight()
                .wrapContentHeight(align = Alignment.CenterVertically)
                .padding(horizontal = 8.dp)
        ) {
            content()
        }

        if (next) {
            Column(
                Modifier
                    .width(32.dp)
                    .padding(horizontal = 8.dp)
                    .fillMaxHeight()
                    .wrapContentHeight(align = Alignment.CenterVertically)
            ){
                Icon(imageVector = Icons.Filled.KeyboardArrowRight, contentDescription = null, tint = LocalPalette.current.textColorDetail)
            }
        }
    }
}

@Composable
fun ProfileMenuItem(
    icon: ImageVector,
    title: String,
    content: @Composable () -> Unit = {},
    next: Boolean,
    onClick: () -> Unit = {}
) {
    ProfileMenuItem(
        icon = { Icon(imageVector = icon, contentDescription = null, modifier = Modifier.fillMaxSize(), tint = LocalPalette.current.textColor) },
        title = { Text(text = title, color = LocalPalette.current.textColor, fontSize = 18.sp) },
        content = { content() },
        next,
        onClick
    )
}

@Composable
fun ProfileMenuItem(
    title: String,
    content: @Composable () -> Unit = {},
    next: Boolean,
    onClick: () -> Unit = {}
) {
    ProfileMenuItem(
        icon = {},
        title = { Text(text = title, color = LocalPalette.current.textColor, fontSize = 18.sp) },
        content = { content() },
        next,
        onClick,
        showIcon = false
    )
}

@Composable
fun ProfileMenuItem(
    icon: ImageVector,
    title: String,
    content: String,
    next: Boolean = false,
    onClick: () -> Unit = {}
) {
    ProfileMenuItem(
        icon = { Icon(imageVector = icon, contentDescription = null, modifier = Modifier.fillMaxSize(), tint = LocalPalette.current.textColor) },
        title = { Text(text = title, color = LocalPalette.current.textColor, fontSize = 18.sp) },
        content = { Text(text = content, color = LocalPalette.current.textColorDetail) },
        next,
        onClick
    )
}

@Preview
@Composable
fun ProfileMenuPreview() {
    ProfileMenuGroup(title = "测试") {
        ProfileMenuItem(icon = Icons.Filled.MailOutline, title = "内容1", content = "内容2", true)
    }
}

@Preview
@Composable
fun ProfileMenuPreviewDark() {
    DailySetTheme(darkTheme = true) {
        ProfileMenuGroup(title = "测试") {
            ProfileMenuItem(icon = Icons.Filled.MailOutline, title = "内容1", content = "内容2", true)
        }
    }
}