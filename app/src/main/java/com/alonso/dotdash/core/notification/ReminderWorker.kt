package com.alonso.dotdash.core.notification

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.alonso.dotdash.data.local.AppSettingsDataStore
import com.alonso.dotdash.data.repository.AppSettingsRepositoryImpl
import kotlinx.coroutines.flow.first
import java.time.LocalDate

private const val DAYS_SINCE_LAST_OPEN_FOR_REMINDER = 2L

class ReminderWorker(
    appContext: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(appContext, workerParams) {

    override suspend fun doWork(): Result {
        val repository = AppSettingsRepositoryImpl(
            AppSettingsDataStore(applicationContext)
        )

        val settings = repository.getSettings().first()

        if (!settings.trainingReminderEnabled) {
            return Result.success()
        }

        val todayEpochDay = LocalDate.now().toEpochDay()
        val daysSinceLastOpen = todayEpochDay - settings.lastOpenedEpochDay

        if (daysSinceLastOpen >= DAYS_SINCE_LAST_OPEN_FOR_REMINDER) {
            NotificationHelper.showTrainingReminder(applicationContext)
        }

        return Result.success()
    }
}
