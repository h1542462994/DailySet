package org.tty.dailyset.viewmodel

import android.util.Log
import androidx.compose.runtime.livedata.observeAsState
import androidx.lifecycle.*
import kotlinx.coroutines.launch
import org.tty.dailyset.DailySetApplication
import org.tty.dailyset.model.entity.*
import org.tty.dailyset.model.lifetime.dailyset.ClazzDailySetCursor
import org.tty.dailyset.model.lifetime.dailyset.ClazzDailySetState
import org.tty.dailyset.model.lifetime.dailytable.DailyTableState2
import org.tty.dailyset.ui.page.MainPageTabs

class MainViewModel(val service: DailySetApplication): ViewModel() {

    /**
     * init the viewModel
     */
    fun init() {
        registerDailyTableHook()
        registerDailySetHook()
    }



    //region main scope
    private var _mainTab = MutableLiveData(MainPageTabs.DAILY_SET)
    val mainTab = _mainTab
    val setMainTab: (MainPageTabs) -> Unit = { tab ->
        viewModelScope.launch {
            _mainTab.value = tab
        }
    }
    //endregion

    //region profile scope
    val seedVersionPreference: LiveData<Preference?> = service.preferenceRepository.seedVersionPreference.asLiveData()
    val seedVersion: LiveData<Int> = service.preferenceRepository.seedVersion.asLiveData()
    val currentUserUid: LiveData<String> = service.preferenceRepository.currentUserUid.asLiveData()
    val users: LiveData<List<User>> = service.userRepository.users.asLiveData()
    //endregion

    //region dailyTable.settings scope
    val dailyTableSummaries = service.dailyTableRepository.dailyTableSummaries.asLiveData()
    val currentDailyTableUid = MutableLiveData(DailyTable.default)

    var currentDailyTRC = MutableLiveData<LiveData<DailyTRC?>>()
        internal set
    var currentDailyTableState2 = MutableLiveData(DailyTableState2.default())
        internal set

    private fun registerDailyTableHook() {
        currentDailyTableUid.observeForever {
            Log.d(TAG, "currentDailyTableUid changed to $it")
            postCurrentDailyTRC(it)
        }
        currentDailyTRC.observeForever {
            Log.d(TAG, "currentDailyTRC.livedata changed to ${it.value}")
            // 标记，仅允许在currentDailyTRC改变后重写一次。
            var oneTick = true
            it.observeForever { it2 ->
                // 在这里设置了拦截，奇怪的是，it2会在数据库修改阶段变为DailyTRC.default()
                if (oneTick || it2 != DailyTRC.default()) {
                    Log.d(TAG, "currentDailyTRC changed to $it2")
                    if (it2 != null) {
                        // TODO: 2021/6/24 hook to user.
                        postCurrentDailyTableState2(dailyTRC = it2, currentUserUid = currentUserUid.value)
                    }
                    oneTick = false
                }
            }
        }
        currentDailyTableState2.observeForever {
            Log.d(TAG, "currentDailyTableState2 changed to $it")
        }
    }

    private fun postCurrentDailyTRC(currentDailyTableUid: String = DailyTable.default) {
        currentDailyTRC.postValue(
            service.dailyTableRepository.loadDailyTRC(currentDailyTableUid).asLiveData()
        )
    }

    private fun postCurrentDailyTableState2(dailyTRC: DailyTRC, currentUserUid: String?) {
        currentDailyTableState2.postValue(DailyTableState2.of(dailyTRC = dailyTRC, currentUserUid))
    }
    //endregion

    //region dailySet scope
    val dailySets = service.dailySetRepository.dailySets.asLiveData()
    val normalDailyDurations = service.dailySetRepository.normalDailyDurations.asLiveData()
    val clazzDailyDurations = service.dailySetRepository.clazzDailyDurations.asLiveData()

    val currentDailySetUid = MutableLiveData("")
    @Deprecated("use currentDailySetDurationsLiveData instead.")
    val currentDailySetLiveData = MutableLiveData<LiveData<DailySet?>>()
    @Deprecated("use currentDailySet instead.")
    val currentDailySet = MutableLiveData(DailySet.empty())
    val currentDailySetDurationsLiveData = MutableLiveData<LiveData<DailySetDurations?>>()
    val currentDailySetDurations = MutableLiveData(DailySetDurations.empty())

    /**
     * the cache for clazzDailySetCursorCache
     */
    val clazzDailySetCursorCache = HashMap<String, ClazzDailySetCursor>()
    val currentDailySetBindingKey = MutableLiveData<Pair<String, String>>()
    val currentDailySetBindingLiveData = MutableLiveData<LiveData<DailySetBinding?>>()
    val currentDailySetBinding = MutableLiveData(DailySetBinding.empty())
    var currentDailyTRCBinding = MutableLiveData<LiveData<DailyTRC?>>()
        internal set
    var currentDailyTableState2Binding = MutableLiveData(DailyTableState2.default())
        internal set
    var currentClazzDailySetState = MutableLiveData<ClazzDailySetState>()

    private fun registerDailySetHook() {
        currentDailySetUid.observeForever {
            Log.d(TAG, "currentDailySetUid changed to $it")
            //postCurrentDailySetLiveData(it)
            postCurrentDailySetDurationsLiveData(it)
        }
//        currentDailySetLiveData.observeForever {
//            Log.d(TAG, "currentDailySetLiveData changed to ${it.value}")
//            it.observeForever { it2 ->
//                Log.d(TAG, "currentDailySet changed to $it2")
//                if (it2 != null) {
//                    postCurrentDailySet(it2)
//                }
//            }
//        }
        currentDailySetDurationsLiveData.observeForever {
            Log.d(TAG, "currentDailySetDurationsLiveData changed to ${it.value}")
            it.observeForever { it2 ->
                Log.d(TAG, "currentDailySetDurations changed to $it2")
                if (it2 != null) {
                    postCurrentDailySetDurations(it2)
                }
            }
        }

        currentDailySetBindingKey.observeForever {
            Log.d(TAG, "currentDailySetBindingKey changed to $it")
            postCurrentDailySetBindingLiveData(it)
        }

        currentDailySetBindingLiveData.observeForever {
            Log.d(TAG, "currentDailySetBindingLiveData changed to ${it.value}")
            it.observeForever { it2 ->
                Log.d(TAG, "currentDailySetBinding changed to $it2")
                if (it2 != null) {
                    postCurrentDailySetBinding(it2)
                }
            }
        }

        currentDailySetBinding.observeForever {
            Log.d(TAG, "currentDailySetBinding changed to $it")
            postCurrentDailyTRCBinding(it.bindingDailyTableUid)
        }

        currentDailyTRCBinding.observeForever {
            Log.d(TAG, "currentDailyTRCBinding.livedata changed to ${it.value}")
            // 标记，仅允许在currentDailyTRC改变后重写一次。
            var oneTick = true
            it.observeForever { it2 ->
                // 在这里设置了拦截，奇怪的是，it2会在数据库修改阶段变为DailyTRC.default()
                if (oneTick || it2 != DailyTRC.default()) {
                    Log.d(TAG, "currentDailyTRCBinding changed to $it2")
                    if (it2 != null) {
                        // TODO: 2021/6/24 hook to user.
                        postCurrentDailyTableState2Binding(dailyTRC = it2, currentUserUid = currentUserUid.value)
                    }
                    oneTick = false
                }
            }
        }
        currentDailyTableState2Binding.observeForever {
            Log.d(TAG, "currentDailyTableState2Binding changed to $it")
            postCurrentClazzDailySetState(
                ClazzDailySetState(currentDailySetDurations.value!!, clazzDailySetCursorCache, currentDailySetBinding.value!!, currentDailyTableState2Binding.value!!, this)
            )
        }
    }

    @Deprecated("use postCurrentDailySetDurationsLiveData instead.")
    private fun postCurrentDailySetLiveData(dailySetUid: String) {
        currentDailySetLiveData.postValue(
            service.dailySetRepository.loadDailySet(dailySetUid).asLiveData()
        )
    }

    @Deprecated("use postCurrentDailySetDurations instead.")
    private fun postCurrentDailySet(dailySet: DailySet) {
        currentDailySet.postValue(
            dailySet
        )
    }

    private fun postCurrentDailySetDurationsLiveData(dailySetUid: String) {
        currentDailySetDurationsLiveData.postValue(
            service.dailySetRepository.loadDailySetDurations(dailySetUid).asLiveData()
        )
    }

    private fun postCurrentDailySetDurations(dailySetDurations: DailySetDurations) {
        currentDailySetDurations.postValue(
            dailySetDurations
        )
    }

    private fun postCurrentDailySetBindingLiveData(key: Pair<String, String>) {
        currentDailySetBindingLiveData.postValue(
            service.dailySetRepository.loadDailySetBinding(
                dailySetUid = key.first,
                dailyDurationUid = key.second
            ).asLiveData()
        )
    }

    private fun postCurrentDailySetBinding(dailySetBinding: DailySetBinding) {
        currentDailySetBinding.postValue(
            dailySetBinding
        )
    }

    private fun postCurrentDailyTRCBinding(dailyTableUid: String) {
        currentDailyTRCBinding.postValue(
            service.dailyTableRepository.loadDailyTRC(dailyTableUid).asLiveData()
        )
    }

    private fun postCurrentDailyTableState2Binding(dailyTRC: DailyTRC, currentUserUid: String?) {
        currentDailyTableState2Binding.postValue(DailyTableState2.of(dailyTRC = dailyTRC, currentUserUid))
    }

    private fun postCurrentClazzDailySetState(clazzDailySetState: ClazzDailySetState) {
        currentClazzDailySetState.postValue(clazzDailySetState)
    }

    //endregion

    companion object {
        const val TAG = "MainViewModel"
    }
}