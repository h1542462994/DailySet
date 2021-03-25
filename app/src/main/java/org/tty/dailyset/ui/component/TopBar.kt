package org.tty.dailyset.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
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
                        .fillMaxSize())
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
                        .fillMaxSize())
            }
        }
        BoxWithConstraints(modifier = Modifier
            .fillMaxHeight()
            .weight(1f)
            .padding(start = 12.dp, end = 12.dp + 56.dp, top = 8.dp, bottom = 8.dp)
            .wrapContentSize(align = Alignment.Center)
        ) {
            content()
        }
    }
}

@Composable
fun CenterBar(
    useBack: Boolean = false,
    onBackPressed: () -> Unit = {},
    content: String
) {
    CenterBar(
        useBack = useBack,
        onBackPressed = onBackPressed
    ) {
        Text(text = content, fontSize = 20.sp, fontWeight = FontWeight.Bold)
    }
}

@Preview
@Composable
fun TopBarPreview() {
    TopBar(title = "测试标题", useBack = true)

}