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
    private var retryAction: Action = Action.Refresh
    fun onAction(action: Action) {
        when (action) {
            Action.Retry -> perform(retryAction)
            Action.Refresh -> { retryAction = action; perform(action) }
            is Action.AddCity -> { retryAction = action; perform(action) }
            is Action.RemoveCity -> viewModelScope.launch {
                runCatching { repository.removeCity(action.location) }
                    .onFailure { uiState.value = UiState.Error(it.localizedMessage ?: "Şehir silinemedi.") }
            }
        }
    }
    private fun perform(action: Action) {
        viewModelScope.launch {
            uiState.value = UiState.Loading
            try {
                when (action) {
                    Action.Refresh -> repository.refreshDefaultLocation()
                    is Action.AddCity -> repository.addCity(action.name)
                    else -> Unit
                }
                uiState.value = UiState.Success(repository.savedWeather.first())
            }
            catch (error: Exception) { uiState.value = UiState.Error(error.localizedMessage ?: "Could not load weather. Check your connection and retry.") }
        }
    }
    sealed interface Action {
        data object Retry : Action
        data object Refresh : Action
        data class AddCity(val name: String) : Action
        data class RemoveCity(val location: String) : Action
    }
}
