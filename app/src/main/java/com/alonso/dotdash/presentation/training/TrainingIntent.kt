package com.alonso.dotdash.presentation.training

sealed interface TrainingIntent {
    data object LoadTraining : TrainingIntent
    data object SelectAnswer : TrainingIntent
    data object CheckAnswer : TrainingIntent
    data object NextQuestion : TrainingIntent
    data object RestartTraining : TrainingIntent
    data object EndTraining : TrainingIntent
}
