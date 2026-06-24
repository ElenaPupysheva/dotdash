package com.alonso.dotdash.core.notification

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.alonso.dotdash.data.local.AppSettingsDataStore
import com.alonso.dotdash.data.repository.AppSettingsRepositoryImpl
import kotlinx.coroutines.flow.first
import java.time.LocalDate

private const val DAYS_SINCE_LAST_OPEN_FOR_REMINDER = 1L

class ReminderWorker(
    appContext: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(appContext, workerParams) {

    override suspend fun doWork(): Result {
        Log.d("ReminderWorker", "Worker started")

        val repository = AppSettingsRepositoryImpl(
            AppSettingsDataStore(applicationContext)
        )

        val settings = repository.getSettings().first()

        if (!settings.trainingReminderEnabled) {
            Log.d("ReminderWorker", "Reminder disabled, worker finished")
            return Result.success()
        }

        val todayEpochDay = LocalDate.now().toEpochDay()
        val daysSinceLastOpen = todayEpochDay - settings.lastOpenedEpochDay

        Log.d(
            "ReminderWorker",
            "enabled=${settings.trainingReminderEnabled}, " +
                    "lastOpened=${settings.lastOpenedEpochDay}, " +
                    "today=$todayEpochDay, " +
                    "daysSinceLastOpen=$daysSinceLastOpen"
        )

        if (daysSinceLastOpen >= DAYS_SINCE_LAST_OPEN_FOR_REMINDER) {
            Log.d("ReminderWorker", "Showing notification")

            NotificationHelper.createNotificationChannel(applicationContext)
            NotificationHelper.showTrainingReminder(applicationContext)
        } else {
            Log.d("ReminderWorker", "Notification skipped, not enough days passed")
        }

        return Result.success()
    }
}
