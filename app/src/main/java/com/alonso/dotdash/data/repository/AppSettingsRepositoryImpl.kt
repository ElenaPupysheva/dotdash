package com.alonso.dotdash.data.repository

import com.alonso.dotdash.data.local.AppSettings
import com.alonso.dotdash.data.local.AppSettingsDataStore
import com.alonso.dotdash.domain.repository.AppSettingsRepository
import kotlinx.coroutines.flow.Flow

class AppSettingsRepositoryImpl(
    private val dataStore: AppSettingsDataStore
) : AppSettingsRepository {

    override fun getSettings(): Flow<AppSettings> {
        return dataStore.settingsFlow
    }

    override suspend fun setVibrationEnabled(enabled: Boolean) {
        dataStore.setVibrationEnabled(enabled)
    }

    override suspend fun setTrainingReminderEnabled(enabled: Boolean) {
        dataStore.setTrainingReminderEnabled(enabled)
    }
}
