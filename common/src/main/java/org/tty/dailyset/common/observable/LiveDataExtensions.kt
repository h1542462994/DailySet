package org.tty.dailyset.common.observable

import android.annotation.SuppressLint
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import kotlinx.coroutines.flow.Flow
import org.jetbrains.annotations.NotNull
import org.tty.dailyset.common.local.Tags
import org.tty.dailyset.common.local.logger
import org.tty.dioc.observable.channel.Channels
import org.tty.dioc.observable.channel.observe

@Suppress("unused")
val Tags.liveDataExtension: String
    get() = "o.t.d.c.observable.LiveDataExtension"

private fun logDIfTag(moduleTag: String, tag: String?, content: String) {
    if (tag != null) {
        logger.d(moduleTag, content)
    }
}

//region liveDataBase

/**
 * create a [MutableLiveData] with [initial]
 */
inline fun <reified T> liveData(@NotNull initial: T): MutableLiveData<T> {
    return MutableLiveData(initial)
}

/**
 * create a [LiveData] with [flow]
 */
inline fun <reified T> liveData(flow: Flow<T>): LiveData<T> {
    return flow.asLiveData()
}

/**
 * create a [LiveData] with [liveData] and ignore null value.
 * if the liveData changes to null, the result will maintain the value.
 */
fun <T> liveDataIgnoreNull(liveData: LiveData<T?>): LiveData<T> {
    val mediator = MediatorLiveData<T>()
    mediator.addSource(liveData) { value ->
        if (value != null) {
            mediator.value = value
        }
    }
    return mediator
}

/**
 * create a [LiveData] with [flow] and ignore null value.
 * if the liveData changes to null, the result will maintain the value.
 */
inline fun <reified T> liveDataIgnoreNull(flow: Flow<T?>): LiveData<T> =
    liveDataIgnoreNull(liveData(flow))

fun <T> liveDataValueOrDefault(liveData: LiveData<T?>, default: T): LiveData<T> {
    val mediator = MediatorLiveData<T>()
    mediator.addSource(liveData) { value ->
        if (value != null) {
            mediator.value = value
        } else {
            @Suppress("USELESS_CAST")
            mediator.value = default as T
        }
    }
    return mediator
}


/**
 * shortcut function for [liveDataIgnoreNull]
 */
inline fun <reified T> LiveData<T?>.ignoreNull(): LiveData<T> =
    liveDataIgnoreNull(this)


//endregion

//region liveDataChain1

/**
 * to create a [LiveData] by [source] and [action]
 * @param source the source liveData.
 * @param action transform from source to real data.
 * @param tag the tag for logging.
 */
@SuppressLint("NullSafeMutableLiveData")
fun <T, R> liveDataChain(
    source: LiveData<T>,
    tag: String? = null,
    action: (value: T, collector: LiveCollector<R>) -> Unit
): LiveData<R> {
    val mediator = MediatorLiveData<R>()

    lateinit var middleLiveData: LiveData<R>
    var assigned = false

    val collector = object : LiveCollector<R> {
        override fun emit(value: R) {
            if (assigned) {
                assigned = false
                mediator.removeSource(mediator)
            }
            logDIfTag(Tags.liveDataExtension, tag, "$tag.target ->  $value")
            mediator.value = value
        }

        override fun emitSource(liveData: LiveData<R>) {
            if (assigned) {
                if (middleLiveData == liveData) {
                    return
                }
                mediator.removeSource(middleLiveData)
            }
            mediator.addSource(liveData) { v ->
                logDIfTag(Tags.liveDataExtension, tag, "$tag.target (async) ->  $v")
                mediator.value = v
            }
            assigned = true
            middleLiveData = liveData
        }
    }
    mediator.addSource(source) { s ->
        logDIfTag(Tags.liveDataExtension, tag, "$tag.source ->  $s")
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
inline fun <reified T, reified R> liveDataMap(
    source: LiveData<T>,
    tag: String? = null,
    noinline mapper: (T) -> R
): LiveData<R> {
    return liveDataChain(source, tag) { value, collector ->
        collector.emit(mapper(value))
    }
}

/**
 * to create a [LiveData] by [source] and [mapper] (sync version) and ignore null value.
 * @param source the source liveData.
 * @param mapper transform from source to real data (sync).
 * @param tag the tag for logging.
 * @see liveDataChain
 */
inline fun <reified T, reified R> liveDataMapIgnoreNull(
    source: LiveData<T>,
    tag: String? = null,
    noinline mapper: (T) -> R?
): LiveData<R> {
    return liveDataChain(source, tag) { value, collector ->
        val r = mapper(value)
        if (r != null) {
            collector.emit(r)
        }
    }
}

/**
 * to create a [LiveData] by [source] and [mapper] (async version)
 * @param source the source liveData.
 * @param mapper transform from source to real data (async).
 * @param tag the tag for logging.
 * @see liveDataChain
 */
inline fun <reified T, reified R> liveDataMapAsync(
    source: LiveData<T>,
    tag: String? = null,
    noinline mapper: (T) -> Flow<R>
): LiveData<R> {
    return liveDataChain(source, tag) { value, collector ->
        val flow = mapper(value)
        collector.emitSource(liveData(flow))
    }
}

/**
 * to create a [LiveData] by [source] and [mapper] (async version) and ignore null value.
 * @param source the source liveData.
 * @param mapper transform from source to real data (async).
 * @param tag the tag for logging.
 * @see liveDataChain
 * @see liveDataIgnoreNull
 */
inline fun <reified T, reified R> liveDataMapAsyncIgnoreNull(
    source: LiveData<T>,
    tag: String? = null,
    noinline mapper: (T) -> Flow<R?>
): LiveData<R> {
    return liveDataChain(source, tag) { value, collector ->
        collector.emitSource(liveDataIgnoreNull(mapper(value)))
    }
}
//endregion

//region liveDataChain2

/**
 * to create a [LiveData] by [source1], [source2] and [action].
 * if the source1 or source2 changed to newData, the [action] will be invoked.
 * @param source1 the source liveData part first.
 * @param source2 the source liveData part second.
 * @param action transform from source to real data.
 * @param tag the tag for logging.
 * @see liveDataVarargsChain
 */

inline fun <reified T1, reified T2, R> liveData2Chain(
    source1: LiveData<T1>,
    source2: LiveData<T2>,
    tag: String? = null,
    noinline action: (value1: T1, value2: T2, collector: LiveCollector<R>) -> Unit
): LiveData<R> {
    return liveDataVarargsChain(sources = arrayOf(
        liveDataMap(source1) { it },
        liveDataMap(source2) { it }), tag
    ) { values, collector ->
        action(values[0] as T1, values[1] as T2, collector)
    }
}

/**
 * to create a [LiveData] by [source1], [source2] and [mapper] (sync version).
 * if the source1 or source2 changed to newData, the [mapper] will be invoked.
 * @param source1 the source liveData part first.
 * @param source2 the source liveData part second.
 * @param mapper transform from source to real data.
 * @param tag the tag for logging.
 * @see liveData2Chain
 */
inline fun <reified T1, reified T2, R> liveData2Map(
    source1: LiveData<T1>,
    source2: LiveData<T2>,
    tag: String? = null,
    noinline mapper: (value1: T1, value2: T2) -> R
): LiveData<R> {
    return liveData2Chain(source1, source2, tag) { value1, value2, collector ->
        collector.emit(mapper(value1, value2))
    }
}

/**
 * to create a [LiveData] by [source1], [source2] and [mapper] (sync version). and ignore null value.
 * if the source1 or source2 changed to newData, the [mapper] will be invoked.
 * @param source1 the source liveData part first.
 * @param source2 the source liveData part second.
 * @param mapper transform from source to real data.
 * @param tag the tag for logging.
 * @see liveData2Chain
 */
inline fun <reified T1, reified T2, R> liveData2MapIgnoreNull(
    source1: LiveData<T1>,
    source2: LiveData<T2>,
    tag: String? = null,
    noinline mapper: (value1: T1, value2: T2) -> R?
): LiveData<R> {
    return liveData2Chain(source1, source2, tag) { value1, value2, collector ->
        val r = mapper(value1, value2)
        if (r != null) {
            collector.emit(r)
        }
    }
}

/**
 * to create a [LiveData] by [source1], [source2] and [mapper] (async version).
 * if the source1 or source2 changed to newData, the [mapper] will be invoked.
 * @param source1 the source liveData part first.
 * @param source2 the source liveData part second.
 * @param mapper transform from source to real data.
 * @param tag the tag for logging.
 * @see liveData2Chain
 */
inline fun <reified T1, reified T2, reified R> liveData2MapAsync(
    source1: LiveData<T1>,
    source2: LiveData<T2>,
    tag: String? = null,
    noinline mapper: (value1: T1, value2: T2) -> Flow<R>
): LiveData<R> {
    return liveData2Chain(source1, source2, tag) { value1, value2, collector ->
        collector.emitSource(liveData(mapper(value1, value2)))
    }
}

/**
 * to create a [LiveData] by [source1], [source2] and [mapper] (async version). and ignore null value.
 * if the source1 or source2 changed to newData, the [mapper] will be invoked.
 * @param source1 the source liveData part first.
 * @param source2 the source liveData part second.
 * @param mapper transform from source to real data.
 * @param tag the tag for logging.
 * @see liveData2Chain
 */
inline fun <reified T1, reified T2, reified R> liveData2MapAsyncIgnoreNull(
    source1: LiveData<T1>,
    source2: LiveData<T2>,
    tag: String? = null,
    noinline mapper: (value1: T1, value2: T2) -> Flow<R?>
): LiveData<R> {
    return liveData2Chain(source1, source2, tag) { value1, value2, collector ->
        collector.emitSource(liveDataIgnoreNull(mapper(value1, value2)))
    }
}
//endregion

//region liveDataChain3

/**
 * to create a [LiveData] by [source1], [source2], [source3] and [action].
 * if the source1 or source2 changed to newData, the [action] will be invoked.
 * @param source1 the source liveData part first.
 * @param source2 the source liveData part second.
 * @param source3 the source liveData part third.
 * @param action transform from source to real data.
 * @param tag the tag for logging.
 * @see liveDataVarargsChain
 */
inline fun <reified T1, reified T2, reified T3, R> liveData3Chain(
    source1: LiveData<T1>,
    source2: LiveData<T2>,
    source3: LiveData<T3>,
    tag: String? = null,
    noinline action: (value1: T1, value2: T2, value3: T3, collector: LiveCollector<R>) -> Unit
): LiveData<R> {
    return liveDataVarargsChain(sources = arrayOf(
        liveDataMap(source1) { it },
        liveDataMap(source2) { it },
        liveDataMap(source3) { it }), tag
    ) { values, collector ->
        action(values[0] as T1, values[1] as T2, values[2] as T3, collector)
    }
}

/**
 * to create a [LiveData] by [source1], [source2], [source3] and [mapper] (sync version).
 * if the source1 or source2 changed to newData, the [mapper] will be invoked.
 * @param source1 the source liveData part first.
 * @param source2 the source liveData part second.
 * @param source3 the source liveData part third.
 * @param mapper transform from source to real data.
 * @param tag the tag for logging.
 * @see liveData3Chain
 */
inline fun <reified T1, reified T2, reified T3, R> liveData3Map(
    source1: LiveData<T1>,
    source2: LiveData<T2>,
    source3: LiveData<T3>,
    tag: String? = null,
    noinline mapper: (value1: T1, value2: T2, value3: T3) -> R
): LiveData<R> {
    return liveData3Chain(source1, source2, source3, tag) { value1, value2, value3, collector ->
        collector.emit(mapper(value1, value2, value3))
    }
}

/**
 * to create a [LiveData] by [source1], [source2], [source3] and [mapper] (sync version). and ignore null value.
 * if the source1 or source2 changed to newData, the [mapper] will be invoked.
 * @param source1 the source liveData part first.
 * @param source2 the source liveData part second.
 * @param source3 the source liveData part third.
 * @param mapper transform from source to real data.
 * @param tag the tag for logging.
 * @see liveData3Chain
 */
inline fun <reified T1, reified T2, reified T3, R> liveData3MapIgnoreNull(
    source1: LiveData<T1>,
    source2: LiveData<T2>,
    source3: LiveData<T3>,
    tag: String? = null,
    noinline mapper: (value1: T1, value2: T2, value3: T3) -> R?
): LiveData<R> {
    return liveData3Chain(source1, source2, source3, tag) { value1, value2, value3, collector ->
        val r = mapper(value1, value2, value3)
        if (r != null) {
            collector.emit(r)
        }
    }
}

/**
 * to create a [LiveData] by [source1], [source2], [source3] and [mapper] (async version).
 * if the source1 or source2 changed to newData, the [mapper] will be invoked.
 * @param source1 the source liveData part first.
 * @param source2 the source liveData part second.
 * @param source3 the source liveData part third.
 * @param mapper transform from source to real data.
 * @param tag the tag for logging.
 * @see liveData3Chain
 */
inline fun <reified T1, reified T2, reified T3, reified R> liveData3MapAsync(
    source1: LiveData<T1>,
    source2: LiveData<T2>,
    source3: LiveData<T3>,
    tag: String? = null,
    noinline mapper: (value1: T1, value2: T2, value3: T3) -> Flow<R>
): LiveData<R> {
    return liveData3Chain(source1, source2, source3, tag) { value1, value2, value3, collector ->
        collector.emitSource(liveData(mapper(value1, value2, value3)))
    }
}

/**
 * to create a [LiveData] by [source1], [source2], [source3] and [mapper] (async version). and ignore null value.
 * if the source1 or source2 changed to newData, the [mapper] will be invoked.
 * @param source1 the source liveData part first.
 * @param source2 the source liveData part second.
 * @param source3 the source liveData part third.
 * @param mapper transform from source to real data.
 * @param tag the tag for logging.
 * @see liveData3Chain
 */
inline fun <reified T1, reified T2, reified T3, reified R> liveData3MapAsyncIgnoreNull(
    source1: LiveData<T1>,
    source2: LiveData<T2>,
    source3: LiveData<T3>,
    tag: String? = null,
    noinline mapper: (value1: T1, value2: T2, value3: T3) -> Flow<R?>
): LiveData<R> {
    return liveData3Chain(source1, source2, source3, tag) { value1, value2, value3, collector ->
        collector.emitSource(liveDataIgnoreNull(mapper(value1, value2, value3)))
    }
}

//endregion

//region liveDataVarargs

/**
 * to create a [LiveData] by [sources] and [action].
 * if the any of the sources changed to newData, the [action] will be invoked.
 * @param sources the source liveData part first.
 * @param action transform from source to real data.
 * @param tag the tag for logging.
 * @see liveDataVarargsChain
 */
fun <T, R> liveDataVarargsChain(
    vararg sources: LiveData<T>,
    tag: String? = null,
    action: (values: List<T>, collector: LiveCollector<R>) -> Unit
): LiveData<R> {
    val mediator = MediatorLiveData<R>()

    lateinit var middleLiveData: LiveData<R>
    var assigned = false

    val collector = object : LiveCollector<R> {
        override fun emit(value: R) {
            if (assigned) {
                assigned = false
                mediator.removeSource(mediator)
            }
            logDIfTag(Tags.liveDataExtension, tag, "$tag.target ->  $value" )
            mediator.value = value!!
        }

        override fun emitSource(liveData: LiveData<R>) {
            if (assigned) {
                if (middleLiveData == liveData) {
                    return
                }
                mediator.removeSource(middleLiveData)
            }
            mediator.addSource(liveData) { v ->
                logDIfTag(Tags.liveDataExtension, tag,"$tag.target (async) ->  $v")
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
            logDIfTag(Tags.liveDataExtension, tag, "$tag.source[$index] ->  $value")
            channels[index].emit(value)
        }
    }



    return mediator
}

/**
 * to create a [LiveData] by [sources] and [mapper]. (sync version)
 * if the any of the sources changed to newData, the [mapper] will be invoked.
 * @param sources the source liveData part first.
 * @param mapper transform from source to real data.
 * @param tag the tag for logging.
 * @see liveDataVarargsChain
 */
inline fun <reified T, reified R> liveDataVarargsMap(
    vararg sources: LiveData<T>,
    tag: String? = null,
    noinline mapper: (values: List<T>) -> R
): LiveData<R> {
    return liveDataVarargsChain(sources = sources, tag = tag) { values, collector ->
        collector.emit(mapper(values))
    }
}

/**
 * to create a [LiveData] by [sources] and [mapper]. (sync version) and ignore null value.
 * if the any of the sources changed to newData, the [mapper] will be invoked.
 * @param sources the source liveData part first.
 * @param mapper transform from source to real data.
 * @param tag the tag for logging.
 * @see liveDataVarargsChain
 */
inline fun <reified T, reified R> liveDataVarargsMapIgnoreNull(
    vararg sources: LiveData<T>,
    tag: String? = null,
    noinline mapper: (values: List<T>) -> R?
): LiveData<R> {
    return liveDataVarargsChain(sources = sources, tag = tag) { values, collector ->
        val r = mapper(values)
        if (r != null) {
            collector.emit(r)
        }
    }
}

/**
 * to create a [LiveData] by [sources] and [mapper]. (async version)
 * if the any of the sources changed to newData, the [mapper] will be invoked.
 * @param sources the source liveData part first.
 * @param mapper transform from source to real data.
 * @param tag the tag for logging.
 * @see liveDataVarargsChain
 */
inline fun <reified T, reified R> liveDataVarargsMapAsync(
    vararg sources: LiveData<T>,
    tag: String? = null,
    noinline mapper: (values: List<T>) -> Flow<R>
): LiveData<R> {
    return liveDataVarargsChain(sources = sources, tag = tag) { values, collector ->
        return@liveDataVarargsChain collector.emitSource(liveData(mapper(values)))
    }
}

/**
 * to create a [LiveData] by [sources] and [mapper]. (async version) and ignore null value.
 * if the any of the sources changed to newData, the [mapper] will be invoked.
 * @param sources the source liveData part first.
 * @param mapper transform from source to real data.
 * @param tag the tag for logging.
 * @see liveDataVarargsChain
 */
inline fun <reified T, reified R> liveDataVarargsMapAsyncNotNull(
    vararg sources: LiveData<T>,
    tag: String? = null,
    noinline mapper: (values: List<T>) -> Flow<R?>
): LiveData<R> {
    return liveDataVarargsChain(sources = sources, tag = tag) { values, collector ->
        return@liveDataVarargsChain collector.emitSource(liveDataIgnoreNull(mapper(values)))
    }
}

//endregion

//region liveData


//endregion
