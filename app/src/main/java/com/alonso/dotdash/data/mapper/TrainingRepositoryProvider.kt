package com.alonso.dotdash.data.mapper

import com.alonso.dotdash.data.repository.TrainingRepositoryImpl
import com.alonso.dotdash.domain.repository.TrainingRepository

object TrainingRepositoryProvider {
    private val repository: TrainingRepository by lazy {
        TrainingRepositoryImpl()
    }

    fun provideRepository(): TrainingRepository = repository
}
