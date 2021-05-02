package org.tty.dailyset.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.tty.dailyset.R
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

/**
 * ProfileMenuItem, provides slots to [icon], title, content
 */
@Composable
fun ProfileMenuItem(
    icon: (@Composable () -> Unit)? = null,
    next: Boolean = false,
    onClick: (() -> Unit)? = null,
    title: @Composable () -> Unit,
    content: @Composable () -> Unit,
) {
    var modifier1 = Modifier
        .height(56.dp)
        .fillMaxWidth()
        .background(MaterialTheme.colors.background)
    if (onClick != null){
        modifier1 = modifier1.clickable { onClick() }
    }

    Row(
        modifier = modifier1
    ) {
        if (icon != null) {
            BoxWithConstraints(
                Modifier
                    .width(56.dp)
                    .fillMaxHeight()
                    .padding(16.dp)
            ) {
                icon()
            }
        }
        var modifier2 = Modifier
            .weight(1f)
            .fillMaxHeight()
            .wrapContentHeight(align = Alignment.CenterVertically)
        if (icon != null) {
            modifier2 = modifier2.padding(start = 8.dp)
        } else {
            modifier2 = modifier2.padding(start = 24.dp)
        }
        BoxWithConstraints(
           modifier = modifier2
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


        Column(
            Modifier
                .width(32.dp)
                .padding(horizontal = 8.dp)
                .fillMaxHeight()
                .wrapContentHeight(align = Alignment.CenterVertically)
        ){
            if (next) {
                Icon(imageVector = Icons.Filled.KeyboardArrowRight, contentDescription = null, tint = LocalPalette.current.textColorDetail)
            }
        }
    }
}

@Composable
fun ProfileMenuItem(
    icon: ImageVector,
    next: Boolean,
    onClick: (() -> Unit)? = null,
    title: String,
    content: @Composable () -> Unit = {}
) {
    ProfileMenuItem(
        icon = { Icon(imageVector = icon, contentDescription = null, modifier = Modifier.fillMaxSize(), tint = LocalPalette.current.textColor) },
        title = { Text(text = title, color = LocalPalette.current.textColor, fontSize = 18.sp) },
        content = { content() },
        next = next,
        onClick = onClick
    )
}


@Composable
fun ProfileMenuItem(
    icon: ImageVector,
    next: Boolean = false,
    onClick: (() -> Unit)? = null,
    title: String,
    content: String
) {
    ProfileMenuItem(
        icon = { Icon(imageVector = icon, contentDescription = null, modifier = Modifier.fillMaxSize(), tint = LocalPalette.current.textColor) },
        next = next,
        onClick = onClick,
        title = { Text(text = title, color = LocalPalette.current.textColor, fontSize = 18.sp) },
        content = { Text(text = content, color = LocalPalette.current.textColorDetail) },
    )
}

@Composable
fun ProfileMenuItem(
    next: Boolean,
    onClick: (() -> Unit)? = null,
    title: String,
    content: @Composable () -> Unit = {},
) {
    ProfileMenuItem(
        next = next,
        onClick = onClick,
        title = { Text(text = title, color = LocalPalette.current.textColor, fontSize = 18.sp) },
        content = { content() },
    )
}

@Composable
fun ProfileMenuItem(
    next: Boolean,
    onClick: (() -> Unit)? = null,
    title: String,
    content: String,
) {
    ProfileMenuItem(
        next = next,
        onClick = onClick,
        title = { Text(text = title, color = LocalPalette.current.textColor, fontSize = 18.sp) },
        content = { Text(text = content, color = LocalPalette.current.textColorDetail) },
    )
}

@Composable
fun TipBox(
    content: @Composable () -> Unit
) {
    BoxWithConstraints(
        modifier = Modifier.background(color = LocalPalette.current.backgroundColor)
            .fillMaxWidth()
    ) {
        content()
    }
}

@Composable
fun TipBox(value: String) {
    TipBox {
        Text(
            modifier = Modifier.padding(horizontal = 24.dp, vertical = 8.dp),
            text = value,
            color = LocalPalette.current.textColorDetail
        )
    }
}

@Composable
fun IconText(imageVector: ImageVector, text: String) {
    Row(
        modifier = Modifier
            .wrapContentSize(align = Alignment.Center)
    ) {
        Icon(
            modifier = Modifier.scale(0.8f),
            imageVector = imageVector, contentDescription = null, tint = LocalPalette.current.textColor
        )
        Text(
            modifier = Modifier.align(alignment = Alignment.CenterVertically),
            text = text, color = LocalPalette.current.textColor
        )
    }
}