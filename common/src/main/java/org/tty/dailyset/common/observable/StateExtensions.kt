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
 * create a state by [value]
 * @see LiveData
 * @see observeAsState()
 */
@Composable
fun <T> state(value: LiveData<T>, default: T): State<T> {
    return value.observeAsState(initial = default)
}

/**
 * create a state by [value]
 * @see LiveData
 * @see observeAsState()
 */
@Composable
fun <T> state(value: MutableLiveData<T>, default: T): MutableState<T> {
    val state = value.observeAsState(default)
    val setter = { v: T ->
        value.postValue(v)
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