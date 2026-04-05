package com.alonso.dotdash.presentation.training

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.alonso.dotdash.domain.repository.TrainingRepository

class TrainingViewModelFactory(private val repository: TrainingRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TrainingViewModel::class.java)) {
            return TrainingViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
