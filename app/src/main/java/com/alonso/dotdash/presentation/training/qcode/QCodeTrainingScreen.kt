package com.alonso.dotdash.presentation.training.qcode

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.VolumeUp
import androidx.compose.material.icons.outlined.Lightbulb
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.alonso.dotdash.R
import com.alonso.dotdash.core.common.MorsePlayer
import com.alonso.dotdash.core.common.ToneBeepPlayer
import com.alonso.dotdash.core.ui.QuizButton
import com.alonso.dotdash.core.ui.TrainingCard
import com.alonso.dotdash.domain.model.TrainingQuestion
import com.alonso.dotdash.presentation.training.TrainingViewModel
import com.alonso.dotdash.ui.theme.SuccessGreen
import kotlinx.coroutines.flow.Flow

private const val DEFAULT_TRAINING = 10
@Composable
fun QCodeTrainingScreen(
    onBackClick: () -> Unit,
    viewModel: TrainingViewModel,
    hintViewModel: HintAllowanceViewModel
) {
    val currentQuestion by viewModel.currentQuestion.collectAsState()
    val showResult by viewModel.showResult.collectAsState()
    val isAnswerCorrect by viewModel.isAnswerCorrect.collectAsState()
    val selectedAnswer by viewModel.selectedAnswer.collectAsState()
    val correctAnswersCount by viewModel.correctAnswersCount.collectAsState()
    val answeredQuestionsCount by viewModel.answeredQuestionsCount.collectAsState()
    val hintsRemaining by hintViewModel.totalRemaining.collectAsState()

    currentQuestion?.let { question ->
        QCodeTrainingContent(
            question = question,
            showResult = showResult,
            isAnswerCorrect = isAnswerCorrect,
            selectedAnswer = selectedAnswer,
            answeredQuestionsCount = answeredQuestionsCount,
            hintsRemaining = hintsRemaining,
            hintEvents = hintViewModel.events,
            onHintRequested = hintViewModel::requestHint,
            onBackClick = onBackClick,
            onAnswerSelected = viewModel::onAnswerSelected,
            onNextQuestion = viewModel::onNextQuestion
        )
    } ?: QCodeTrainingResult(
        correctAnswersCount = correctAnswersCount,
        onBackClick = onBackClick,
        onRestart = viewModel::onRestart
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QCodeTrainingResult(
    correctAnswersCount: Int,
    onBackClick: () -> Unit,
    onRestart: () -> Unit
) {
    val totalQuestions = DEFAULT_TRAINING

    Scaffold(
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    titleContentColor = MaterialTheme.colorScheme.onBackground,
                    navigationIconContentColor = MaterialTheme.colorScheme.onBackground,
                    actionIconContentColor = MaterialTheme.colorScheme.onBackground
                ),
                title = {
                    Text(stringResource(R.string.q_code_training))
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
                .padding(horizontal = 20.dp)
                .navigationBarsPadding(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Surface(
                shape = RoundedCornerShape(28.dp),
                color = MaterialTheme.colorScheme.surface,
                tonalElevation = 2.dp,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(
                        horizontal = 20.dp,
                        vertical = 24.dp
                    ),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = stringResource(R.string.completed),
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    Spacer(Modifier.height(12.dp))

                    Text(
                        text = stringResource(
                            R.string.q_code_result,
                            correctAnswersCount,
                            totalQuestions
                        ),
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    Spacer(Modifier.height(6.dp))

                    Text(
                        text = stringResource(
                            R.string.training_errors,
                            totalQuestions - correctAnswersCount
                        ),
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Spacer(Modifier.height(20.dp))

                    Button(
                        onClick = onRestart,
                        shape = RoundedCornerShape(18.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp)
                    ) {
                        Text(
                            text = stringResource(R.string.again),
                            style = MaterialTheme.typography.titleSmall
                        )
                    }
                }
            }

        }

    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QCodeTrainingContent(
    question: TrainingQuestion,
    showResult: Boolean,
    isAnswerCorrect: Boolean?,
    selectedAnswer: String?,
    answeredQuestionsCount: Int,
    hintsRemaining: Int,
    hintEvents: Flow<HintEvent>,
    onHintRequested: () -> Unit,
    onBackClick: () -> Unit,
    onAnswerSelected: (String) -> Unit,
    onNextQuestion: () -> Unit
) {
    var showHint by rememberSaveable(question.morseCode) {
        mutableStateOf(false)
    }
    var hintUnlocked by rememberSaveable(question.morseCode) {
        mutableStateOf(false)
    }
    var limitReached by rememberSaveable(question.morseCode) {
        mutableStateOf(false)
    }

    LaunchedEffect(question.morseCode, hintEvents) {
        hintEvents.collect { event ->
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

    val soundPlayer = remember { ToneBeepPlayer() }
    val morsePlayer = remember { MorsePlayer(soundPlayer) }

    DisposableEffect(Unit) {
        onDispose {
            morsePlayer.stop()
            morsePlayer.release()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    titleContentColor = MaterialTheme.colorScheme.onBackground,
                    navigationIconContentColor = MaterialTheme.colorScheme.onBackground,
                    actionIconContentColor = MaterialTheme.colorScheme.onBackground
                ),
                title = {
                    Text(stringResource(R.string.q_code_training))
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back")
                    }
                },
                actions = {
                    Text(
                        "${answeredQuestionsCount + 1} / 10",
                        modifier = Modifier.padding(end = 16.dp)
                    )
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 20.dp)
                .navigationBarsPadding(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            LinearProgressIndicator(
                progress = { answeredQuestionsCount / 10f },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(28.dp))
            Text(
                stringResource(R.string.which_q_code),
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(Modifier.height(18.dp))

            TrainingCard(question.morseCode)

            Spacer(Modifier.height(12.dp))
            OutlinedButton(onClick = { morsePlayer.play(question.morseCode) }) {
                Icon(Icons.AutoMirrored.Filled.VolumeUp, null)
                Spacer(Modifier.width(8.dp))
                Text(stringResource(R.string.listen))
            }

            question.hint?.let { hint ->
                TextButton(
                    onClick = {
                        when {
                            showHint -> showHint = false
                            hintUnlocked -> showHint = true
                            else -> onHintRequested()
                        }
                    }
                ) {
                    Icon(Icons.Outlined.Lightbulb, contentDescription = null)
                    Spacer(Modifier.width(8.dp))

                    Text(
                        if (showHint) {
                            stringResource(R.string.hide_hint)
                        } else {

                            qCodeHintButtonText(hintsRemaining)
                        }
                    )
                }

                if (showHint) {
                    Text(
                        text = hint,
                        style = MaterialTheme.typography.bodyLarge,
                        textAlign = TextAlign.Center
                    )
                }
            }

            Spacer(Modifier.height(16.dp))
            if (showResult) {
                Text(
                    text = stringResource(
                        if (isAnswerCorrect == true) {
                            R.string.correct
                        } else {
                            R.string.incorrect
                        }
                    ),
                    style = MaterialTheme.typography.titleSmall,
                    color = if (isAnswerCorrect == true) {
                        SuccessGreen
                    } else {
                        MaterialTheme.colorScheme.error
                    }
                )

                Spacer(Modifier.height(16.dp))
            }

            question.options.chunked(2).forEach { options ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    options.forEach { option ->
                        val correct = option == question.correctAnswer
                        val selected = option == selectedAnswer

                        QuizButton(
                            symbol = option,
                            onClick = { onAnswerSelected(option) },
                            enabled = !showResult,
                            containerColor = when {
                                showResult && correct -> SuccessGreen
                                showResult && selected -> MaterialTheme.colorScheme.error
                                else -> MaterialTheme.colorScheme.surface
                            },
                            modifier = Modifier.weight(1f)
                        )
                    }
                    if (options.size == 1) Spacer(Modifier.weight(1f))
                }
                Spacer(Modifier.height(12.dp))
            }
            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = onNextQuestion,
                enabled = showResult,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
            ) {
                Text(stringResource(R.string.next_question))
            }

            Spacer(Modifier.height(16.dp))
        }
    }
    if (limitReached) {
        AlertDialog(
            onDismissRequest = { limitReached = false },
            title = {
                Text(stringResource(R.string.no_hints_title))
            },
            text = {
                Text(stringResource(R.string.no_hints_message))
            },
            confirmButton = {
                TextButton(onClick = { limitReached = false }) {
                    Text(stringResource(android.R.string.ok))
                }
            }
        )
    }
}

@Composable
private fun qCodeHintButtonText(hintsRemaining: Int): String {
    val baseText = stringResource(R.string.show_hint_count, hintsRemaining)
    return if (hintsRemaining == 0) {
        "$baseText  🎬 +3"
    } else {
        baseText
    }
}