package org.tty.dailyset.viewmodel

import androidx.lifecycle.*
import org.tty.dailyset.DailySetApplication

class MainViewModelFactory(private val service: DailySetApplication): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MainViewModel(service) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}