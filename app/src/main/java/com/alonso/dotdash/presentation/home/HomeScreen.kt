package com.alonso.dotdash.presentation.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.alonso.dotdash.core.navigation.Screen
import com.alonso.dotdash.ui.theme.SuccessGreen

private data class HomeMenuItemUi(
    val title: String,
    val subtitle: String,
    val icon: ImageVector,
    val route: String,
    val iconTint: Color,
    val iconContainer: Color
)

private data class HomeStatUi(
    val value: String,
    val label: String
)

@Composable
fun HomeScreen(
    navController: NavController,
    viewModel: HomeViewModel
) {
    val statistics by viewModel.statistics.collectAsState()

    val menuItems = listOf(
        HomeMenuItemUi(
            title = "Упражнения",
            subtitle = "Тренируйся каждый день",
            icon = Icons.Filled.PlayArrow,
            route = Screen.TrainingScreen.route,
            iconTint = MaterialTheme.colorScheme.primary,
            iconContainer = MaterialTheme.colorScheme.primaryContainer
        ),
        HomeMenuItemUi(
            title = "Словарь",
            subtitle = "Все символы Морзе",
            icon = Icons.AutoMirrored.Filled.MenuBook,
            route = Screen.DictionaryScreen.route,
            iconTint = MaterialTheme.colorScheme.primary,
            iconContainer = MaterialTheme.colorScheme.primaryContainer
        ),
        HomeMenuItemUi(
            title = "Статистика",
            subtitle = "Твой прогресс",
            icon = Icons.Filled.BarChart,
            route = Screen.StatisticScreen.route,
            iconTint = SuccessGreen,
            iconContainer = SuccessGreen.copy(alpha = 0.12f)
        ),
        HomeMenuItemUi(
            title = "Настройки",
            subtitle = "Тема, звук, поддержка",
            icon = Icons.Filled.Settings,
            route = Screen.SettingsScreen.route,
            iconTint = MaterialTheme.colorScheme.primary,
            iconContainer = MaterialTheme.colorScheme.primaryContainer
        )
    )

    val progressText = "${statistics.todayCorrectAnswers} / ${statistics.dailyGoal} знаков"
    val progressValue = if (statistics.dailyGoal == 0) {
        0f
    } else {
        (statistics.todayCorrectAnswers.toFloat() / statistics.dailyGoal.toFloat())
            .coerceIn(0f, 1f)
    }

    val stats = listOf(
        HomeStatUi(statistics.learnedSymbolsCount.toString(), "выучено"),
        HomeStatUi("${statistics.accuracyPercent}%", "точность"),
        HomeStatUi("${statistics.todayTrainingMinutes}м", "сегодня")
    )

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 20.dp, vertical = 16.dp)
        ) {
            Text(
                text = "Точка и тире",
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.onBackground
            )

            Spacer(modifier = Modifier.height(18.dp))

            HomeHeroCard(
                title = "Цель на сегодня",
                progressText = progressText,
                progress = progressValue,
                buttonText = "Продолжить",
                onClick = { navController.navigate(Screen.TrainingScreen.route) }
            )

            Spacer(modifier = Modifier.height(14.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                stats.forEach { stat ->
                    HomeStatCard(
                        value = stat.value,
                        label = stat.label,
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            Text(
                text = "Разделы",
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(12.dp))

            Column(
                verticalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                menuItems.forEach { item ->
                    HomeMenuCard(
                        title = item.title,
                        subtitle = item.subtitle,
                        icon = item.icon,
                        iconTint = item.iconTint,
                        iconContainer = item.iconContainer,
                        onClick = { navController.navigate(item.route) }
                    )
                }
            }
        }
    }
}

@Composable
private fun HomeHeroCard(
    title: String,
    progressText: String,
    progress: Float,
    buttonText: String,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(215.dp)
            .clip(RoundedCornerShape(28.dp))
            .background(
                brush = Brush.linearGradient(
                    colors = listOf(
                        MaterialTheme.colorScheme.primary,
                        MaterialTheme.colorScheme.inversePrimary
                    )
                )
            )
            .padding(22.dp)
    ) {
        Box(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .size(150.dp)
                .background(
                    color = Color.White.copy(alpha = 0.08f),
                    shape = CircleShape
                )
        )

        Box(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(end = 8.dp, bottom = 8.dp)
                .size(88.dp)
                .background(
                    color = Color.White.copy(alpha = 0.06f),
                    shape = CircleShape
                )
        )

        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            Text(
                text = title.uppercase(),
                style = MaterialTheme.typography.labelLarge,
                color = Color.White.copy(alpha = 0.92f)
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = progressText,
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontFamily = FontFamily.SansSerif,
                    fontWeight = FontWeight.Bold
                ),
                color = Color.White
            )

            Spacer(modifier = Modifier.height(18.dp))

            LinearProgressIndicator(
                progress = { progress },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
                    .clip(RoundedCornerShape(999.dp)),
                color = Color.White,
                trackColor = Color.White.copy(alpha = 0.18f)
            )

            Spacer(modifier = Modifier.height(22.dp))

            Surface(
                shape = RoundedCornerShape(16.dp),
                color = Color.White,
                modifier = Modifier.clickable(onClick = onClick)
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 18.dp, vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = buttonText,
                        style = MaterialTheme.typography.titleSmall,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun HomeStatCard(
    value: String,
    label: String,
    modifier: Modifier = Modifier
) {
    ElevatedCard(
        shape = RoundedCornerShape(18.dp),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.elevatedCardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        modifier = modifier
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 16.dp)
        ) {
            Text(
                text = value,
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(2.dp))

            Text(
                text = label,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun HomeMenuCard(
    title: String,
    subtitle: String,
    icon: ImageVector,
    iconTint: Color,
    iconContainer: Color,
    onClick: () -> Unit
) {
    ElevatedCard(
        shape = RoundedCornerShape(16.dp),
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
                .padding(horizontal = 14.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .background(
                        color = iconContainer,
                        shape = RoundedCornerShape(12.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = iconTint
                )
            }

            Spacer(modifier = Modifier.size(14.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Spacer(modifier = Modifier.height(2.dp))

                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(18.dp)
            )
        }
    }
}
