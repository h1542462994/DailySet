package org.tty.dailyset.ui.component

import android.util.Log
import androidx.compose.foundation.gestures.animateScrollBy
import androidx.compose.foundation.interaction.collectIsDraggedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
    itemIndex: Int,
    onItemIndexChanged: (Int) -> Unit
) {
    val TAG = "ListSelector"
    val spaceHeight = (height - cellHeight) / 2
    val cellHeightPx = toPx(dp = cellHeight)
    val spaceHeightPx = toPx(dp = spaceHeight)
    // composition state finalKey, if finalKey changed in re-composition, the sub layout will be recomposed.
    val finalKey = "$data"

    var rememberIndex by remember(finalKey) {
        mutableStateOf(itemIndex)
    }
    val lazyListState = rememberSaveable(saver = LazyListState.Saver) {
        LazyListState(
            firstVisibleItemScrollOffset = (cellHeightPx * rememberIndex).toInt()
        )
    }

    // if the data changed, initialize the scroll position.
    // side-effect to initialize the scroll position when data changed.
    LaunchedEffect(key1 = finalKey, block = {
        Log.d(TAG, "data changed, so update the lazyListState")
        lazyListState.scrollToItem(
            0, (cellHeightPx * rememberIndex).toInt()
        )
    })

    /**
     * offset Y
     */
    val offsetY =
        if (lazyListState.firstVisibleItemIndex == 0) {
            lazyListState.firstVisibleItemScrollOffset.toFloat()
        } else {
            spaceHeightPx+
                    (lazyListState.firstVisibleItemIndex - 1) * cellHeightPx +
                    lazyListState.firstVisibleItemScrollOffset
        }

    val realIndex = (offsetY / cellHeightPx).roundToInt()
    // side-effect to notify index when realIndex changed
    LaunchedEffect(key1 = realIndex, block = {
        if (realIndex != rememberIndex) {
            rememberIndex = realIndex
            Log.d(TAG, "index changed, so notify index = ${realIndex}, data = ${data[realIndex]}.")
            onItemIndexChanged(realIndex)
        }
    })

    val isDragged = lazyListState.interactionSource.collectIsDraggedAsState().value
    // use scope related to current composition.
    val scope = rememberCoroutineScope()

    // prevent the action if is not dragged
    var preventAction by remember {
        mutableStateOf(false)
    }
    val actionKey = "${isDragged},${lazyListState.isScrollInProgress}"
    // if the action is need to be act
    val needAction = !preventAction && (!isDragged and !lazyListState.isScrollInProgress)
    // side-effect to change the current scroll position when stopped.
    LaunchedEffect(key1 = actionKey, block = {
        if (needAction) {
            preventAction = true
            val targetOffset = (cellHeightPx * realIndex)
            Log.d(TAG,"isDragged and isScroll first changed to (false, false), ${targetOffset},${offsetY}, so scroll the item.")
            scope.launch {
                lazyListState.animateScrollBy(targetOffset - offsetY)
            }
        } else if(isDragged) {
            preventAction = false
        }
    })

    LazyColumn(
        modifier = Modifier
            .height(height = height)
            .width(width = width),
        state = lazyListState
    ) {
        item(key = "*header") {
            Spacer(modifier = Modifier.height(spaceHeight))
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
                    color = if (rememberIndex == index) MaterialTheme.colors.primary else Color.Unspecified,
                    fontWeight = if (rememberIndex == index) FontWeight.Bold else null,
                    fontSize = if (rememberIndex == index) 20.sp else 16.sp
                )
            }
        })
        item(key = "*end") {
            Spacer(modifier = Modifier.height(spaceHeight))
        }
    }

}

@Composable
fun TimeSelector(
    height: Dp,
    cellHeight: Dp,
    initTime: Time,
    minuteSpace: Int = 5,
    min: Time? = null,
    max: Time? = null,
    onTimeChanged: (Time) -> Unit
) {
    val TAG = "TimeSelector"

    data class HourMinute(
        val hour: Int,
        val minute: Int,
    )

    val (minH, minM) = min?.hm() ?: Pair(0,0)
    val (maxH, maxM) = max?.hm() ?: Pair(23,60)
    val (iniH, iniM) = initTime.hm()

    var rememberTime by remember(key1 = initTime) {
        mutableStateOf(HourMinute(iniH, iniM))
    }

    // check the minute range
    val cMinM = if (rememberTime.hour == minH) minM else 0
    val cMaxM = if (rememberTime.hour == maxH) maxM else 60
    if (rememberTime.minute < cMinM) {
        rememberTime = rememberTime.copy(minute = cMinM)

    }
    if (rememberTime.minute > cMaxM) {
        rememberTime = rememberTime.copy(minute = cMaxM)
    }

    val hours = (minH..maxH).map { it.toString() }
    val minutes =
        rangeX(cMinM, cMaxM, minuteSpace).map {
            if (it < 10) {
                "0${it}"
            } else {
                "$it"
            }
        }

    val hourIndex = rememberTime.hour - minH
    val minuteIndex = (rememberTime.minute - cMinM) / minuteSpace

    LaunchedEffect(key1 = rememberTime, block = {
        val time = Time.valueOf("${rememberTime.hour}:${rememberTime.minute}:0")
        Log.d(TAG,"time changed to ${time}, so notify it")
        onTimeChanged(time)
    })

    Row {
        ListSelector(
            data = hours,
            height = height,
            width = 40.dp,
            cellHeight = cellHeight,
            itemIndex = hourIndex) {
            rememberTime = rememberTime.copy(
                hour = hours[it].toInt()
            )
        }

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
            itemIndex = minuteIndex) {
            rememberTime = rememberTime.copy(
                minute = minutes[it].toInt()
            )
        }

    }
}
