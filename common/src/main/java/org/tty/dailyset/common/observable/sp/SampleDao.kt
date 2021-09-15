package org.tty.dailyset.common.observable.sp

import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.flow.Flow

@SharePreferenceDao(name = "settings")
interface SampleDao {
    @SharePreferenceKey
    var userUid: String

    @SharePreferenceKey
    val userUidFlow: Flow<String>

    @SharePreferenceKey
    val userUidLiveData: MutableLiveData<String>
}