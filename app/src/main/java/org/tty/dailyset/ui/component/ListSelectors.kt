package org.tty.dailyset.ui.component

import android.util.Log
import androidx.compose.foundation.gestures.animateScrollBy
import androidx.compose.foundation.interaction.collectIsDraggedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.tty.dailyset.ui.utils.toPx
import kotlin.math.roundToInt

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
    width: Dp,
    cellHeight: Dp,
    initItemIndex: Int,
    onItemIndexChanged: (Int) -> Unit
) {
    val spaceHeightDp = (height - cellHeight) / 2

    val cellHeightPx = toPx(dp = cellHeight)
    val spaceHeightPx = toPx(dp = spaceHeightDp)

    val state = rememberLazyListState(initialFirstVisibleItemScrollOffset = (cellHeightPx * initItemIndex).toInt())

    var currentIndex by remember {
        mutableStateOf(initItemIndex)
    }

    /**
     * 计算axisY offset.
     */
    val offsetY by derivedStateOf {
        if (state.firstVisibleItemIndex == 0) {
            state.firstVisibleItemScrollOffset.toFloat()
        } else {
            spaceHeightPx + (state.firstVisibleItemIndex - 1) * cellHeightPx + state.firstVisibleItemScrollOffset
        }
    }

    // no inline
    val tempOffsetY = offsetY
    val tempIndex = (offsetY / cellHeightPx).roundToInt()
    if (tempIndex != currentIndex) {
        currentIndex = tempIndex
        onItemIndexChanged(currentIndex)
    }

    val isDragged = state.interactionSource.collectIsDraggedAsState().value
    var firstInteract by remember { mutableStateOf(false) }
    if (!isDragged && firstInteract){
        if (!state.isScrollInProgress) {
            firstInteract = false
            //Log.d("ListSelector", "upPress")
            GlobalScope.launch {
                val targetOffset = (cellHeightPx * tempIndex)
                state.animateScrollBy(targetOffset - tempOffsetY)
            }
        }
    } else if (isDragged) {
        firstInteract= true
    }

    LazyColumn(
        modifier = Modifier
            .height(height = height)
            .width(width = width),
        state = state
    ) {
        item(key = "*header") {
            Spacer(modifier = Modifier.height(spaceHeightDp))
        }
        itemsIndexed(data, itemContent = { index, item ->
            Row(modifier = Modifier.height(cellHeight).width(width).wrapContentWidth(align = Alignment.CenterHorizontally)) {
                Text(
                    text = item,
                    modifier = Modifier.width(width).wrapContentWidth(Alignment.CenterHorizontally).align(Alignment.CenterVertically),
                    color = if (currentIndex == index) MaterialTheme.colors.primary else Color.Unspecified,
                    fontWeight = if (currentIndex == index) FontWeight.Bold else null,
                    fontSize = if (currentIndex == index) 20.sp else 16.sp
                )
            }
        })
        item(key = "*end") {
            Spacer(modifier = Modifier.height(spaceHeightDp))
        }
    }
}
