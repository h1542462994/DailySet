package org.tty.dailyset.ui.component

import android.util.Log
import androidx.compose.foundation.gestures.animateScrollBy
import androidx.compose.foundation.interaction.collectIsDraggedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
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
import org.tty.dailyset.rangeX
import org.tty.dailyset.ui.utils.toPx
import java.time.LocalTime
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
    val tag = "ListSelector"
    val spaceHeight = (height - cellHeight) / 2
    val cellHeightPx = toPx(dp = cellHeight)
    val spaceHeightPx = toPx(dp = spaceHeight)
    // composition state finalKey, if finalKey changed in re-composition, the sub layout will be recomposed.
    val stateKey = "$data,$itemIndex"
    // ignore the dataKey
    val dataKey = "$data"

    // the currentIndex of the current state from (data, itemIndex) .. epoch
    var rememberIndex by remember(stateKey) {
        mutableStateOf(itemIndex)
    }
    val lazyListState = rememberSaveable(saver = LazyListState.Saver) {
        LazyListState(
            firstVisibleItemScrollOffset = (cellHeightPx * rememberIndex).toInt()
        )
    }


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

    // if the data changed, initialize the scroll position.
    // side-effect to initialize the scroll position when data changed.
    // FIXME: 2021/6/19 当外部项导致数据更改时，会导致状态异常。
    LaunchedEffect(key1 = stateKey, block = {
        if (itemIndex != realIndex) {
            Log.d(tag, "data changed, so update the lazyListState")
            lazyListState.scrollToItem(
                0, (cellHeightPx * rememberIndex).toInt()
            )
        }
    })

    // side-effect to notify index when realIndex changed
    LaunchedEffect(key1 = realIndex, block = {
        if (realIndex != rememberIndex) {
            rememberIndex = realIndex
            Log.d(tag, "index changed, so notify index = $realIndex")
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
            Log.d(tag,"isDragged and isScroll first changed to (false, false), ${targetOffset},${offsetY}, so scroll the item.")
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
    initTime: LocalTime,
    minuteSpace: Int = 5,
    min: LocalTime? = null,
    max: LocalTime? = null,
    onTimeChanged: (LocalTime) -> Unit
) {
    val TAG = "TimeSelector"

    data class HourMinute(
        val hour: Int,
        val minute: Int,
    )

    val (minH, minM) = min?.hm() ?: Pair(0,0)

    val (maxH, maxM) = max?.hm() ?: Pair(23,60)
    val (iniH, iniM) = initTime.hm()
    val finalKey = "${minH}:${minM},${maxH}:${maxM},${iniH}:${iniM}"

    var rememberTime by remember(key1 = finalKey) {
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
        val time = LocalTime.of(rememberTime.hour, rememberTime.minute)
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
        // TODO: 2021/5/24 当结束时间超过0点时的处理逻辑?
        ListSelector(
            data = minutes,
            height = height,
            width = 40.dp,
            cellHeight = cellHeight,
            itemIndex = minuteIndex) {
            rememberTime = rememberTime.copy(
                // FIXME: 2021/5/24 震荡的bug
                minute =  if (it < minutes.size) minutes[it].toInt() else minutes[0].toInt()
            )
        }
    }
}

/**
 * ListSelector count ver.
 */
@Composable
fun CountSelector(text: String, index: Int, onItemIndexChanged: (Int) -> Unit) {
    val width = 80.dp
    Column(modifier = Modifier.width(width)) {
        Text(
            modifier = Modifier
                .padding(bottom = 8.dp)
                .width(width)
                .wrapContentWidth(align = Alignment.CenterHorizontally),
            text = text,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold
        )
        ListSelector(
            data = (1..10).map { it.toString() },
            height = 180.dp,
            width = width,
            cellHeight = 40.dp,
            itemIndex = index
        ) {
            onItemIndexChanged(it)
        }
    }
}

internal fun LocalTime.hm(): Pair<Int, Int> {
    return Pair(this.hour, this.minute)
}