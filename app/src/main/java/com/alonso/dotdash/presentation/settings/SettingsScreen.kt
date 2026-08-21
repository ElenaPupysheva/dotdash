package com.alonso.dotdash.presentation.settings

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.selection.toggleable
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.Vibration
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.semantics.Role
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.alonso.dotdash.R
import com.alonso.dotdash.core.common.MAX_TONE_FREQUENCY_HZ
import com.alonso.dotdash.core.common.MIN_TONE_FREQUENCY_HZ
import com.alonso.dotdash.core.common.TONE_FREQUENCY_STEP_HZ
import com.alonso.dotdash.core.common.ToneBeepPlayer
import com.alonso.dotdash.core.common.ToneFrequency
import com.alonso.dotdash.core.notification.cancelReminderWork
import com.alonso.dotdash.core.notification.scheduleReminderWork
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    onBackClick: () -> Unit,
    isDarkTheme: Boolean,
    onThemeChange: (Boolean) -> Unit,
    viewModel: SettingsViewModel
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
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

    DisposableEffect(lifecycleOwner, appSettings.trainingReminderEnabled) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                if (!hasNotificationPermission() && appSettings.trainingReminderEnabled) {
                    viewModel.updateTrainingReminderEnabled(false)
                    cancelReminderWork(context.applicationContext)
                }
            }
        }

        lifecycleOwner.lifecycle.addObserver(observer)

        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
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
                .verticalScroll(rememberScrollState())
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
                SettingsToneFrequencyRow(
                    frequencyHz = appSettings.toneFrequencyHz,
                    onFrequencyChange = viewModel::updateToneFrequencyHz
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
                    checked = appSettings.trainingReminderEnabled && hasNotificationPermission(),
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
        }
    }
}

@Composable
private fun SettingsToneFrequencyRow(
    frequencyHz: Int,
    onFrequencyChange: (Int) -> Unit
) {
    var sliderValue by remember(frequencyHz) {
        mutableFloatStateOf(frequencyHz.toFloat())
    }
    val tonePlayer = remember { ToneBeepPlayer() }

    DisposableEffect(tonePlayer) {
        onDispose(tonePlayer::release)
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 14.dp, vertical = 14.dp),
        verticalAlignment = Alignment.Top
    ) {
        SettingsLeadingIcon(icon = Icons.Filled.VolumeUp)

        Spacer(modifier = Modifier.size(14.dp))

        Column(modifier = Modifier.weight(1f)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = stringResource(R.string.tone_frequency),
                        style = MaterialTheme.typography.titleSmall,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = stringResource(
                            R.string.tone_frequency_value,
                            sliderValue.roundToInt()
                        ),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.primary
                    )
                }

                IconButton(onClick = tonePlayer::shortBeep) {
                    Icon(
                        imageVector = Icons.Filled.VolumeUp,
                        contentDescription = stringResource(R.string.preview_tone),
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }

            Text(
                text = stringResource(R.string.tone_frequency_description),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Slider(
                value = sliderValue,
                onValueChange = { value ->
                    val steppedValue =
                        (value / TONE_FREQUENCY_STEP_HZ).roundToInt() *
                            TONE_FREQUENCY_STEP_HZ
                    sliderValue = steppedValue.toFloat()
                    ToneFrequency.update(steppedValue)
                },
                onValueChangeFinished = {
                    onFrequencyChange(sliderValue.roundToInt())
                },
                valueRange = MIN_TONE_FREQUENCY_HZ.toFloat()..
                    MAX_TONE_FREQUENCY_HZ.toFloat(),
                steps = (MAX_TONE_FREQUENCY_HZ - MIN_TONE_FREQUENCY_HZ) /
                    TONE_FREQUENCY_STEP_HZ - 1
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
            .toggleable(
                value = checked,
                role = Role.Switch,
                onValueChange = onCheckedChange
            )
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

        Spacer(modifier = Modifier.size(12.dp))

        Switch(
            checked = checked,
            onCheckedChange = null,
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
            IconButton(
                onClick = onDecrease,
                enabled = goal > MIN_DAILY_GOAL
            ) {
                Icon(
                    imageVector = Icons.Filled.Remove,
                    contentDescription = stringResource(R.string.decrease),
                    tint = if (goal == MIN_DAILY_GOAL) {
                        MaterialTheme.colorScheme.onSurfaceVariant
                    } else {
                        MaterialTheme.colorScheme.primary
                    }
                )
            }

            Text(
                text = goal.toString(),
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(horizontal = 6.dp)
            )

            IconButton(
                onClick = onIncrease,
                enabled = goal < MAX_DAILY_GOAL
            ) {
                Icon(
                    imageVector = Icons.Filled.Add,
                    contentDescription = stringResource(R.string.increase),
                    tint = if (goal >= MAX_DAILY_GOAL) {
                        MaterialTheme.colorScheme.onSurfaceVariant
                    } else {
                        MaterialTheme.colorScheme.primary
                    }
                )
            }
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
