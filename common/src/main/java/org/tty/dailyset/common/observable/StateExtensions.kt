package org.tty.dailyset.common.observable

import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

//region state by value

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
 * create a [MutableState] to remember the [value]
 * @see remember
 * @see mutableStateOf
 */
@Composable
inline fun <reified T> state(value: T, key1: Any?, policy: SnapshotMutationPolicy<T> = structuralEqualityPolicy()): MutableState<T> {
    return remember(key1 = key1) {
        mutableStateOf(value = value, policy = policy)
    }
}

//endregion

//region state by liveData

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

//endregion

//region state by flow

@Composable
inline fun <reified T> state(flow: InitialFlow<T>): State<T> {
    val (f, i) = flow
    return f.collectAsState(initial = i)
}

@Composable
inline fun <reified T> state(flow: InitialMutableFlow<T>): MutableState<T> {
    val (f, i) = flow
    val state = f.collectAsState(i)
    val setter = { v: T ->
        f.postValue(v)
    }

    return object: MutableState<T> {
        override var value: T
            get() = state.value
            set(value) { setter.invoke(value) }

        override fun component1(): T = value
        override fun component2(): (T) -> Unit = setter
    }
}

//endregion