package org.tty.dailyset.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import org.tty.dailyset.DailySetApplication
import org.tty.dailyset.common.local.ComponentViewModel
import org.tty.dailyset.common.observable.*
import org.tty.dailyset.model.entity.*
import org.tty.dailyset.model.lifetime.UserState
import org.tty.dailyset.model.lifetime.dailyset.ClazzDailySetCursor
import org.tty.dailyset.model.lifetime.dailyset.ClazzDailySetState
import org.tty.dailyset.model.lifetime.dailytable.DailyTableState2
import org.tty.dailyset.ui.page.MainPageTabs

class MainViewModel(val service: DailySetApplication): ViewModel() {

    init {
        // register the viewModel
        ComponentViewModel provides this
    }

    override fun onCleared() {
        // unregister the viewModel
        ComponentViewModel.pop()
    }

    /**
     * init the viewModel
     */
    fun init() {
        registerDailySetHook()
    }

    //region liveData Internal
    private val mainTabLiveData = liveData(MainPageTabs.DAILY_SET)
    private val seedVersionLiveData = liveData(service.preferenceRepository.seedVersion)
    private val currentUserUidLiveData = liveData(service.preferenceRepository.currentUserUid)
    private val usersLiveData = liveData(service.userRepository.users)
    private val currentUserStateLiveData = liveData2Map(usersLiveData, currentUserUidLiveData, "currentUser") { users, currentUserUid ->
        val currentUser = users.find { it.userUid == currentUserUid } ?: User.default()
        UserState(currentUser, currentUserUid)
    }


    //endregion


    //region profile scope



    //endregion

    //region dailyTable.settings scope
    val dailyTableSummaries = liveData(service.dailyTableRepository.dailyTableSummaries)
    val currentDailyTableUid = liveData(DailyTable.default)

    val currentDailyTRC = liveDataMapAsync(currentDailyTableUid, "currentDailyTRC") {
        service.dailyTableRepository.loadDailyTRC(it)
    }
    val currentDailyTRCEnd = currentDailyTRC.ignoreNull().initial(DailyTRC.default())

    val currentDailyTableState2 = liveData2Map(currentDailyTRC, currentUserUidLiveData, "currentDailyTableState2") { value1, value2 ->
        return@liveData2Map if (value1 == null) {
            null
        } else {
            DailyTableState2(value1, value2)
        }
    }
    val currentDailyTableState2End = currentDailyTableState2.ignoreNull().initial(
        DailyTableState2.default())

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
                        postCurrentDailyTableState2Binding(dailyTRC = it2, currentUserUid = currentUserUidLiveData.value)
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

    //region liveData Exports
    val mainTab = mainTabLiveData.initial(MainPageTabs.DAILY_SET)
    val seedVersion = seedVersionLiveData.initial(PreferenceName.SEED_VERSION.defaultValue.toInt())
    val currentUserUid = currentUserUidLiveData.initial(PreferenceName.CURRENT_USER_UID.defaultValue)
    val users = usersLiveData.initial(listOf())
    val currentUserState = currentUserStateLiveData.initial(UserState(User.default(), User.local))
    //endregion

    companion object {
        const val TAG = "MainViewModel"
    }


}