package org.tty.dailyset.ui.component

import android.util.Log
import androidx.compose.foundation.gestures.animateScrollBy
import androidx.compose.foundation.gestures.scrollBy
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import org.tty.dailyset.ui.utils.toPx

@Composable
fun ListSelector(
    state: LazyListState,
    height: Dp,
    cellHeight: Dp,
    content: LazyListScope.() -> Unit
) {
    val spaceHeightDp = (height - cellHeight) / 2
    var first by remember {
        mutableStateOf(true)
    }

    if (first) {
        first = false
        Log.d("ListSelector", height.toString())
    }

    LazyColumn(
        modifier = Modifier.height(height = height),
        state = state
    ) {
        item(key = "*header") {
            Spacer(modifier = Modifier.height(spaceHeightDp))
        }
        content()
        item(key = "*end") {
            Spacer(modifier = Modifier.height(spaceHeightDp))
        }
    }
}

@Composable
fun ListSelector(
    data: List<String>,
    height: Dp,
    cellHeight: Dp,
    initItemIndex: Int,
    onItemIndexChanged: (Int) -> Unit
) {
    val spaceHeightDp = (height - cellHeight) / 2

    val cellHeightPx = toPx(dp = cellHeight)
    val state = rememberLazyListState(initialFirstVisibleItemScrollOffset = (cellHeightPx * initItemIndex).toInt())
    var currentIndex = initItemIndex
    val currentOffsetPx = (state.layoutInfo.viewportStartOffset + state.layoutInfo.viewportEndOffset) / 2
    state.layoutInfo.visibleItemsInfo.forEachIndexed{ i, item ->
        if (item.offset >= currentOffsetPx && item.offset < currentOffsetPx + cellHeightPx) {
            currentIndex = i
        }
    }
    
    LazyColumn(
        modifier = Modifier.height(height = height),
        state = state
    ) {
        item(key = "*header") {
            Spacer(modifier = Modifier.height(spaceHeightDp))
        }
        itemsIndexed(data, itemContent = { index, item ->
            Row(modifier = Modifier.height(40.dp)) {
                Text(
                    text = item,
                    modifier = Modifier.align(Alignment.CenterVertically),
                    color = if (currentIndex == index) MaterialTheme.colors.primary else Color.Unspecified
                )
            }
        })
        item(key = "*end") {
            Spacer(modifier = Modifier.height(spaceHeightDp))
        }
    }
}
