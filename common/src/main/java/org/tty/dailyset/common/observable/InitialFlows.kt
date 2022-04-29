package org.tty.dailyset.common.observable

import androidx.compose.runtime.*
import kotlinx.coroutines.flow.*
import org.tty.dailyset.common.local.logger

/**
 * data class combines [flow] and [initial].
 */
@Deprecated("please use .asActivityStateFlow instead.")
data class InitialFlow<T>(
    val flow: Flow<T>,
    val initial: T
): Flow<T> by flow

/**
 * data class combines [flow] and [initial].
 */
@Deprecated("please use MutableStateFlow<T> instead.")
data class InitialMutableStateFlow<T>(
    val flow: MutableStateFlow<T>,
    val initial: T
): MutableStateFlow<T> by flow

/**
 * to create a [InitialFlow], so you can use [collectAsState] without a initial data.
 */
@Deprecated("please use .asActivityStateFlow instead.")
inline fun <reified T> Flow<T>.initial(value: T): InitialFlow<T> {
    return InitialFlow(this, value)
}

/**
 * to create a [InitialMutableStateFlow], so you can use [collectAsState] without a initial data.
 */
@Deprecated("please use MutableStateFlow<T> instead.")
inline fun <reified T> MutableStateFlow<T>.initial(value: T): InitialMutableStateFlow<T> {
    return InitialMutableStateFlow(this, value)
}

/**
 * to collect the [InitialFlow] to [State].
 */
@Composable
@Deprecated("please use .asActivityStateFlow instead.")
fun <T> InitialFlow<T>.collectAsState(): State<T> {
    return this.flow.collectAsState(initial = this.initial)
}

/**
 * to collect the [InitialMutableStateFlow] to [MutableState].
 */
@Composable
@Deprecated("please use MutableStateFlow<T> instead.")
fun <T> InitialMutableStateFlow<T>.collectAsState(): MutableState<T> {

    val state = this@collectAsState.flow.collectAsState(initial = this@collectAsState.initial)

    return object : MutableState<T> {
        override var value: T
            get() = state.value
            set(value) {
                this@collectAsState.flow.tryEmit(value)
            }

        override fun component1(): T {
            return value
        }

        override fun component2(): (T) -> Unit {
            return { value = it }
        }

    }
}

@Composable
@Deprecated("not used yet.")
fun <T> InitialFlow<T>.rememberCollectAsState(): MutableState<T> {
    val state = this.flow.collectAsState(initial = this.initial)
    return remember(key1 = "") { mutableStateOf(state.value) }
}

@Composable
@Deprecated("not used yet.")
fun <T> InitialMutableStateFlow<T>.rememberCollectAsState(key1: Any? = null): MutableState<T> {
    val state = this.flow.collectAsState()

    val rState = remember(key1 = key1) { mutableStateOf(state.value) }

    LaunchedEffect(key1 = rState.value, block = {
        this@rememberCollectAsState.flow.tryEmit(rState.value)
        logger.d("**","rememberCollectAsState: $rState")
    })
    return rState
}

@Deprecated("not used yet.")
inline fun <reified T> initialMutableSharedFlowOf(value: T): InitialMutableStateFlow<T> {
    return MutableStateFlow(value).initial(value)
}

@Deprecated("not used yet.")
fun <T> InitialMutableStateFlow<T>.asInitialFlow(): InitialFlow<T> {
    return InitialFlow(this.flow, this.initial)
}


