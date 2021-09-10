package org.tty.dailyset.common.observable

import androidx.lifecycle.LiveData

interface LiveCollector<T> {
    fun emit(value: T)
    fun emitSource(liveData: LiveData<T>)
}