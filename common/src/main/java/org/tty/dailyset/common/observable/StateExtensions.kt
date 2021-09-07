package org.tty.dailyset.common.observable

import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

/**
 * create a [MutableState] to remember the [value]
 * @see remember
 * @see mutableStateOf
 */
@Composable
fun <T> state(value: T, policy: SnapshotMutationPolicy<T> = structuralEqualityPolicy()): MutableState<T> {
    return remember {
        mutableStateOf(value = value, policy = policy)
    }
}

/**
 * create a state by [livedata]
 * @see LiveData
 * @see observeAsState()
 */
@Composable
inline fun <reified T> state(livedata: LiveData<T>, initial: T): State<T> {
    return livedata.observeAsState(initial = initial)
}

/**
 * create a state by [livedata]
 * @see LiveData
 * @see observeAsState()
 */
@Composable
inline fun <reified T> state(livedata: MutableLiveData<T>, initial: T): MutableState<T> {
    val state = livedata.observeAsState(initial)
    val setter = { v: T ->
        livedata.postValue(v)
    }

    return object : MutableState<T> {
        override var value: T
            get() = state.value
            set(value) {
                setter.invoke(value)
            }

        override fun component1(): T = state.value

        override fun component2() = setter
    }
}