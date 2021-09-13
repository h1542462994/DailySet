package org.tty.dailyset.common.observable

import androidx.compose.runtime.livedata.observeAsState
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

/**
 * data class combines [liveData] and [initial].
 */
data class InitialLiveData<T>(val liveData: LiveData<T>, val initial: T)


/**
 * data class combines [liveData] and [initial].
 */
data class InitialMutableLiveData<T>(val liveData: MutableLiveData<T>, val initial: T)

/**
 * to create a [InitialLiveData], so you can use [observeAsState] without a initial data.
 * <pre>
 *     val userLiveData: LiveData<User> = ...
 *     val user = userLiveData.initial(User.default())
 *
 *     val user = state(user)
 * </pre>
 */
inline fun <reified T> LiveData<T>.initial(initial: T): InitialLiveData<T> {
    return InitialLiveData(this, initial)
}

/**
 * to create a [InitialMutableLiveData], so you can use [observeAsState] without a initial data.
 * <pre>
 *     val userLiveData: MutableLiveData<User> = ...
 *     val user = userLiveData.initial(User.default())
 *
 *     val user = state(user)
 * </pre>
 */
inline fun <reified T> MutableLiveData<T>.initial(initial: T): InitialMutableLiveData<T> {
    return InitialMutableLiveData(this, initial)
}
