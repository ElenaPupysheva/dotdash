package com.alonso.dotdash.domain.repository

import com.alonso.dotdash.data.local.AppSettings
import kotlinx.coroutines.flow.Flow

interface AppSettingsRepository {
    fun getSettings(): Flow<AppSettings>

    suspend fun setSoundEnabled(enabled: Boolean)
    suspend fun setVibrationEnabled(enabled: Boolean)
    suspend fun setTrainingReminderEnabled(enabled: Boolean)
}
