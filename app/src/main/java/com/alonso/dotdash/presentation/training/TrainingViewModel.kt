package com.alonso.dotdash.presentation.training

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alonso.dotdash.data.local.TrainingQuestion
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

    init {
        loadTraining()
    }

    fun loadTraining() {
        viewModelScope.launch {
            val questions = repository.loadTraining()
            _currentQuestion.value = questions.firstOrNull()
            _showResult.value = false
            _isAnswerCorrect.value = null
        }
    }

    fun onAnswerSelected(answer: String) {
        viewModelScope.launch {
            repository.selectAnswer(answer)

            val isCorrect = repository.checkAnswer()
            _isAnswerCorrect.value = isCorrect
            _showResult.value = true
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
