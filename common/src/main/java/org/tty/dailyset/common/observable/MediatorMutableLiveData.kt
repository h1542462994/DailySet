package org.tty.dailyset.common.observable

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData

open class MediatorMutableLiveData<T>(
    private val mutableLiveData: MutableLiveData<T>
): MediatorLiveData<T>() {
    override fun postValue(value: T) {
        // delegate the postValue to mutableLiveData
        mutableLiveData.postValue(value)
    }
}