package com.example.weatherroom.data.repository

import com.example.weatherroom.data.local.SavedWeather
import com.example.weatherroom.data.local.WeatherDao
import com.example.weatherroom.data.remote.WeatherApi
import kotlinx.coroutines.flow.Flow

class WeatherRepository(private val dao: WeatherDao, private val api: WeatherApi) {
    val savedWeather: Flow<List<SavedWeather>> = dao.observeAll()
    suspend fun refreshDefaultLocation() {
        val response = api.currentWeather(latitude = 39.9334, longitude = 32.8597)
        dao.insert(SavedWeather("Ankara", response.current.temperatureC, response.current.windSpeedKmh, System.currentTimeMillis()))
    }
}
