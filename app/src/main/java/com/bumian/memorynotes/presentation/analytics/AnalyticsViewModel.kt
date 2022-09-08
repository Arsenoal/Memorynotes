package com.bumian.memorynotes.presentation.analytics

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bumian.memorynotes.repo.analytics.AnalyticsRepo
import kotlinx.coroutines.launch

class AnalyticsViewModel(
    private val analyticsRepo: AnalyticsRepo
): ViewModel() {

    fun sendEvent(name: String) {
        viewModelScope.launch {
            analyticsRepo.sendEvent(name)
        }
    }
}