package org.tty.dailyset.viewmodel

import android.util.Log
import androidx.lifecycle.*
import kotlinx.coroutines.launch
import org.tty.dailyset.DailySetApplication
import org.tty.dailyset.model.entity.DailyTRC
import org.tty.dailyset.model.entity.DailyTable
import org.tty.dailyset.model.entity.Preference
import org.tty.dailyset.model.entity.User
import org.tty.dailyset.model.lifetime.DailyTableState2
import org.tty.dailyset.ui.page.MainPageTabs

class MainViewModel(val service: DailySetApplication): ViewModel() {

    /**
     * init the viewModel
     */
    fun init() {
        currentDailyTableUid.observeForever {
            postCurrentDailyTRC(it)
        }
        currentDailyTRC.observeForever {
            Log.d(TAG, "currentDailyTRC.livedata changed to ${it.value}")
            it.observeForever { it2 ->
                Log.d(TAG, "currentDailyTRC changed to $it2")
                if (it2 != null) {
                    postCurrentDailyTableState2(dailyTRC = it2, currentUserUid = currentUserUid.value)
                }
            }
        }
        currentDailyTableState2.observeForever {
            Log.d(TAG, "currentDailyTableState2 changed to $it")
        }
        // TODO: 2021/5/11 添加用户更改的监听。
    }

    private fun postCurrentDailyTRC(currentDailyTableUid: String = DailyTable.default) {
        currentDailyTRC.postValue(
            service.dailyTableRepository.loadDailyTRC(currentDailyTableUid).asLiveData()
        )
    }

    private fun postCurrentDailyTableState2(dailyTRC: DailyTRC, currentUserUid: String?) {
        currentDailyTableState2.postValue(DailyTableState2.of(dailyTRC = dailyTRC, currentUserUid))
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
    private var _currentDailyTableUid = MutableLiveData(DailyTable.default)
    val currentDailyTableUid = _currentDailyTableUid


    var currentDailyTRC = MutableLiveData<LiveData<DailyTRC?>>()
        internal set
    var currentDailyTableState2 = MutableLiveData<DailyTableState2>(DailyTableState2.default())
        internal set
    //endregion

    companion object {
        const val TAG = "MainViewModel"
    }
}