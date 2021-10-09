package org.tty.dailyset.common.observable

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import org.tty.dioc.util.pair

fun <T1, T2, R> flow2(flow1: Flow<T1>, flow2: Flow<T2>, transform: ((value1: T1, value2: T2) -> R)): Flow<R> {
    return flow1.combine(flow2) { value1, value2 ->
        transform(value1, value2)
    }
}

fun <T1, T2, T3, R> flow3(flow1: Flow<T1>, flow2: Flow<T2>, flow3: Flow<T3>, transform: (value1: T1, value2: T2, value3: T3) -> R): Flow<R> {
    return flow1.combine(flow2) { value1, value2 ->
        pair(value1, value2)
    }.combine(flow3) { pair, value3 ->
        transform(pair.first, pair.second, value3)
    }
}