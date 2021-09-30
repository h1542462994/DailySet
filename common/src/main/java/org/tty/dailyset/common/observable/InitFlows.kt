package org.tty.dailyset.common.observable

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

data class InitFlow<T>(
    val flow: Flow<T>,
    val initial: T
)

data class InitMutableStateFlow<T>(
    val flow: MutableStateFlow<T>,
    val initial: T
)

inline fun <reified T> Flow<T>.initial(value: T): InitFlow<T> {
    return InitFlow(this, value)
}

inline fun <reified T> MutableStateFlow<T>.initial(value: T): InitMutableStateFlow<T> {
    return InitMutableStateFlow(this, value)
}