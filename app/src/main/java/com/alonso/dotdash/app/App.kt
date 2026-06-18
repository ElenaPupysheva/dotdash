package com.alonso.dotdash.app

import android.app.Application
import com.alonso.dotdash.core.notification.NotificationHelper
import com.alonso.dotdash.core.notification.ensureReminderWorkScheduled
import com.alonso.dotdash.data.local.AppSettingsDataStore
import com.alonso.dotdash.data.repository.AppSettingsRepositoryImpl
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class App : Application() {

    private val applicationScope = CoroutineScope(SupervisorJob() + Dispatchers.Default)

    override fun onCreate() {
        super.onCreate()

        NotificationHelper.createNotificationChannel(this)

        applicationScope.launch {
            val repository = AppSettingsRepositoryImpl(
                AppSettingsDataStore(applicationContext)
            )

            if (repository.getSettings().first().trainingReminderEnabled) {
                ensureReminderWorkScheduled(applicationContext)
            }
        }
    }
}
