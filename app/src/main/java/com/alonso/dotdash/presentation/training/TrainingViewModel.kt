package com.alonso.dotdash.presentation.training

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alonso.dotdash.domain.model.TrainingQuestion
import com.alonso.dotdash.domain.repository.TrainingRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class TrainingViewModel(
    private val repository: TrainingRepository
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
    private val _selectedAnswer = MutableStateFlow<String?>(null)
    val selectedAnswer = _selectedAnswer.asStateFlow()

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
            } else {
                endTraining()
            }
        }
    }

    fun onRestart() {
        loadTraining()
    }

    private fun endTraining() {
        viewModelScope.launch {
            repository.endTraining()
            _currentQuestion.value = null
            _showResult.value = false
        }
    }
}
