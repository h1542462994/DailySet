package org.tty.dailyset.common.observable

import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import org.tty.dioc.util.pair

/**
 * use combine operator on [flow1] and [flow2]
 */
fun <T1, T2, R> flow2(flow1: Flow<T1>, flow2: Flow<T2>, transform: ((value1: T1, value2: T2) -> R)): Flow<R> {
    return flow1.combine(flow2) { value1, value2 ->
        transform(value1, value2)
    }
}

/**
 * use combine operator on [flow1], [flow2] and [flow3]
 */
fun <T1, T2, T3, R> flow3(flow1: Flow<T1>, flow2: Flow<T2>, flow3: Flow<T3>, transform: (value1: T1, value2: T2, value3: T3) -> R): Flow<R> {
    return flow1.combine(flow2) { value1, value2 ->
        pair(value1, value2)
    }.combine(flow3) { pair, value3 ->
        transform(pair.first, pair.second, value3)
    }
}

/**
 * use combine operator on [flow1] and [flow2] and receive to [Pair]
 */
fun <T1, T2> flow2ToPair(flow1: Flow<T1>, flow2: Flow<T2>): Flow<Pair<T1, T2>> {
    return flow2(flow1, flow2) { value1, value2 ->
        Pair(value1, value2)
    }
}

/**
 * use combine operator on [flow1], [flow2] and [flow3] and receive to [Triple]
 */
fun <T1, T2, T3> flow3ToTriple(flow1: Flow<T1>, flow2: Flow<T2>, flow3: Flow<T3>): Flow<Triple<T1, T2, T3>> {
    return flow3(flow1, flow2, flow3) { value1, value2, value3 ->
        Triple(value1, value2, value3)
    }
}

/**
 * to create a mutableFlow
 */
fun <T> mutableFlowOf(value: T): MutableFlow<T> {
    val liveData = MutableLiveData(value)
    return MutableFlowImpl(liveData)
}