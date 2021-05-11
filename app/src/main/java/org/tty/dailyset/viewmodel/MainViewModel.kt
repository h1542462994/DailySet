package org.tty.dailyset.viewmodel

import androidx.lifecycle.*
import kotlinx.coroutines.launch
import org.tty.dailyset.DailySetApplication
import org.tty.dailyset.model.entity.DailyTRC
import org.tty.dailyset.model.entity.DailyTable
import org.tty.dailyset.model.entity.Preference
import org.tty.dailyset.model.entity.User
import org.tty.dailyset.ui.page.MainPageTabs

class MainViewModel(val service: DailySetApplication): ViewModel() {

    /**
     * init the viewModel
     */
    fun init() {
        postCurrentDailyTRC()
        currentDailyTableUid.observeForever {
            postCurrentDailyTRC(it)
        }
    }

    private fun postCurrentDailyTRC(currentDailyTableUid: String = DailyTable.default) {
        currentDailyTRC.postValue(
            service.dailyTableRepository.loadDailyTRC(currentDailyTableUid).asLiveData()
        )
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
    //endregion

}