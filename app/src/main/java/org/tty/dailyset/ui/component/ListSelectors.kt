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
import org.tty.dailyset.ui.utils.hm
import org.tty.dailyset.ui.utils.rangeX
import org.tty.dailyset.ui.utils.toPx
import java.sql.Time
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

    var currentIndex by remember(key1 = data, key2 = initItemIndex) {
        mutableStateOf(initItemIndex)
    }

    val state = rememberLazyListState(initialFirstVisibleItemScrollOffset = (cellHeightPx * currentIndex).toInt())

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
        onItemIndexChanged(tempIndex)
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
        firstInteract = true
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
            Row(modifier = Modifier
                .height(cellHeight)
                .width(width)
                .wrapContentWidth(align = Alignment.CenterHorizontally)) {
                Text(
                    text = item,
                    modifier = Modifier
                        .width(width)
                        .wrapContentWidth(Alignment.CenterHorizontally)
                        .align(Alignment.CenterVertically),
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

@Composable
fun TimeSelector(
    height: Dp,
    width: Dp,
    cellHeight: Dp,
    initTime: Time,
    minuteSpace: Int = 5,
    min: Time? = null,
    max: Time? = null,
    onTimeChanged: (Time) -> Unit
) {
    val (min_h, min_m) = min?.hm() ?: Pair(0,0)
    val (max_h, max_m) = max?.hm() ?: Pair(23,60)
    val (ini_h, ini_m) = initTime.hm()

    //Log.d("ListSelector", "time:${min_h}:${min_m}")

    var cTime by remember(key1 = initTime) {
        mutableStateOf(Pair(ini_h, ini_m))
    }

    Log.d("ListSelector", "timeC:${cTime.first}:${cTime.second}")

    val hours = (min_h..max_h).map { it.toString() }
    val cMinM = if (cTime.first == min_h) min_m else 0
    val cMaxM = if (cTime.second == max_h) max_m else 60
    //Log.d("ListSelector","r:${cMinM},${cMaxM}")

    if (cTime.second < cMinM) {
        cTime = cTime.copy(second = cMinM)

    }
    if (cTime.second > cMaxM) {
        cTime = cTime.copy(second = cMaxM)
    }

    val minutes =
        rangeX(cMinM, cMaxM, minuteSpace).map {
            if (it < 10) {
                "0${it}"
            } else {
                "$it"
            }
        }

    Log.d("ListSelector", "ms:${minutes}")
    val index = (cTime.second - cMinM) / minuteSpace
    Log.d("ListSelector", "index:${index}")


    Row {
        ListSelector(
            data = hours,
            height = height,
            width = 40.dp,
            cellHeight = cellHeight,
            initItemIndex = cTime.first - min_h) {
            Log.d("ListSelector", "h:${it}")
            cTime = cTime.copy(
                first = hours[it].toInt()
            )
        }
        // :
        Column(
            modifier = Modifier
                .height(height)
                .wrapContentHeight(align = Alignment.CenterVertically)
        ) {
            Text(
                text = ":",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
        }
        ListSelector(
            data = minutes,
            height = height,
            width = 40.dp,
            cellHeight = cellHeight,
            initItemIndex = index) {
            Log.d("ListSelector", "m:${it}")
            cTime = cTime.copy(
                second = minutes[it].toInt()
            )
        }

    }
}
