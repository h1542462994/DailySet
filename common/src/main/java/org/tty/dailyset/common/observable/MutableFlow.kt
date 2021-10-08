package org.tty.dailyset.common.observable

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asFlow
import kotlinx.coroutines.flow.Flow

interface MutableFlow<T>: Flow<T> {
    fun postValue(value: T)
}

internal class MutableFlowImpl<T>(private val mutableLiveData: MutableLiveData<T>): MutableFlow<T>, Flow<T> by mutableLiveData.asFlow() {
    override fun postValue(value: T) {
        mutableLiveData.postValue(value)
    }
}
