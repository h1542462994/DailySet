package org.tty.dailyset.common.observable

import kotlinx.coroutines.flow.Flow

data class InitialFlow<T>(
    val flow: Flow<T>,
    val initial: T
)

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