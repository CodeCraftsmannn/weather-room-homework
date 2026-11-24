package com.example.weatherroom.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherroom.data.local.SavedWeather
import com.example.weatherroom.data.repository.WeatherRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

sealed interface UiState {
    data object Loading : UiState
    data class Success(val weather: List<SavedWeather>) : UiState
    data class Error(val message: String) : UiState
}

class WeatherViewModel(private val repository: WeatherRepository) : ViewModel() {
    val weather: StateFlow<List<SavedWeather>> = repository.savedWeather.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())
    val uiState = MutableStateFlow<UiState>(UiState.Loading)
    fun onAction(action: Action) { when (action) { Action.Retry, Action.Refresh -> refresh() } }
    private fun refresh() {
        viewModelScope.launch {
            uiState.value = UiState.Loading
            try {
                repository.refreshDefaultLocation()
                uiState.value = UiState.Success(repository.savedWeather.first())
            }
            catch (error: Exception) { uiState.value = UiState.Error(error.localizedMessage ?: "Could not load weather. Check your connection and retry.") }
        }
    }
    sealed interface Action { data object Retry : Action; data object Refresh : Action }
}
