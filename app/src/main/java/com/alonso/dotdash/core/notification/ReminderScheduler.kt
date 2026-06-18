package com.alonso.dotdash.core.notification

import android.content.Context
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import java.util.concurrent.TimeUnit

private const val TRAINING_REMINDER_WORK_NAME = "training_reminder_work"
fun scheduleReminderWork(context: Context) {
    val request = PeriodicWorkRequestBuilder<ReminderWorker>(
        1, TimeUnit.DAYS
    ).build()

    WorkManager.getInstance(context).enqueueUniquePeriodicWork(
        TRAINING_REMINDER_WORK_NAME,
        ExistingPeriodicWorkPolicy.UPDATE,
        request
    )
}

fun ensureReminderWorkScheduled(context: Context) {
    val request = PeriodicWorkRequestBuilder<ReminderWorker>(
        1, TimeUnit.DAYS
    ).build()

    WorkManager.getInstance(context).enqueueUniquePeriodicWork(
        TRAINING_REMINDER_WORK_NAME,
        ExistingPeriodicWorkPolicy.KEEP,
        request
    )
}

fun cancelReminderWork(context: Context) {
    WorkManager.getInstance(context).cancelUniqueWork(TRAINING_REMINDER_WORK_NAME)
}
