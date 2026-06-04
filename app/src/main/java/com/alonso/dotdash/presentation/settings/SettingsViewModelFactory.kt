package com.alonso.dotdash.presentation.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.alonso.dotdash.domain.repository.AppSettingsRepository
import com.alonso.dotdash.domain.repository.StatisticsRepository

class SettingsViewModelFactory(
    private val statisticsRepository: StatisticsRepository,
    private val appSettingsRepository: AppSettingsRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SettingsViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return SettingsViewModel(
                statisticsRepository = statisticsRepository,
                appSettingsRepository = appSettingsRepository
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
