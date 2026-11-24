package com.example.weatherroom.data.remote

import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory

object WeatherClient {
    private val json = Json { ignoreUnknownKeys = true }
    private val http = OkHttpClient.Builder().addInterceptor(HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BASIC }).build()
    val api: WeatherApi by lazy {
        Retrofit.Builder().baseUrl("https://api.open-meteo.com/").client(http)
            .addConverterFactory(json.asConverterFactory("application/json".toMediaType())).build().create(WeatherApi::class.java)
    }
}
