package com.alonso.dotdash.presentation.training

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.alonso.dotdash.R
import com.alonso.dotdash.core.ui.QuizButton
import com.alonso.dotdash.core.ui.TrainingCard
import com.alonso.dotdash.ui.theme.SuccessGreen

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
                        text = stringResource(R.string.training),
                        style = MaterialTheme.typography.titleLarge,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Localized description"
                        )
                    }
                }
            )
        },
        containerColor = MaterialTheme.colorScheme.background,
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            currentQuestion?.let { question ->
                Text(
                    text = stringResource(R.string.training_screen),
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(16.dp))

                TrainingCard(question.morseCode)

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    question.options.forEach { option ->
                        val isCorrectSelectedButton =
                            showResult && isAnswerCorrect == true && selectedAnswer == option

                        QuizButton(
                            symbol = option,
                            onClick = { viewModel.onAnswerSelected(option) },
                            enabled = !showResult,
                            containerColor = if (isCorrectSelectedButton) {
                                SuccessGreen
                            } else {
                                MaterialTheme.colorScheme.primary
                            }
                        )

                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }
                if (showResult) {
                    Text(
                        text = if (isAnswerCorrect == true) "Правильно" else "Неправильно"
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))

                QuizButton(
                    symbol = "Дальше",
                    onClick = { viewModel.onNextQuestion() }
                )
            } ?: run {
                Text(
                    text = "Тренировка завершена",
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Результат: $correctAnswersCount из 10",
                    style = MaterialTheme.typography.titleMedium
                )

                Text(
                    text = "Ошибок: ${10 - correctAnswersCount}",
                    style = MaterialTheme.typography.bodyMedium
                )
                Spacer(modifier = Modifier.height(16.dp))

                QuizButton(
                    symbol = "Заново",
                    onClick = { viewModel.onRestart() }
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
        }

    }

}
