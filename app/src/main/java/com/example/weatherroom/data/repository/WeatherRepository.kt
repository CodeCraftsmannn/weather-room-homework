package com.example.weatherroom.data.repository

import com.example.weatherroom.data.local.SavedWeather
import com.example.weatherroom.data.local.WeatherDao
import com.example.weatherroom.data.remote.WeatherApi
import kotlinx.coroutines.flow.Flow

class WeatherRepository(private val dao: WeatherDao, private val api: WeatherApi, private val geocodingApi: WeatherApi) {
    val savedWeather: Flow<List<SavedWeather>> = dao.observeAll()
    suspend fun refreshDefaultLocation() {
        fetchAndSave("Ankara", 39.9334, 32.8597)
    }
    suspend fun addCity(cityName: String) {
        val city = geocodingApi.searchCity(cityName.trim()).results.firstOrNull()
            ?: throw IllegalArgumentException("'${cityName.trim()}' şehri bulunamadı.")
        val displayName = city.country?.let { "${city.name}, $it" } ?: city.name
        fetchAndSave(displayName, city.latitude, city.longitude)
    }
    suspend fun removeCity(location: String) {
        dao.deleteByLocation(location)
    }
    private suspend fun fetchAndSave(location: String, latitude: Double, longitude: Double) {
        val response = api.currentWeather(latitude = latitude, longitude = longitude)
        dao.insert(SavedWeather(location, response.current.temperatureC, response.current.windSpeedKmh, System.currentTimeMillis()))
    }
}
