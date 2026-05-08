package com.ibra.weatherapp.repository

import com.ibra.weatherapp.data.api.RetrofitInstance
import com.ibra.weatherapp.data.db.WeatherDao
import com.ibra.weatherapp.data.db.WeatherEntity
import com.ibra.weatherapp.data.model.ForecastResponse
import com.ibra.weatherapp.data.model.WeatherResponse
import kotlinx.coroutines.flow.Flow

class WeatherRepository(private val dao: WeatherDao) {

    // API calls
    suspend fun getCurrentWeather(city: String, apiKey: String): WeatherResponse {
        return RetrofitInstance.api.getCurrentWeather(city, apiKey)
    }

    suspend fun getForecast(city: String, apiKey: String): ForecastResponse {
        return RetrofitInstance.api.getForecast(city, apiKey)
    }

    // Database operations
    suspend fun saveCity(weather: WeatherEntity) {
        dao.insertCity(weather)
    }

    fun getRecentCities(): Flow<List<WeatherEntity>> {
        return dao.getAllCities()
    }

    suspend fun deleteCity(id: Int) {
        dao.deleteCity(id)
    }

    suspend fun deleteAllCities() {
        dao.deleteAllCities()
    }
}

