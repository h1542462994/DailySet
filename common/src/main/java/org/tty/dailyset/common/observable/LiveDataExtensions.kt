package org.tty.dailyset.common.observable

import android.annotation.SuppressLint
import androidx.lifecycle.*
import kotlinx.coroutines.flow.Flow
import org.jetbrains.annotations.NotNull
import org.tty.dailyset.common.local.logger
import org.tty.dioc.observable.channel.Channels
import org.tty.dioc.observable.channel.observe
import kotlin.random.Random

private const val TAG = "o.t.d.c.observable.LiveDataExtension"

//region liveDataBase

/**
 * to create a [InitialMutableLiveData]
 */
inline fun <reified T> liveData(@NotNull initial: T): MutableLiveData<T> {
    return MutableLiveData(initial)
}

/**
 * to create a [LiveData] by [flow]
 */
inline fun <reified T> liveData(flow: Flow<T>): LiveData<T> {
    return flow.asLiveData()
}

fun <T> liveDataIgnoreNull(flow: Flow<T?>, tag: String = "$flow"): LiveData<T> {
    val mediator = MediatorLiveData<T>()
    val signal = Random.nextInt(0, 100)
    mediator.addSource(flow.asLiveData()) { value ->
        logger.d(TAG, "$tag.target [$signal] changed to $value")
        mediator.value = value
    }
    return mediator
}

inline fun <reified T> LiveData<T>.initial(initial: T): InitialLiveData<T> {
    return InitialLiveData(this, initial)
}

inline fun <reified T> MutableLiveData<T>.initial(initial: T): InitialMutableLiveData<T> {
    return InitialMutableLiveData(this, initial)
}

//endregion

//region liveDataChain1

/**
 * to create a [LiveData] by [source] and [mapper] (sync version)
 * @param source the source liveData.
 * @param action transform from source to real data (async).
 * @param tag the tag for logging.
 */
@SuppressLint("NullSafeMutableLiveData")
fun <T, R> liveDataChain(source: LiveData<T>, tag: String = "$source", action: (value: T, collector: LiveCollector<R>) -> Unit): LiveData<R> {
    val mediator = MediatorLiveData<R>()
    val signal = Random.nextInt(0, 100)
    lateinit var middleLiveData: LiveData<R>
    var assigned = false

    val collector = object: LiveCollector<R> {
        override fun emit(value: R) {
            if (assigned) {
                assigned = false
                mediator.removeSource(mediator)
            }
            logger.d(TAG, "$tag.target [$signal] changed to $value")
            mediator.value = value
        }

        override fun emitSource(liveData: LiveData<R>) {
            if (assigned) {
                if (middleLiveData == liveData) {
                    return
                }
                mediator.removeSource(middleLiveData)
            }
            logger.d(TAG, "$tag.liveData [$signal, async] changed to $liveData")
            mediator.addSource(liveData) { v ->
                logger.d(TAG, "$tag.target [$signal, async] changed to $v")
                mediator.value = v
            }
            assigned = true
            middleLiveData = liveData
        }
    }

    mediator.addSource(source) { s ->
        logger.d(TAG, "$tag.source [$signal] changed to $s")
        action(s, collector)
    }

    return mediator
}

/**
 * to create a [LiveData] by [source] and [mapper] (sync version)
 * @param source the source liveData.
 * @param mapper transform from source to real data (async).
 * @param tag the tag for logging.
 */
inline fun <reified T, reified R> liveDataMap(source: LiveData<T>, tag: String = "$source", noinline mapper: (T) -> R): LiveData<R> {
    return liveDataChain(source, tag) { value, collector ->
        collector.emit(mapper(value))
    }
}

/**
 * to create a [LiveData] by [source] and [mapper] (sync version)
 * @param source the source liveData.
 * @param mapper transform from source to real data (async).
 * @param tag the tag for logging.
 */
inline fun <reified T, reified R> liveDataMapIgnoreNull(source: LiveData<T>, tag: String = "$source", noinline mapper: (T) -> R?): LiveData<R> {
    return liveDataChain(source, tag) { value, collector ->
        val r = mapper(value)
        if (r != null) {
            collector.emit(r)
        }
    }
}

inline fun <reified T, reified R> liveDataMapAsync(source: LiveData<T>, tag: String = "$source", noinline mapper: (T) -> Flow<R>): LiveData<R> {
    return liveDataChain(source, tag) { value, collector ->
        val flow = mapper(value)
        collector.emitSource(liveData(flow))
    }
}

inline fun <reified T, reified R> liveDataMapAsyncIgnoreNull(source: LiveData<T>, tag: String = "$source", noinline mapper: (T) -> Flow<R?>): LiveData<R> {
    return liveDataChain(source, tag) { value, collector ->
        collector.emitSource(liveDataIgnoreNull(mapper(value), "$source.flow"))
    }
}

//endregion

//region liveDataChain2
//endregion

//region liveDataChain3
//endregion

//region liveDataVarargs

fun <T, R> liveDataVarargsChain(vararg sources: LiveData<T>, tag: String = "$sources", action: (values: List<T>, collector: LiveCollector<R>) -> Unit): LiveData<R> {
    val mediator = MediatorLiveData<R>()
    val signal = Random.nextInt(0, 100)
    lateinit var middleLiveData: LiveData<R>
    var assigned = false

    val collector = object: LiveCollector<R> {
        override fun emit(value: R) {
            if (assigned) {
                assigned = false
                mediator.removeSource(mediator)
            }
            logger.d(TAG, "$tag.target [$signal] changed to $value")
            mediator.value = value!!
        }

        override fun emitSource(liveData: LiveData<R>) {
            if (assigned) {
                if (middleLiveData == liveData) {
                    return
                }
                mediator.removeSource(middleLiveData)
            }
            logger.d(TAG, "$tag.liveData [$signal, async] changed to $liveData")
            mediator.addSource(liveData) { v ->
                logger.d(TAG, "$tag.target [$signal, async] changed to $v")
                mediator.value = v
            }
            assigned = true
            middleLiveData = liveData
        }
    }

    val length = sources.size
    val channels = List(length) {
        Channels.create<T>()
    }
    Channels.record(*channels.toTypedArray()).observe { values ->
        action(values, collector)
    }

    sources.forEachIndexed { index, liveData ->
        mediator.addSource(liveData) { value ->
            logger.d(TAG, "$tag.source($index) changed to $value")
            channels[index].emit(value)
        }
    }

    return mediator
}

fun <T, R> liveDataVarargsMap(vararg sources: LiveData<T>, tag: String = "$sources", mapper: (values: List<T>) -> R): LiveData<R> {
    return liveDataVarargsChain(sources = sources, tag = tag) { values, collector ->
        collector.emit(mapper(values))
    }
}


//endregion

//region liveDataFlatmap
//endregion
