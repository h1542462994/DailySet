package org.tty.dailyset.common.observable

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

/**
 * combination of [liveData] and [initial]
 */
data class InitialLiveData<T>(val liveData: LiveData<T>, val initial: T)

/**
 * combination of [liveData] and [initial]
 */
data class InitialMutableLiveData<T>(val liveData: MutableLiveData<T>, val initial: T)


