package com.alonso.dotdash.presentation.statistics

import androidx.lifecycle.ViewModel
import com.alonso.dotdash.domain.repository.StatisticsRepository

class StatisticsViewModel(
    repository: StatisticsRepository
) : ViewModel() {
    val statistics = repository.getStatistics()
}
