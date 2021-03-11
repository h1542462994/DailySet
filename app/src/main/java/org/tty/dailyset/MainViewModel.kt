package org.tty.dailyset

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import org.tty.dailyset.model.entity.Preference
import org.tty.dailyset.provider.DailySetApplication

class MainViewModel(private val service: DailySetApplication): ViewModel() {
    val seedVersion: LiveData<Preference> = service.preferenceRepository.seedVersion.asLiveData()
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