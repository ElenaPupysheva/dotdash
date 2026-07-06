package com.alonso.dotdash.presentation.training.qcode

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alonso.dotdash.data.local.HintAllowanceDataStore
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

private const val SUBSCRIPTION_TIMEOUT_MILLIS = 5_000L

sealed interface HintEvent {
    data object Granted : HintEvent
    data object LimitReached : HintEvent
}

class HintAllowanceViewModel(
    private val dataStore: HintAllowanceDataStore
) : ViewModel() {

    val totalRemaining = dataStore.allowanceFlow
        .map { it.totalRemaining }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(SUBSCRIPTION_TIMEOUT_MILLIS),
            initialValue = 3
        )

    private val _events = MutableSharedFlow<HintEvent>()
    val events = _events.asSharedFlow()

    private var requestInProgress = false

    fun requestHint() {
        if (requestInProgress) return
        requestInProgress = true

        viewModelScope.launch {
            try {
                _events.emit(
                    if (dataStore.consumeHint()) {
                        HintEvent.Granted
                    } else {
                        HintEvent.LimitReached
                    }
                )
            } finally {
                requestInProgress = false
            }
        }
    }

    fun addRewardedHints(amount: Int = 3) {
        viewModelScope.launch {
            dataStore.addRewardedHints(amount)
        }
    }
}
