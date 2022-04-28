package org.tty.dailyset.common.observable

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.livedata.observeAsState
import kotlinx.coroutines.flow.*

/**
 * data class combines [flow] and [initial].
 */
data class InitialFlow<T>(
    val flow: Flow<T>,
    val initial: T
): Flow<T> by flow

/**
 * data class combines [flow] and [initial].
 */
data class InitialMutableSharedFlow<T>(
    val flow: MutableSharedFlow<T>,
    val initial: T
): MutableSharedFlow<T> by flow

/**
 * to create a [InitialFlow], so you can use [collectAsState] without a initial data.
 */
inline fun <reified T> Flow<T>.initial(value: T): InitialFlow<T> {
    return InitialFlow(this, value)
}

/**
 * to create a [InitialMutableSharedFlow], so you can use [collectAsState] without a initial data.
 */
inline fun <reified T> MutableSharedFlow<T>.initial(value: T): InitialMutableSharedFlow<T> {
    return InitialMutableSharedFlow(this, value)
}

/**
 * to collect the [InitialFlow] to [State].
 */
@Composable
fun <T> InitialFlow<T>.collectAsState(): State<T> {
    return this.flow.collectAsState(initial = this.initial)
}

/**
 * to collect the [InitialMutableSharedFlow] to [MutableState].
 */
@Composable
fun <T> InitialMutableSharedFlow<T>.collectAsState(): MutableState<T> {

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

inline fun <reified T> initialMutableSharedFlowOf(value: T): InitialMutableSharedFlow<T> {
    return MutableStateFlow(value).initial(value)
}

