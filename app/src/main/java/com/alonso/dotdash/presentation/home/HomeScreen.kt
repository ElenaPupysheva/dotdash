package com.alonso.dotdash.presentation.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavController
import com.alonso.dotdash.BuildConfig
import com.alonso.dotdash.R
import com.alonso.dotdash.core.ads.RewardedHintsAd
import com.alonso.dotdash.core.common.findActivity
import com.alonso.dotdash.core.navigation.Screen
import com.alonso.dotdash.ui.theme.NavTextActiveLight
import com.alonso.dotdash.ui.theme.SuccessGreen
import com.my.target.ads.MyTargetView
import com.my.target.common.models.IAdLoadingError
import com.yandex.mobile.ads.banner.BannerAdEventListener
import com.yandex.mobile.ads.banner.BannerAdSize
import com.yandex.mobile.ads.banner.BannerAdView
import com.yandex.mobile.ads.common.AdRequest
import com.yandex.mobile.ads.common.AdRequestError
import com.yandex.mobile.ads.common.ImpressionData

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
    val hintsRemaining by viewModel.hintsRemaining.collectAsState()
    val activity = LocalContext.current.findActivity()
    val rewardedHintsAd = remember(activity) {
        activity?.let(::RewardedHintsAd)
    }
    var showRewardedHintsDialog by rememberSaveable { mutableStateOf(false) }
    var adUnavailable by rememberSaveable { mutableStateOf(false) }

    val menuItems = listOf(
        HomeMenuItemUi(
            title = stringResource(R.string.training),
            subtitle = stringResource(R.string.txt_training),
            icon = Icons.Filled.PlayArrow,
            route = Screen.BufferScreen.route,
            iconTint = MaterialTheme.colorScheme.primary,
            iconContainer = MaterialTheme.colorScheme.primaryContainer
        ),
        HomeMenuItemUi(
            title = stringResource(R.string.dictionary),
            subtitle = stringResource(R.string.txt_dictionary),
            icon = Icons.AutoMirrored.Filled.MenuBook,
            route = Screen.DictionaryScreen.route,
            iconTint = MaterialTheme.colorScheme.primary,
            iconContainer = MaterialTheme.colorScheme.primaryContainer
        ),
        HomeMenuItemUi(
            title = stringResource(R.string.statistic),
            subtitle = stringResource(R.string.txt_statistic),
            icon = Icons.Filled.BarChart,
            route = Screen.StatisticScreen.route,
            iconTint = SuccessGreen,
            iconContainer = SuccessGreen.copy(alpha = 0.12f)
        ),
        HomeMenuItemUi(
            title = stringResource(R.string.settings),
            subtitle = stringResource(R.string.txt_settings),
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
        HomeStatUi("${statistics.todayTrainingMinutes} мин", "сегодня")
    )

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 16.dp)
                .navigationBarsPadding()
        ) {
            Text(
                text = stringResource(R.string.header),
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.onBackground
            )

            Spacer(modifier = Modifier.height(18.dp))

            HomeHeroCard(
                title = stringResource(R.string.aim_txt),
                progressText = progressText,
                progress = progressValue,
                hintsRemaining = hintsRemaining,
                buttonText = stringResource(R.string.continue_txt),
                onClick = {
                    navController.navigate(Screen.BufferScreen.route)
                },
                onHintsClick = {
                    adUnavailable = false
                    showRewardedHintsDialog = true
                }
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
                text = stringResource(R.string.sections),
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
            HomeBannerAd(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 14.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
        }
    }

    if (showRewardedHintsDialog) {
        AlertDialog(
            onDismissRequest = {
                showRewardedHintsDialog = false
                adUnavailable = false
            },
            title = {
                Text(stringResource(R.string.no_hints_title))
            },
            text = {
                Text(
                    if (adUnavailable) {
                        stringResource(R.string.ad_unavailable_message)
                    } else {
                        stringResource(R.string.rewarded_hints_message)
                    }
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        adUnavailable = false
                        if (rewardedHintsAd == null) {
                            adUnavailable = true
                        } else {
                            rewardedHintsAd.loadAndShow(
                                onReward = {
                                    showRewardedHintsDialog = false
                                    viewModel.addRewardedHints()
                                },
                                onUnavailable = {
                                    adUnavailable = true
                                }
                            )
                        }
                    }
                ) {
                    Text(stringResource(R.string.watch_ad_for_hints))
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        showRewardedHintsDialog = false
                        adUnavailable = false
                    }
                ) {
                    Text(stringResource(R.string.later))
                }
            }
        )
    }
}

@Composable
private fun HomeHeroCard(
    title: String,
    progressText: String,
    progress: Float,
    hintsRemaining: Int,
    buttonText: String,
    onClick: () -> Unit,
    onHintsClick: () -> Unit
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
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = title.uppercase(),
                    style = MaterialTheme.typography.labelLarge,
                    color = Color.White.copy(alpha = 0.92f)
                )

                HintCounterBadge(
                    hintsRemaining = hintsRemaining,
                    onClick = onHintsClick
                )
            }

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
                        color = NavTextActiveLight
                    )
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                        contentDescription = null,
                        tint = NavTextActiveLight,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun HintCounterBadge(
    hintsRemaining: Int,
    onClick: () -> Unit
) {
    Surface(
        shape = RoundedCornerShape(999.dp),
        color = Color.White.copy(alpha = 0.18f),
        modifier = Modifier.clickable(enabled = hintsRemaining == 0, onClick = onClick)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 7.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Icon(
                imageVector = Icons.Filled.Lightbulb,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(17.dp)
            )

            Text(
                text = if (hintsRemaining == 0) {
                    "0  🎬 +3"
                } else {
                    hintsRemaining.toString()
                },
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }
    }
}

@Composable
private fun HomeBannerAd(
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val activity = context.findActivity() ?: return
    val vkSlotId = BuildConfig.VK_BANNER_SLOT_ID
    val yandexAdUnitId = BuildConfig.YANDEX_BANNER_AD_UNIT_ID
    var useYandex by remember(vkSlotId, yandexAdUnitId) {
        mutableStateOf(vkSlotId <= 0)
    }

    if (vkSlotId <= 0 && yandexAdUnitId.isBlank()) return

    BoxWithConstraints(modifier = modifier) {
        val bannerWidthDp = maxWidth.value.toInt().coerceAtLeast(1)
        if (useYandex && yandexAdUnitId.isNotBlank()) {
            val bannerView = remember(activity, yandexAdUnitId, bannerWidthDp) {
                BannerAdView(activity).apply {
                    setAdSize(BannerAdSize.sticky(activity, bannerWidthDp))
                    setBannerAdEventListener(
                        object : BannerAdEventListener {
                            override fun onAdLoaded() = Unit

                            override fun onAdFailedToLoad(error: AdRequestError) = Unit

                            override fun onAdClicked() = Unit

                            override fun onImpression(data: ImpressionData?) = Unit
                        }
                    )
                    loadAd(AdRequest.Builder(yandexAdUnitId).build())
                }
            }

            DisposableEffect(bannerView) {
                onDispose {
                    bannerView.destroy()
                }
            }

            AndroidView(
                factory = { bannerView },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            )
        } else if (!useYandex) {
            val bannerView = remember(context, vkSlotId, bannerWidthDp) {
                MyTargetView(context).apply {
                    setSlotId(vkSlotId)
                    setAdSize(
                        MyTargetView.AdSize.getAdSizeForCurrentOrientation(
                            bannerWidthDp,
                            50,
                            context
                        )
                    )
                    setListener(
                        object : MyTargetView.MyTargetViewListener {
                            override fun onLoad(view: MyTargetView) = Unit

                            override fun onNoAd(
                                error: IAdLoadingError,
                                view: MyTargetView
                            ) {
                                useYandex = true
                            }

                            override fun onShow(view: MyTargetView) = Unit

                            override fun onClick(view: MyTargetView) = Unit
                        }
                    )
                    load()
                }
            }

            DisposableEffect(bannerView) {
                onDispose {
                    bannerView.destroy()
                }
            }

            AndroidView(
                factory = { bannerView },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            )
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
                style = MaterialTheme.typography.titleMedium,
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
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
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
