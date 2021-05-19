package org.tty.dailyset.ui.component

import android.util.Log
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp

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

