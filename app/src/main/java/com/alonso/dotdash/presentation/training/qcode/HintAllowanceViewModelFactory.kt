package com.alonso.dotdash.presentation.training.qcode

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.alonso.dotdash.data.local.HintAllowanceDataStore

class HintAllowanceViewModelFactory(
    private val dataStore: HintAllowanceDataStore
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        return HintAllowanceViewModel(dataStore) as T
    }
}
