package org.tty.dailyset.common.observable

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import kotlinx.coroutines.flow.Flow

data class InitialFlow<T>(
    val flow: Flow<T>,
    val initial: T
): Flow<T> by flow

data class InitialMutableFlow<T>(
    val flow: MutableFlow<T>,
    val initial: T
)

inline fun <reified T> Flow<T>.initial(value: T): InitialFlow<T> {
    return InitialFlow(this, value)
}

inline fun <reified T> MutableFlow<T>.initial(value: T): InitialMutableFlow<T> {
    return InitialMutableFlow(this, value)
}

@Composable
fun <T> InitialFlow<T>.collectAsState(): State<T> {
    return this.flow.collectAsState(initial = this.initial)
}