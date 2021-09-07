package org.tty.dailyset.common.observable

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import kotlinx.coroutines.flow.Flow
import org.tty.dailyset.common.local.logger
import kotlin.random.Random

const val TAG = "o.t.d.c.o.LiveDataExtension"

/**
 * to create a [LiveData]
 */
inline fun <reified T> livedata(initial: T): MutableLiveData<T> {
    return MutableLiveData(initial)
}

/**
 * to create a [LiveData] by [flow]
 */
inline fun <reified T> livedata(flow: Flow<T>): LiveData<T> {
    return flow.asLiveData()
}

/**
 * to create a [LiveData] by [source] and [mapper]
 * @param tag the tag to log.
 */
inline fun <reified T, reified R> livedataAsync(source: LiveData<T>, initial: R, tag: String = "$source", crossinline mapper: (T) -> Flow<R?>): LiveData<R> {
    val mutableLiveData = MutableLiveData(initial)

    val signal = Random.nextInt(0, 100)

    source.observeForever { l ->
        logger.d(TAG, "${tag}.source [${signal},async] changed to $l")
        val liveData = mapper.invoke(l).asLiveData()
        liveData.observeForever { value ->
            logger.d(TAG, "${tag}.target [${signal},async] changed to $value")
            if (value != null) {
                mutableLiveData.postValue(value)
            }
        }
    }

    return mutableLiveData
}

inline fun <reified T, reified R> livedataAsync(source: Flow<T>, initial: R, tag: String = "$source", crossinline mapper: (T) -> Flow<R?>): LiveData<R> {
    val liveData = livedata(source)

    val mutableLiveData = MutableLiveData(initial)

    val signal = Random.nextInt(0, 100)

    liveData.observeForever { l ->
        logger.d(TAG, "${tag}.source [${signal},async] changed to $l")
        val liveData2 = mapper.invoke(l).asLiveData()
        liveData2.observeForever { value ->
            logger.d(TAG, "${tag}.target [${signal},async] changed to $value")
            if (value != null) {
                mutableLiveData.postValue(value)
            }
        }
    }

    return mutableLiveData
}

inline fun <reified T, reified R> livedata(source: LiveData<T>, initial: R, tag: String = "$source", crossinline mapper: (T) -> R?): LiveData<R> {
    val mutableLiveData = MutableLiveData(initial)

    val signal = Random.nextInt(0, 100)

    source.observeForever { l ->
        logger.d(TAG, "${tag}.source [$signal] changed to $l")
        val result = mapper(l)
        logger.d(TAG, "${tag}.target [$signal] changed to $result")
        if (result != null) {
            mutableLiveData.postValue(result)
        }
    }

    return mutableLiveData
}

inline fun <reified T, reified R> livedata(flow: Flow<T>, initial: R, tag: String = "$flow", crossinline mapper: (T) -> R?): LiveData<R> {
    val liveData = flow.asLiveData()
    return livedata(liveData, initial, tag, mapper)
}