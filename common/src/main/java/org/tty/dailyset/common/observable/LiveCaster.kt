package org.tty.dailyset.common.observable

import androidx.lifecycle.LiveData
import androidx.lifecycle.asFlow
import androidx.lifecycle.asLiveData
import kotlinx.coroutines.flow.Flow

/**
 * a interface provides combination of [Flow] and [LiveData]
 */
sealed interface LiveCaster<T> {
    fun asFlow(): Flow<T>
    fun asLiveData(): LiveData<T>
}

data class FlowLiveCaster<T>(val flow: Flow<T>): LiveCaster<T> {
    override fun asFlow(): Flow<T> {
        return flow;
    }

    override fun asLiveData(): LiveData<T> {
        return flow.asLiveData()
    }
}

data class LiveDataCaster<T>(val liveData: LiveData<T>): LiveCaster<T> {
    override fun asFlow(): Flow<T> {
        return liveData.asFlow()
    }

    override fun asLiveData(): LiveData<T> {
        return liveData
    }
}

inline fun <reified T, reified R> ((T) -> Flow<R?>).toLiveCaster(): ((T) -> LiveCaster<R?>) {
    return { value: T ->
        FlowLiveCaster(invoke(value))
    }
}

inline fun <reified T> Flow<T>.toLiveCaster(): LiveCaster<T> {
    return FlowLiveCaster(this)
}

inline fun <reified T> LiveData<T>.toLiveCaster(): LiveCaster<T> {
    return LiveDataCaster(this)
}