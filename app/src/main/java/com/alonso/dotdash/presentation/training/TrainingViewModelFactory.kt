package com.alonso.dotdash.presentation.training

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.alonso.dotdash.domain.repository.StatisticsRepository
import com.alonso.dotdash.domain.repository.TrainingRepository

class TrainingViewModelFactory(
    private val repository: TrainingRepository,
    private val statisticsRepository: StatisticsRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TrainingViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return TrainingViewModel(
                repository = repository,
                statisticsRepository = statisticsRepository
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
