package com.alonso.dotdash.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.alonso.dotdash.data.local.HintAllowanceDataStore
import com.alonso.dotdash.domain.repository.StatisticsRepository

class HomeViewModelFactory(
    private val repository: StatisticsRepository,
    private val hintAllowanceDataStore: HintAllowanceDataStore
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return HomeViewModel(repository, hintAllowanceDataStore) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
