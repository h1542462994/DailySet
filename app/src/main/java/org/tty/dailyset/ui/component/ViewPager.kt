package org.tty.dailyset.ui.component

import android.util.Log
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.gestures.animateScrollBy
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.interaction.collectIsDraggedAsState
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import kotlinx.coroutines.launch
import org.tty.dailyset.component.common.measuredWidthDp
import org.tty.dailyset.component.common.toPx
import kotlin.math.floor

@Composable
fun ViewPager(
    previous: @Composable (() -> Unit)? = null,
    current: @Composable () -> Unit,
    next: @Composable (() -> Unit)? = null,
    stateKey: Any?,
    onPrevious: () -> Unit,
    onNext: () -> Unit
) {

    val measuredWidthDp = measuredWidthDp()
    val measuredWidth = toPx(dp = measuredWidthDp).toInt()
    var scrollState: ScrollState? = null
    val scope = rememberCoroutineScope()

    if (previous == null && next == null) {
        BoxWithConstraints(
            modifier = Modifier
                .width(measuredWidthDp)
                .fillMaxHeight()
        ) {
            current()
        }
    } else if (previous == null && next != null) {
        scrollState = rememberScrollState(0)
        LaunchedEffect(key1 = stateKey, block = {
            scrollState?.scrollTo(0)
        })
        Row(
            modifier = Modifier
                .width(measuredWidthDp * 2)
                .horizontalScroll(scrollState)
        ) {
            BoxWithConstraints(modifier = Modifier
                .width(measuredWidthDp)
                .fillMaxHeight()) {
                current()
            }
            BoxWithConstraints(modifier = Modifier
                .width(measuredWidthDp)
                .fillMaxHeight()) {
                next()
            }
        }
    } else if (previous != null && next == null) {

        scrollState = rememberScrollState(measuredWidth)
        LaunchedEffect(key1 = stateKey, block = {
            scrollState?.scrollTo(measuredWidth)
        })
        Row(
            modifier = Modifier
                .width(measuredWidthDp * 2)
                .horizontalScroll(scrollState)
        ) {
            BoxWithConstraints(modifier = Modifier
                .width(measuredWidthDp)
                .fillMaxHeight()) {
                previous()
            }
            BoxWithConstraints(modifier = Modifier
                .width(measuredWidthDp)
                .fillMaxHeight()) {
                current()
            }
        }
    } else if (previous != null && next != null) {
        scrollState = rememberScrollState(measuredWidth)
        LaunchedEffect(key1 = stateKey, block = {
            scrollState.scrollTo(measuredWidth)
        })
        Row(
            modifier = Modifier
                .width(measuredWidthDp * 3)
                .horizontalScroll(scrollState)
        ) {
            BoxWithConstraints(modifier = Modifier
                .width(measuredWidthDp)
                .fillMaxHeight()) {
                previous()
            }
            BoxWithConstraints(modifier = Modifier
                .width(measuredWidthDp)
                .fillMaxHeight()) {
                current()
            }
            BoxWithConstraints(modifier = Modifier
                .width(measuredWidthDp)
                .fillMaxHeight()) {
                next()
            }
        }
    }

    if (scrollState != null) {
        val isDragged = scrollState.interactionSource.collectIsDraggedAsState().value
        // use scope related to current composition.


        // prevent the action if is not dragged
        var preventAction by remember {
            mutableStateOf(false)
        }

        val actionKey = "${isDragged},${scrollState.isScrollInProgress}"
        // if the action is need to be act
        val needAction = !preventAction && (!isDragged and !scrollState.isScrollInProgress)
        // side-effect to change the current scroll position when stopped.
        LaunchedEffect(key1 = actionKey, block = {
            if (needAction) {
                preventAction = true
                val realIndex = floor((scrollState.value.toDouble() / measuredWidth.toDouble()) + 0.5).toInt()
                val targetOffset = realIndex * measuredWidth
                scope.launch {
                    scrollState.animateScrollTo(targetOffset)
                }

                if (previous == null && next != null) {
                    if (realIndex == 1) {
                        onNext()
                    }
                }
                if (previous != null && next == null) {
                    if (realIndex == 0) {
                        onPrevious()
                    }
                }
                if (previous != null && next != null) {
                    if (realIndex == 0){
                        onPrevious()
                    } else if (realIndex == 2) {
                        onNext()
                    }
                }
            } else if(isDragged) {
                preventAction = false
            }
        })
    }


}