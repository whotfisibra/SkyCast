package com.ibra.weatherapp.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface WeatherDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCity(weather: WeatherEntity)

    @Query("SELECT * FROM recent_cities ORDER BY timestamp DESC")
    fun getAllCities(): Flow<List<WeatherEntity>>

    @Query("DELETE FROM recent_cities WHERE id = :id")
    suspend fun deleteCity(id: Int)

    @Query("DELETE FROM recent_cities")
    suspend fun deleteAllCities()
}

