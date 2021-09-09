package org.tty.dailyset.common.observable

import android.annotation.SuppressLint
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import kotlinx.coroutines.flow.Flow
import org.tty.dailyset.common.local.logger
import org.tty.dioc.observable.channel.Channels
import org.tty.dioc.observable.channel.observe
import kotlin.random.Random

private const val TAG = "o.t.d.c.observable.LiveDataExtension"

/**
 * to create a [InitialMutableLiveData]
 */
inline fun <reified T> liveData(initial: T): InitialMutableLiveData<T> {
    return InitialMutableLiveData(MutableLiveData(), initial)
}

/**
 * to create a [LiveData] by [flow]
 */
inline fun <reified T> liveData(flow: Flow<T>): LiveData<T> {
    return flow.asLiveData()
}

inline fun <reified T> liveData(flow: Flow<T>, initial: T): InitialLiveData<T> {
    return InitialLiveData(flow.asLiveData(), initial)
}

/**
 * to create a [LiveData] by [source] and [mapper] (sync version)
 * @param source the source liveData.
 * @param mapper transform from source to real data (async).
 * @param initial the initial of the result livedata.
 * @param tag the tag for logging.
 */
@SuppressLint("NullSafeMutableLiveData")
fun <T, R> liveData(source: LiveData<T>, initial: R, tag: String = "$source", mapper: (T) -> R?): InitialLiveData<R> {
    val mediator = MediatorLiveData<R>()
    val signal = Random.nextInt(0, 100)
    mediator.addSource(source) { s ->
        logger.d(TAG, "$tag.source [$signal] changed to $s")
        val v = mapper.invoke(s)
        if (v != null) {
            logger.d(TAG, "$tag.target [$signal] changed to $v")
            mediator.value = v
        }
    }

    return InitialLiveData(mediator, initial)
}

/**
 * to create a [LiveData] by [source] and [mapper] (sync version)
 * @param source the source liveData.
 * @param mapper transform from source to real data (async).
 * @param initial the initial of the result livedata.
 * @param tag the tag for logging.
 */
inline fun <reified T, reified R> liveData(source: Flow<T>, initial: R, tag: String = "$source", noinline mapper: (T) -> R?): InitialLiveData<R> {
    return liveData(source.asLiveData(), initial, tag, mapper)
}

fun <T1, T2, R> liveData2(source1: LiveData<T1>, source2: LiveData<T2>, initial: R, tag: String = "$source1,$source2", mapper: (T1, T2) -> R?): InitialLiveData<R> {
    val mediator = MediatorLiveData<R>()
    val signal = Random.nextInt(0, 100)
    val channel1 = Channels.create<T1>()
    val channel2 = Channels.create<T2>()
    Channels.record(channel1, channel2).observe {
        mediator.value = mapper(it.first, it.second)
    }


}

/**
 * to create a [LiveData] by [source] and [mapper] (async version)
 * @param source the source liveData.
 * @param mapper transform from source to real data (async).
 * @param initial the initial of the result livedata.
 * @param tag the tag for logging.
 */
fun <T, R> liveDataAsync(source: LiveCaster<T>, initial: R, tag: String = "$source", mapper: (T) -> LiveCaster<R?>): InitialLiveData<R> {
    val mediator = MediatorLiveData<R>()
    val sourceLiveData = source.asLiveData()
    val signal = Random.nextInt(0, 100)
    lateinit var middleLiveData: LiveData<R?>
    var assigned = false

    mediator.addSource(sourceLiveData) { s ->
        logger.d(TAG, "$tag.source [$signal, async] changed to $s")
        val newLiveData = mapper.invoke(s).asLiveData()
        if (assigned) {
            if (middleLiveData == newLiveData) {
                return@addSource
            }
            mediator.removeSource(middleLiveData)
        }
        logger.d(TAG, "$tag.liveData [$signal, async] changed to $newLiveData")
        mediator.addSource(newLiveData) { value ->
            logger.d(TAG, "$tag.target [$signal, async] changed to $value")
            mediator.value = value
        }
        assigned = true
        middleLiveData = newLiveData
    }

    return InitialLiveData(mediator, initial)
}

/**
 * to create a [LiveData] by [source] and [mapper] (async version)
 * @param source the source liveData.
 * @param mapper transform from source to real data (async).
 * @param initial the initial of the result livedata.
 * @param tag the tag for logging.
 */
inline fun <reified T, reified R> liveDataAsync(source: Flow<T>, initial: R, tag: String = "$source", crossinline mapper: (T) -> Flow<R?>): InitialLiveData<R> {
    return liveDataAsync(source.toLiveCaster(), initial, tag, mapper.toLiveCaster())
}

/**
 * to create a [LiveData] by [source] and [mapper] (async version)
 * @param source the source liveData.
 * @param mapper transform from source to real data (async).
 * @param initial the initial of the result livedata.
 * @param tag the tag for logging.
 */
inline fun <reified T , reified R> liveDataAsync(source: LiveData<T>, initial: R, tag: String = "$source", crossinline mapper: (T) -> Flow<R?>): InitialLiveData<R> {

    return liveDataAsync(source.toLiveCaster(), initial, tag, mapper.toLiveCaster())
}
