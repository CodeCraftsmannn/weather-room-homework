package com.example.weatherroom.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface WeatherDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE) suspend fun insert(weather: SavedWeather)
    @Update suspend fun update(weather: SavedWeather)
    @Query("DELETE FROM saved_weather WHERE location = :location") suspend fun deleteByLocation(location: String)
    @Query("SELECT * FROM saved_weather ORDER BY updatedAt DESC") fun observeAll(): Flow<List<SavedWeather>>
}
