package org.tty.dailyset.common.observable

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations

/**
 * a collector used for transforms of [LiveData].
 * @see [Transformations]
 */
interface LiveCollector<T> {
    /**
     * to emit a real data.
     */
    fun emit(value: T)

    /**
     * to emit a data binding to a datasource.
     */
    fun emitSource(liveData: LiveData<T>)
}