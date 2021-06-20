package org.tty.dailyset.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.tty.dailyset.ui.theme.LocalPalette

@Composable
fun TopBar(
    title: @Composable () -> Unit,
    useBack: Boolean = false,
    onBackPressed: () -> Unit = {}
) {
    Row(modifier = Modifier
        .height(56.dp)
        .background(LocalPalette.current.background1)
    ) {
        BoxWithConstraints(modifier = Modifier
            .width(56.dp)
            .padding(8.dp)
            .fillMaxHeight()
            .wrapContentSize(align = Alignment.Center)
            .clip(shape = CircleShape)
            .clickable(onClick = onBackPressed)
        ) {
            if (useBack) {
                Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = null,
                    modifier = Modifier
                        .padding(8.dp)
                        .fillMaxSize(),
                    tint = LocalPalette.current.primary
                )
            }
        }
        BoxWithConstraints(modifier = Modifier
            .fillMaxHeight()
            .weight(1f)
            .padding(horizontal = 12.dp, vertical = 8.dp)
            .wrapContentHeight(align = Alignment.CenterVertically)
        ) {
            title()
        }
    }
}

@Composable
fun TopBar(
    title: String,
    useBack: Boolean = false,
    onBackPressed: () -> Unit = {}
) {
    TopBar(
        title = { Text(text = title, fontSize = 20.sp, fontWeight = FontWeight.Bold) },
        useBack = useBack,
        onBackPressed = onBackPressed
    )
}

@Composable
fun CenterBar(
    useBack: Boolean = false,
    onBackPressed: () -> Unit = {},
    extensionArea: (@Composable () -> Unit)? = null,
    content: @Composable () -> Unit
) {
    Row(modifier = Modifier
        .height(56.dp)
        .background(LocalPalette.current.background1)
    ) {
        BoxWithConstraints(modifier = Modifier
            .width(56.dp)
            .padding(8.dp)
            .fillMaxHeight()
            .wrapContentSize(align = Alignment.Center)
            .clip(shape = CircleShape)
            .clickable(onClick = onBackPressed)
        ) {
            if (useBack) {
                Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = null,
                    modifier = Modifier
                        .padding(8.dp)
                        .fillMaxSize(),
                    tint = LocalPalette.current.primary
                )
            }
        }
        var modifier1 = Modifier
            .fillMaxHeight()
            .weight(1.0f)
            .wrapContentSize(align = Alignment.Center)

        modifier1 = if (extensionArea == null) {
            modifier1.padding(start = 12.dp, top = 0.dp, end = 12.dp + 56.dp, bottom = 0.dp)
        } else {
            modifier1.padding(horizontal = 12.dp, vertical = 0.dp)
        }

        BoxWithConstraints(modifier = modifier1) {
            content()
        }

        if (extensionArea != null) {
            extensionArea()
        }

    }
}

@Composable
fun BarExtension(
    expandedState: MutableState<Boolean>,
    dropDownContent: @Composable ColumnScope.() -> Unit
) {
    var expanded by expandedState

    BoxWithConstraints(modifier = Modifier
        .width(56.dp)
        .padding(8.dp)
        .fillMaxHeight()
        .wrapContentSize(align = Alignment.Center)
        .clip(shape = CircleShape)
        .clickable(onClick = { expanded = true })
    ) {
        Icon(imageVector = Icons.Filled.MoreVert, contentDescription = null,
            modifier = Modifier
                .padding(8.dp)
                .fillMaxSize(),
            tint = LocalPalette.current.primary
        )
        DropdownMenu(
            modifier = Modifier.width(180.dp),
            expanded = expanded, onDismissRequest = { expanded = false },
            offset = DpOffset(x = (-36).dp, y = (56).dp)
        ) {
            dropDownContent()
        }
    }
}

@Composable
fun CenterBar(
    useBack: Boolean = false,
    onBackPressed: () -> Unit = {},
    extensionArea: @Composable() (() -> Unit)? = null,
    content: String
) {
    CenterBar(
        useBack = useBack,
        onBackPressed = onBackPressed,
        extensionArea = extensionArea
    ) {
        Text(text = content, fontSize = 20.sp, fontWeight = FontWeight.Bold)
    }
}

@Preview
@Composable
fun TopBarPreview() {
    TopBar(title = "测试标题", useBack = true)

}