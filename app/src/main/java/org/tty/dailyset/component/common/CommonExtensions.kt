package org.tty.dailyset.component.common

import android.widget.Toast
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.unit.Dp
import kotlinx.coroutines.flow.*
import org.tty.dailyset.MainActions
import org.tty.dailyset.annotation.UseComponent
import org.tty.dailyset.common.local.logger
import org.tty.dailyset.provider.LocalSharedComponents
import org.tty.dailyset.provider.LocalSharedComponents0
import org.tty.dailyset.ui.theme.LocalPalette

@Composable
fun sharedComponents0(): SharedComponents {
    return LocalSharedComponents0.current
}

@UseComponent
fun sharedComponents(): SharedComponents {
    return LocalSharedComponents.current
}

@UseComponent
fun nav(): MainActions {
    return sharedComponents().nav.action
}

@Composable
fun measuredWidthDp(): Dp {
    return toDp(px = measuredWidth())
}

@Composable
fun measuredWidth(): Float {
    return LocalView.current.measuredWidth.toFloat()
    //return LocalView.current.measuredWidth.toFloat()
}

@Composable
fun measuredHeightDp(): Dp {
    return toDp(px = measuredHeight())
}

@Composable
fun measuredHeight(): Float {
    return LocalView.current.measuredHeight.toFloat()
}

@Composable
fun toPx(dp: Dp): Float {
    return with(LocalDensity.current) {
        dp.toPx()
    }
}

@Composable
fun toDp(px: Float): Dp {
    return with(LocalDensity.current) {
        px.toDp()
    }
}

@Composable
fun StatusBarToBackground1() {
    val window = sharedComponents0().window
    val background1 = LocalPalette.current.background1
    LaunchedEffect(key1 = LocalPalette.current.background1) {
        window.navigationBarColor = background1.toArgb()
    }
}

@Composable
fun StatusBarToBackground() {
    val window = sharedComponents0().window
    val background1 = LocalPalette.current.background1
    val background = MaterialTheme.colors.background
    LaunchedEffect(key1 = LocalPalette.current.background1) {
        window.navigationBarColor = background.toArgb()
    }
}

@UseComponent
fun showToast(text: String) {
    val sharedComponents = LocalSharedComponents.current
    Toast.makeText(sharedComponents.activityContext, text, Toast.LENGTH_SHORT).show()
}

/**
 *
 */
@UseComponent
fun <T> Flow<T>.asActivityColdStateFlow(initialValue: T): StateFlow<T> {
    val sharedComponents = LocalSharedComponents.current
    return this.stateIn(sharedComponents.activityScope, SharingStarted.WhileSubscribed(), initialValue)
}

@Composable
fun <T> MutableStateFlow<T>.rememberAsMutableState(): MutableState<T> {

    // collect the value and save a snapshot.
//    LaunchedEffect(key1 = "", block = {
//        this@rememberAsMutableState.collect()
//    })

    // collect the initial value, the value will not be changed in recomposition.
    val valueState = remember { mutableStateOf(value) }
    val setValueDelegate = { it: T ->
        valueState.value = it
        // update the source flow with value.
        logger.d("CommonExtensions", "setValueDelegate: $it, ${this@rememberAsMutableState}")
        this@rememberAsMutableState.value = it
    }

    val state = object: MutableState<T> {
        override var value: T
            get() = valueState.value
            set(value) {
                setValueDelegate(value)
            }

        override fun component1(): T {
            return valueState.value
        }

        override fun component2(): (T) -> Unit = setValueDelegate
    }

    return state

}

suspend fun <T> MutableStateFlow<T>.collectSkipFirst(action: (T) -> Unit) {
    var first = true
    this.collect {
        if (first) {
            first = false
        } else {
            action(it)
        }
    }
}
