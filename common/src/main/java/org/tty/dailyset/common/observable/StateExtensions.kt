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
inline fun <reified T> state(value: T, policy: SnapshotMutationPolicy<T> = structuralEqualityPolicy()): MutableState<T> {
    return remember {
        mutableStateOf(value = value, policy = policy)
    }
}

/**
 * create a state by [liveData]
 * @see LiveData
 * @see observeAsState()
 */
@Composable
inline fun <reified T> state(liveData: LiveData<T>, initial: T): State<T> {
    return liveData.observeAsState(initial = initial)
}

/**
 * create a state by [liveData]
 * @see LiveData
 * @see observeAsState()
 */
@Composable
inline fun <reified T> state(liveData: InitialLiveData<T>): State<T> {
    val (l, i) = liveData
    return l.observeAsState(initial = i)
}

/**
 * create a state by [liveData]
 * @see MutableLiveData
 * @see observeAsState()
 */
@Composable
inline fun <reified T> state(liveData: MutableLiveData<T>, initial: T): MutableState<T> {
    val state = liveData.observeAsState(initial)
    val setter = { v: T ->
        liveData.postValue(v)
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

/**
 * create a state by [liveData]
 * @see MutableLiveData
 * @see observeAsState()
 */
@Composable
inline fun <reified T> state(liveData: InitialMutableLiveData<T>): MutableState<T> {
    val (l, i) = liveData

    val state = l.observeAsState(i)
    val setter = { v: T ->
        l.postValue(v)
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

inline fun <reified T, R> State<T>.map(crossinline action: (T) -> R): State<R> {
    val state = this
    return object: State<R> {
        override val value: R
            get() = action(state.value)
    }
}