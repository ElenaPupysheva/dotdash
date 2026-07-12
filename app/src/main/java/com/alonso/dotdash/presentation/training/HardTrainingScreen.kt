package com.alonso.dotdash.presentation.training

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.Lightbulb
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.alonso.dotdash.R
import com.alonso.dotdash.core.ads.RewardedHintsAd
import com.alonso.dotdash.core.common.findActivity
import com.alonso.dotdash.presentation.training.qcode.HintAllowanceViewModel
import com.alonso.dotdash.presentation.training.qcode.HintEvent
import com.alonso.dotdash.ui.theme.SuccessGreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HardTrainingScreen(
    onBackClick: () -> Unit,
    viewModel: TrainingViewModel,
    hintViewModel: HintAllowanceViewModel? = null
) {
    val question by viewModel.currentQuestion.collectAsState()
    val showResult by viewModel.showResult.collectAsState()
    val isCorrect by viewModel.isAnswerCorrect.collectAsState()
    val answered by viewModel.answeredQuestionsCount.collectAsState()
    val correctCount by viewModel.correctAnswersCount.collectAsState()
    val hintsRemaining = hintViewModel?.totalRemaining?.collectAsState()?.value ?: 0
    val activity = LocalContext.current.findActivity()
    val rewardedHintsAd = remember(activity) {
        activity?.let(::RewardedHintsAd)
    }
    var input by remember(question) { mutableStateOf("") }
    var showHint by rememberSaveable(question?.morseCode) { mutableStateOf(false) }
    var hintUnlocked by rememberSaveable(question?.morseCode) { mutableStateOf(false) }
    var limitReached by rememberSaveable { mutableStateOf(false) }
    var adUnavailable by rememberSaveable { mutableStateOf(false) }
    LaunchedEffect(hintViewModel, question?.morseCode) {
        hintViewModel?.events?.collect { event ->
            when (event) {
                HintEvent.Granted -> {
                    hintUnlocked = true
                    showHint = true
                }

                HintEvent.LimitReached -> {
                    limitReached = true
                }
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.hard_mode)) },
                navigationIcon = {
                    IconButton(onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, stringResource(R.string.back))
                    }
                },
                actions = {
                    Text("${(answered + 1).coerceAtMost(10)} / 10", Modifier.padding(end = 16.dp))
                }
            )
        },
        bottomBar = {
            if (question != null) {
                Surface(color = MaterialTheme.colorScheme.background) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .navigationBarsPadding()
                            .padding(horizontal = 20.dp, vertical = 12.dp)
                    ) {
                        Button(
                            onClick = {
                                if (showResult) viewModel.onNextQuestion()
                                else viewModel.onMorseAnswerSubmitted(input)
                            },
                            enabled = showResult || input.isNotBlank(),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(56.dp)
                        ) {
                            Text(stringResource(if (showResult) R.string.next_question else R.string.submit))
                        }
                    }
                }
            }
        }
    ) { padding ->
        question?.let { item ->
            Column(
                Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .verticalScroll(rememberScrollState())
                    .padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                LinearProgressIndicator({ answered / 10f }, Modifier.fillMaxWidth())
                Text(
                    stringResource(R.string.enter_morse),
                    style = MaterialTheme.typography.labelLarge
                )
                Text(item.correctAnswer, style = MaterialTheme.typography.displaySmall)
                item.hint?.let { hint ->
                    TextButton(
                        onClick = {
                            when {
                                showHint -> showHint = false
                                hintUnlocked || hintViewModel == null -> {
                                    hintUnlocked = true
                                    showHint = true
                                }

                                else -> hintViewModel.requestHint()
                            }
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Lightbulb,
                            contentDescription = null
                        )
                        Spacer(Modifier.width(8.dp))
                        Text(
                            if (showHint) {
                                stringResource(R.string.hide_hint)
                            } else if (hintViewModel != null) {
                                hardHintButtonText(hintsRemaining)
                            } else {
                                stringResource(R.string.show_hint)
                            }
                        )
                    }
                    if (showHint) {
                        Text(
                            text = hint,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
                Surface(
                    Modifier
                        .fillMaxWidth()
                        .height(78.dp),
                    shape = RoundedCornerShape(18.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text(
                            input.ifEmpty { "· · ·" },
                            fontFamily = FontFamily.Monospace,
                            style = MaterialTheme.typography.headlineMedium
                        )
                    }
                }
                if (showResult) {
                    Text(
                        if (isCorrect == true) stringResource(R.string.correct) else "${
                            stringResource(
                                R.string.incorrect
                            )
                        }: ${item.morseCode}",
                        color = if (isCorrect == true) SuccessGreen else MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.titleMedium,
                        textAlign = TextAlign.Center,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
                MorseInputPad(
                    value = input,
                    onAppend = { if (!showResult) input += it },
                    onDelete = { if (!showResult && input.isNotEmpty()) input = input.dropLast(1) }
                )
            }
        } ?: Column(
            Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(stringResource(R.string.completed), style = MaterialTheme.typography.headlineSmall)
            Spacer(Modifier.height(12.dp))
            Text("$correctCount / 10", style = MaterialTheme.typography.headlineMedium)
            Spacer(Modifier.height(24.dp))
            Button(
                viewModel::onRestart,
                Modifier.fillMaxWidth()
            ) { Text(stringResource(R.string.again)) }
        }
    }

    if (limitReached) {
        AlertDialog(
            onDismissRequest = {
                limitReached = false
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
                        "${stringResource(R.string.no_hints_message)}\n\n${stringResource(R.string.rewarded_hints_message)}"
                    }
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        adUnavailable = false
                        if (rewardedHintsAd == null || hintViewModel == null) {
                            adUnavailable = true
                        } else {
                            rewardedHintsAd.loadAndShow(
                                onReward = {
                                    limitReached = false
                                    hintViewModel.addRewardedHintsAndRequest()
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
                        limitReached = false
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
private fun hardHintButtonText(hintsRemaining: Int): String {
    val baseText = stringResource(R.string.show_hint_count, hintsRemaining)
    return if (hintsRemaining == 0) {
        "$baseText  🎬 +3"
    } else {
        baseText
    }
}