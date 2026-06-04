package com.alonso.dotdash.presentation.settings

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.Vibration
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.alonso.dotdash.R
import com.alonso.dotdash.core.common.openSupportLink
import com.alonso.dotdash.core.notification.cancelReminderWork
import com.alonso.dotdash.core.notification.scheduleReminderWork

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    onBackClick: () -> Unit,
    isDarkTheme: Boolean,
    onThemeChange: (Boolean) -> Unit,
    viewModel: SettingsViewModel
) {
    val context = LocalContext.current
    val statistics by viewModel.statistics.collectAsState()
    val appSettings by viewModel.appSettings.collectAsState()

    val notificationPermissionLauncher =
        rememberLauncherForActivityResult(
            contract = ActivityResultContracts.RequestPermission()
        ) { isGranted ->
            if (isGranted) {
                viewModel.updateTrainingReminderEnabled(true)
                scheduleReminderWork(context.applicationContext)
            } else {
                viewModel.updateTrainingReminderEnabled(false)
                cancelReminderWork(context.applicationContext)
            }
        }

    fun hasNotificationPermission(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
        } else {
            true
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    titleContentColor = MaterialTheme.colorScheme.onBackground,
                    navigationIconContentColor = MaterialTheme.colorScheme.onBackground
                ),
                title = {
                    Text(
                        text = stringResource(R.string.settings),
                        style = MaterialTheme.typography.titleLarge,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.back)
                        )
                    }
                }
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 20.dp, vertical = 8.dp)
                .navigationBarsPadding()
        ) {
            SettingsSectionTitle(title = stringResource(R.string.appearance))
            SettingsGroupCard {
                SettingsSwitchRow(
                    title = stringResource(R.string.theme),
                    subtitle = if (isDarkTheme) {
                        stringResource(R.string.dark_theme)
                    } else {
                        stringResource(R.string.light_theme)
                    },
                    icon = Icons.Filled.Palette,
                    checked = isDarkTheme,
                    onCheckedChange = onThemeChange
                )
            }

            Spacer(modifier = Modifier.size(20.dp))

            SettingsSectionTitle(title = stringResource(R.string.learning))
            SettingsGroupCard {
                SettingsSwitchRow(
                    title = stringResource(R.string.vibration),
                    subtitle = stringResource(R.string.on_reply),
                    icon = Icons.Filled.Vibration,
                    checked = appSettings.vibrationEnabled,
                    onCheckedChange = viewModel::updateVibrationEnabled
                )
                SettingsDivider()
                SettingsGoalRow(
                    title = stringResource(R.string.daily_goal),
                    subtitle = stringResource(R.string.num_per_day),
                    icon = Icons.Filled.Favorite,
                    goal = statistics.dailyGoal,
                    onDecrease = { viewModel.updateDailyGoal(statistics.dailyGoal - 1) },
                    onIncrease = { viewModel.updateDailyGoal(statistics.dailyGoal + 1) }
                )
            }

            Spacer(modifier = Modifier.size(20.dp))

            SettingsSectionTitle(title = stringResource(R.string.notification))
            SettingsGroupCard {
                SettingsSwitchRow(
                    title = stringResource(R.string.reminder),
                    subtitle = stringResource(R.string.reminder_txt),
                    icon = Icons.Filled.Notifications,
                    checked = appSettings.trainingReminderEnabled,
                    onCheckedChange = { enabled ->
                        if (!enabled) {
                            viewModel.updateTrainingReminderEnabled(false)
                            cancelReminderWork(context.applicationContext)
                        } else {
                            if (hasNotificationPermission()) {
                                viewModel.updateTrainingReminderEnabled(true)
                                scheduleReminderWork(context.applicationContext)
                            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                                notificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                            } else {
                                viewModel.updateTrainingReminderEnabled(true)
                                scheduleReminderWork(context.applicationContext)
                            }
                        }
                    }
                )
            }

            Spacer(modifier = Modifier.size(24.dp))

            SettingsSupportCard(
                onClick = { openSupportLink(context) }
            )
        }
    }
}

@Composable
private fun SettingsSectionTitle(
    title: String
) {
    Text(
        text = title.uppercase(),
        style = MaterialTheme.typography.labelLarge,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        modifier = Modifier.padding(bottom = 10.dp)
    )
}

@Composable
private fun SettingsGroupCard(
    content: @Composable ColumnScope.() -> Unit
) {
    ElevatedCard(
        shape = RoundedCornerShape(18.dp),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 3.dp),
        colors = CardDefaults.elevatedCardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            content = content
        )
    }
}

@Composable
private fun SettingsSwitchRow(
    title: String,
    subtitle: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 14.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        SettingsLeadingIcon(icon = icon)

        Spacer(modifier = Modifier.size(14.dp))

        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.size(2.dp))

            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(
                checkedThumbColor = MaterialTheme.colorScheme.onPrimary,
                checkedTrackColor = MaterialTheme.colorScheme.primary,
                uncheckedThumbColor = MaterialTheme.colorScheme.surface,
                uncheckedTrackColor = MaterialTheme.colorScheme.primaryContainer
            )
        )
    }
}

@Composable
private fun SettingsGoalRow(
    title: String,
    subtitle: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    goal: Int,
    onDecrease: () -> Unit,
    onIncrease: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 14.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        SettingsLeadingIcon(icon = icon)

        Spacer(modifier = Modifier.size(14.dp))

        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.size(2.dp))

            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onDecrease) {
                Icon(
                    imageVector = Icons.Filled.Remove,
                    contentDescription = "Уменьшить"
                )
            }

            Text(
                text = goal.toString(),
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(horizontal = 6.dp)
            )

            IconButton(onClick = onIncrease) {
                Icon(
                    imageVector = Icons.Filled.Add,
                    contentDescription = "Увеличить"
                )
            }
        }
    }
}

@Composable
private fun SettingsSupportCard(
    onClick: () -> Unit
) {
    ElevatedCard(
        shape = RoundedCornerShape(18.dp),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 3.dp),
        colors = CardDefaults.elevatedCardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            SettingsLeadingIcon(icon = Icons.Filled.Favorite)

            Spacer(modifier = Modifier.size(14.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = stringResource(R.string.support_project),
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Spacer(modifier = Modifier.size(2.dp))

                Text(
                    text = "DonationAlerts",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(16.dp)
            )
        }
    }
}

@Composable
private fun SettingsLeadingIcon(
    icon: androidx.compose.ui.graphics.vector.ImageVector
) {
    Row(
        modifier = Modifier
            .size(44.dp)
            .background(
                color = MaterialTheme.colorScheme.primaryContainer,
                shape = CircleShape
            ),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary
        )
    }
}

@Composable
private fun SettingsDivider() {
    HorizontalDivider(
        modifier = Modifier.padding(horizontal = 14.dp),
        thickness = 1.dp,
        color = MaterialTheme.colorScheme.outlineVariant
    )
}
