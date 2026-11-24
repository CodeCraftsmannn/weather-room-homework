package com.example.weatherroom.data.remote

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import retrofit2.http.GET
import retrofit2.http.Query

@Serializable
data class CurrentWeatherResponse(@SerialName("current") val current: CurrentValues)

@Serializable
data class CurrentValues(
    @SerialName("temperature_2m") val temperatureC: Double,
    @SerialName("wind_speed_10m") val windSpeedKmh: Double
)

interface WeatherApi {
    @GET("v1/forecast")
    suspend fun currentWeather(
        @Query("latitude") latitude: Double,
        @Query("longitude") longitude: Double,
        @Query("current") current: String = "temperature_2m,wind_speed_10m",
        @Query("timezone") timezone: String = "auto"
    ): CurrentWeatherResponse
}
