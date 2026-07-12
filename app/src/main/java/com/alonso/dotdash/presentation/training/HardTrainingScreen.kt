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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import com.alonso.dotdash.R
import com.alonso.dotdash.ui.theme.SuccessGreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HardTrainingScreen(onBackClick: () -> Unit, viewModel: TrainingViewModel) {
    val question by viewModel.currentQuestion.collectAsState()
    val showResult by viewModel.showResult.collectAsState()
    val isCorrect by viewModel.isAnswerCorrect.collectAsState()
    val answered by viewModel.answeredQuestionsCount.collectAsState()
    val correctCount by viewModel.correctAnswersCount.collectAsState()
    var input by remember(question) { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    titleContentColor = MaterialTheme.colorScheme.onBackground,
                    navigationIconContentColor = MaterialTheme.colorScheme.onBackground
                ),
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
                    .padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(18.dp)
            ) {
                LinearProgressIndicator({ answered / 10f }, Modifier.fillMaxWidth())
                Text(
                    stringResource(R.string.enter_morse),
                    style = MaterialTheme.typography.labelLarge
                )
                Text(item.correctAnswer, style = MaterialTheme.typography.displayMedium)
                item.hint?.let { Text(it, color = MaterialTheme.colorScheme.onSurfaceVariant) }
                Surface(
                    Modifier
                        .fillMaxWidth()
                        .height(86.dp),
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
                        color = if (isCorrect == true) SuccessGreen else MaterialTheme.colorScheme.error
                    )
                }
                MorseInputPad(
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
}
