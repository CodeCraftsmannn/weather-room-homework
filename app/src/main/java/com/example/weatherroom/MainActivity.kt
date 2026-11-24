package com.example.weatherroom

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.weatherroom.data.local.WeatherDatabase
import com.example.weatherroom.data.remote.WeatherClient
import com.example.weatherroom.data.repository.WeatherRepository
import com.example.weatherroom.ui.UiState
import com.example.weatherroom.ui.WeatherViewModel

class MainActivity : ComponentActivity() {
    @Suppress("UNCHECKED_CAST")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val repo = WeatherRepository(WeatherDatabase.getInstance(this).weatherDao(), WeatherClient.api)
        val factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T = WeatherViewModel(repo) as T
        }
        setContent {
            val vm: WeatherViewModel = viewModel(factory = factory)
            val rows by vm.weather.collectAsState()
            val state by vm.uiState.collectAsState()
            LaunchedEffect(Unit) { vm.onAction(WeatherViewModel.Action.Refresh) }
            MaterialTheme {
                Surface(Modifier.fillMaxSize()) {
                    Column(Modifier.padding(24.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
                        Text("Ankara Weather", style = MaterialTheme.typography.headlineMedium)
                        Button(onClick = { vm.onAction(WeatherViewModel.Action.Refresh) }) { Text("Refresh") }
                        when (state) {
                            UiState.Loading -> CircularProgressIndicator()
                            is UiState.Error -> Column {
                                Text((state as UiState.Error).message)
                                Button(onClick = { vm.onAction(WeatherViewModel.Action.Retry) }) { Text("Retry") }
                            }
                            is UiState.Success -> Text("Saved locally • survives app restarts")
                        }
                        LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                            items(rows, key = { it.location }) { row ->
                                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                    Text(row.location)
                                    Text("${row.temperatureC}°C · wind ${row.windSpeedKmh} km/h")
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
