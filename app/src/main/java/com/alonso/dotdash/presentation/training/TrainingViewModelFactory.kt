package com.alonso.dotdash.presentation.training

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.alonso.dotdash.domain.model.TrainingSource
import com.alonso.dotdash.domain.repository.AppSettingsRepository
import com.alonso.dotdash.domain.repository.StatisticsRepository
import com.alonso.dotdash.domain.repository.TrainingRepository

class TrainingViewModelFactory(
    private val source: TrainingSource,
    private val repository: TrainingRepository,
    private val statisticsRepository: StatisticsRepository,
    private val appSettingsRepository: AppSettingsRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        return TrainingViewModel(
            source = source,
            repository = repository,
            statisticsRepository = statisticsRepository,
            appSettingsRepository = appSettingsRepository
        ) as T
    }
}
