package com.alonso.dotdash.presentation.training

import android.os.SystemClock
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alonso.dotdash.data.local.AppSettings
import com.alonso.dotdash.domain.model.TrainingQuestion
import com.alonso.dotdash.domain.repository.AppSettingsRepository
import com.alonso.dotdash.domain.repository.StatisticsRepository
import com.alonso.dotdash.domain.repository.TrainingRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

private const val MILLIS = 5000L
class TrainingViewModel(
    private val repository: TrainingRepository,
    private val statisticsRepository: StatisticsRepository,
    appSettingsRepository: AppSettingsRepository
) : ViewModel() {

    private val _currentQuestion = MutableStateFlow<TrainingQuestion?>(null)
    val currentQuestion = _currentQuestion.asStateFlow()

    private val _isAnswerCorrect = MutableStateFlow<Boolean?>(null)
    val isAnswerCorrect = _isAnswerCorrect.asStateFlow()

    private val _showResult = MutableStateFlow(false)
    val showResult = _showResult.asStateFlow()

    private val _correctAnswersCount = MutableStateFlow(0)
    val correctAnswersCount = _correctAnswersCount.asStateFlow()

    private val _answeredQuestionsCount = MutableStateFlow(0)
    val answeredQuestionsCount = _answeredQuestionsCount.asStateFlow()

    private val _selectedAnswer = MutableStateFlow<String?>(null)
    val selectedAnswer = _selectedAnswer.asStateFlow()

    val appSettings = appSettingsRepository.getSettings().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(MILLIS),
        initialValue = AppSettings()
    )

    private var sessionStartTimeMillis: Long = 0L
    private val sessionCorrectSymbols = mutableSetOf<String>()

    init {
        loadTraining()
    }

    fun loadTraining() {
        viewModelScope.launch {
            val questions = repository.loadTraining()
            _currentQuestion.value = questions.firstOrNull()
            _showResult.value = false
            _isAnswerCorrect.value = null
            _correctAnswersCount.value = 0
            _answeredQuestionsCount.value = 0
            _selectedAnswer.value = null

            sessionStartTimeMillis = SystemClock.elapsedRealtime()
            sessionCorrectSymbols.clear()
        }
    }

    fun onAnswerSelected(answer: String) {
        if (_showResult.value) return

        viewModelScope.launch {
            repository.selectAnswer(answer)

            val isCorrect = repository.checkAnswer()
            _isAnswerCorrect.value = isCorrect
            _showResult.value = true
            _answeredQuestionsCount.value += 1
            _selectedAnswer.value = answer

            if (isCorrect) {
                _correctAnswersCount.value += 1
                _currentQuestion.value?.correctAnswer?.let { correctSymbol ->
                    sessionCorrectSymbols.add(correctSymbol)
                }
            }
        }
    }

    fun onNextQuestion() {
        viewModelScope.launch {
            if (repository.hasNextQuestion()) {
                val next = repository.nextQuestion()
                _currentQuestion.value = next
                _showResult.value = false
                _isAnswerCorrect.value = null
                _selectedAnswer.value = null
            } else {
                endTraining()
            }
        }
    }

    fun onRestart() {
        loadTraining()
    }

    private suspend fun endTraining() {
        val trainingTimeMillis = SystemClock.elapsedRealtime() - sessionStartTimeMillis

        statisticsRepository.updateStatistics(
            correctAnswers = _correctAnswersCount.value,
            answeredQuestions = _answeredQuestionsCount.value,
            trainingTimeMillis = trainingTimeMillis,
            correctSymbols = sessionCorrectSymbols.toSet()
        )

        repository.endTraining()
        _currentQuestion.value = null
        _showResult.value = false
        _isAnswerCorrect.value = null
        _selectedAnswer.value = null
    }
}
