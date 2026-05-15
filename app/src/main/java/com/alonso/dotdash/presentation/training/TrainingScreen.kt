package com.alonso.dotdash.presentation.training

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.alonso.dotdash.core.common.MorsePlayer
import com.alonso.dotdash.core.common.ToneBeepPlayer
import com.alonso.dotdash.core.ui.QuizButton
import com.alonso.dotdash.core.ui.TrainingCard
import com.alonso.dotdash.ui.theme.SuccessGreen

private const val TRAINING_TOTAL_QUESTIONS = 10

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TrainingScreen(
    onBackClick: () -> Unit,
    viewModel: TrainingViewModel
) {
    val currentQuestion by viewModel.currentQuestion.collectAsState()
    val isAnswerCorrect by viewModel.isAnswerCorrect.collectAsState()
    val showResult by viewModel.showResult.collectAsState()
    val selectedAnswer by viewModel.selectedAnswer.collectAsState()
    val correctAnswersCount by viewModel.correctAnswersCount.collectAsState()
    val answeredQuestionsCount by viewModel.answeredQuestionsCount.collectAsState()

    val soundPlayer = remember { ToneBeepPlayer() }
    val morsePlayer = remember { MorsePlayer(soundPlayer) }

    DisposableEffect(Unit) {
        onDispose {
            morsePlayer.stop()
            morsePlayer.release()
        }
    }

    val currentStep = when {
        currentQuestion == null -> TRAINING_TOTAL_QUESTIONS
        showResult -> answeredQuestionsCount
        else -> answeredQuestionsCount + 1
    }.coerceIn(1, TRAINING_TOTAL_QUESTIONS)

    val progress = answeredQuestionsCount.toFloat() / TRAINING_TOTAL_QUESTIONS.toFloat()

    Scaffold(
        topBar = {
            TopAppBar(
                windowInsets = androidx.compose.foundation.layout.WindowInsets(0),
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    titleContentColor = MaterialTheme.colorScheme.onBackground,
                    navigationIconContentColor = MaterialTheme.colorScheme.onBackground,
                    actionIconContentColor = MaterialTheme.colorScheme.onBackground
                ),
                title = {
                    Text(
                        text = "Упражнения",
                        style = MaterialTheme.typography.titleLarge,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Назад"
                        )
                    }
                },
                actions = {
                    Text(
                        text = "$currentStep / $TRAINING_TOTAL_QUESTIONS",
                        style = MaterialTheme.typography.titleSmall,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.padding(end = 16.dp)
                    )
                }
            )
        },
        containerColor = MaterialTheme.colorScheme.background,
    ) { innerPadding ->
        currentQuestion?.let { question ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(horizontal = 20.dp)
                    .navigationBarsPadding(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                LinearProgressIndicator(
                    progress = { progress },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(6.dp)
                        .clip(RoundedCornerShape(999.dp)),
                    color = MaterialTheme.colorScheme.primary,
                    trackColor = MaterialTheme.colorScheme.primaryContainer
                )

                Spacer(modifier = Modifier.height(28.dp))

                Text(
                    text = "КАКОЙ БУКВЕ СООТВЕТСТВУЕТ КОД?",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(18.dp))

                TrainingCard(question.morseCode)

                Spacer(modifier = Modifier.height(14.dp))

                Surface(
                    shape = RoundedCornerShape(999.dp),
                    color = MaterialTheme.colorScheme.primaryContainer,
                    modifier = Modifier
                        .clip(RoundedCornerShape(999.dp))
                        .clickable { morsePlayer.play(question.morseCode) }
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Filled.VolumeUp,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(18.dp)
                        )
                        Text(
                            text = "Прослушать",
                            style = MaterialTheme.typography.labelLarge,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                if (showResult) {
                    Text(
                        text = if (isAnswerCorrect == true) {
                            "Правильно"
                        } else {
                            "Неправильно"
                        },
                        style = MaterialTheme.typography.titleSmall,
                        color = if (isAnswerCorrect == true) {
                            SuccessGreen
                        } else {
                            MaterialTheme.colorScheme.error
                        }
                    )

                    Spacer(modifier = Modifier.height(16.dp))
                }

                Column(
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    question.options.chunked(2).forEach { rowOptions ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            rowOptions.forEach { option ->
                                val isCorrectOption = option == question.correctAnswer
                                val isSelectedOption = option == selectedAnswer

                                val buttonContainerColor = when {
                                    showResult && isCorrectOption -> SuccessGreen
                                    showResult && isSelectedOption -> MaterialTheme.colorScheme.error
                                    else -> MaterialTheme.colorScheme.surface
                                }

                                val buttonContentColor = when {
                                    showResult && isCorrectOption -> MaterialTheme.colorScheme.onPrimary
                                    showResult && isSelectedOption -> MaterialTheme.colorScheme.onError
                                    else -> MaterialTheme.colorScheme.onSurface
                                }

                                val buttonBorderColor = when {
                                    showResult && isCorrectOption -> SuccessGreen
                                    showResult && isSelectedOption -> MaterialTheme.colorScheme.error
                                    else -> MaterialTheme.colorScheme.outlineVariant
                                }

                                QuizButton(
                                    symbol = option,
                                    onClick = { viewModel.onAnswerSelected(option) },
                                    enabled = !showResult,
                                    containerColor = buttonContainerColor,
                                    contentColor = buttonContentColor,
                                    borderColor = buttonBorderColor,
                                    modifier = Modifier.weight(1f)
                                )
                            }

                            if (rowOptions.size == 1) {
                                Spacer(modifier = Modifier.weight(1f))
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.weight(1f))

                Button(
                    onClick = { viewModel.onNextQuestion() },
                    enabled = showResult,
                    shape = RoundedCornerShape(18.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = SuccessGreen,
                        contentColor = MaterialTheme.colorScheme.onPrimary,
                        disabledContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                        disabledContentColor = MaterialTheme.colorScheme.onSurfaceVariant
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(58.dp)
                ) {
                    Text(
                        text = "Следующий вопрос",
                        style = MaterialTheme.typography.titleSmall
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))
            }
        } ?: run {
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
                        modifier = Modifier.padding(horizontal = 20.dp, vertical = 24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Тренировка завершена",
                            style = MaterialTheme.typography.titleLarge,
                            color = MaterialTheme.colorScheme.onSurface
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        Text(
                            text = "Результат: $correctAnswersCount из $TRAINING_TOTAL_QUESTIONS",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onSurface
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        Text(
                            text = "Ошибок: ${TRAINING_TOTAL_QUESTIONS - correctAnswersCount}",
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )

                        Spacer(modifier = Modifier.height(20.dp))

                        Button(
                            onClick = { viewModel.onRestart() },
                            shape = RoundedCornerShape(18.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.primary,
                                contentColor = MaterialTheme.colorScheme.onPrimary
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(56.dp)
                        ) {
                            Text(
                                text = "Заново",
                                style = MaterialTheme.typography.titleSmall
                            )
                        }
                    }
                }
            }
        }
    }
}
