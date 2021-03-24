package org.tty.dailyset

import androidx.lifecycle.*
import kotlinx.coroutines.launch
import org.tty.dailyset.model.entity.DailyTable
import org.tty.dailyset.model.entity.Preference
import org.tty.dailyset.model.entity.User
import org.tty.dailyset.provider.DailySetApplication
import org.tty.dailyset.ui.page.MainPageTabs

class MainViewModel(private val service: DailySetApplication): ViewModel() {
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
}

class MainViewModelFactory(private val service: DailySetApplication): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MainViewModel(service) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}