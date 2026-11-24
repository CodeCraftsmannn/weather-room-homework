package com.example.weatherroom.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "saved_weather")
data class SavedWeather(@PrimaryKey val location: String, val temperatureC: Double, val windSpeedKmh: Double, val updatedAt: Long)
