package com.ibra.weatherapp.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "recent_cities")
data class WeatherEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val cityName: String,
    val temperature: String,
    val description: String,
    val humidity: String,
    val windSpeed: String,
    val timestamp: Long = System.currentTimeMillis()
)

