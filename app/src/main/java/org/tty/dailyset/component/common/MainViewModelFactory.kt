package org.tty.dailyset.component.common

import androidx.lifecycle.*

class MainViewModelFactory(private val sharedComponents: SharedComponents): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MainViewModel(sharedComponents) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}