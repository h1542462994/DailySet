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
    itemIndex: Int,
    onItemIndexChanged: (Int) -> Unit
) {
    val spaceHeight = (height - cellHeight) / 2
    val cellHeightPx = toPx(dp = cellHeight)
    val spaceHeightPx = toPx(dp = spaceHeight)
    // composition state finalKey, if finalKey changed in re-composition, the sub layout will be recomposed.
    val finalKey = "${data},${itemIndex}"

    var rememberIndex by remember(finalKey) {
        mutableStateOf(itemIndex)
    }

    val lazyListState = rememberSaveable(saver = LazyListState.Saver, key = finalKey) {
        LazyListState(
            firstVisibleItemIndex = (cellHeightPx * rememberIndex).toInt()
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
    LaunchedEffect(key1 = realIndex, block = {
        if (realIndex != rememberIndex) {
            rememberIndex = realIndex
            onItemIndexChanged(realIndex)
        }
    })

    val isDragged = lazyListState.interactionSource.collectIsDraggedAsState().value
    LaunchedEffect(key1 = lazyListState, block = {
        if (!isDragged && !lazyListState.isScrollInProgress) {
            GlobalScope.launch {
                val targetOffsetY = cellHeightPx * realIndex
                lazyListState.animateScrollBy(targetOffsetY - offsetY)
            }
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
            itemIndex = cTime.first - min_h) {
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
            itemIndex = index) {
            Log.d("ListSelector", "m:${it}")
            cTime = cTime.copy(
                second = minutes[it].toInt()
            )
        }

    }
}
