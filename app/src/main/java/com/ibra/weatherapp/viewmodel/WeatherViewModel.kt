package com.ibra.weatherapp.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.ibra.weatherapp.data.db.WeatherDatabase
import com.ibra.weatherapp.data.db.WeatherEntity
import com.ibra.weatherapp.data.model.ForecastResponse
import com.ibra.weatherapp.data.model.WeatherResponse
import com.ibra.weatherapp.repository.WeatherRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

const val API_KEY = "d8e26099c382239ab0bcc9a3fc303528"

sealed class WeatherUiState {
    object Idle : WeatherUiState()
    object Loading : WeatherUiState()
    data class Success(val data: WeatherResponse) : WeatherUiState()
    data class Error(val message: String) : WeatherUiState()
}

sealed class ForecastUiState {
    object Idle : ForecastUiState()
    object Loading : ForecastUiState()
    data class Success(val data: ForecastResponse) : ForecastUiState()
    data class Error(val message: String) : ForecastUiState()
}

class WeatherViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: WeatherRepository

    private val _weatherState = MutableStateFlow<WeatherUiState>(WeatherUiState.Idle)
    val weatherState: StateFlow<WeatherUiState> = _weatherState.asStateFlow()

    private val _forecastState = MutableStateFlow<ForecastUiState>(ForecastUiState.Idle)
    val forecastState: StateFlow<ForecastUiState> = _forecastState.asStateFlow()

    private val _recentCities = MutableStateFlow<List<WeatherEntity>>(emptyList())
    val recentCities: StateFlow<List<WeatherEntity>> = _recentCities.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    init {
        val dao = WeatherDatabase.getDatabase(application).weatherDao()
        repository = WeatherRepository(dao)
        getRecentCities()
        searchWeather("Nairobi")
        _searchQuery.value = "Nairobi"
    }

    fun onSearchQueryChange(query: String) {
        _searchQuery.value = query
    }

    fun searchWeather(city: String) {
        viewModelScope.launch {
            _weatherState.value = WeatherUiState.Loading
            try {
                val result = repository.getCurrentWeather(city, API_KEY)
                _weatherState.value = WeatherUiState.Success(result)
                saveCity(result)
            } catch (e: Exception) {
                _weatherState.value = WeatherUiState.Error(e.message ?: "Something went wrong")
            }
        }
    }

    fun getForecast(city: String) {
        viewModelScope.launch {
            _forecastState.value = ForecastUiState.Loading
            try {
                val result = repository.getForecast(city, API_KEY)
                _forecastState.value = ForecastUiState.Success(result)
            } catch (e: Exception) {
                _forecastState.value = ForecastUiState.Error(e.message ?: "Something went wrong")
            }
        }
    }

    private fun saveCity(weather: WeatherResponse) {
        viewModelScope.launch {
            val entity = WeatherEntity(
                cityName = weather.cityName,
                temperature = "${weather.main.temp.toInt()}°C",
                description = weather.weather[0].description,
                humidity = "${weather.main.humidity}%",
                windSpeed = "${weather.wind.speed} m/s"
            )
            repository.saveCity(entity)
        }
    }

    private fun getRecentCities() {
        viewModelScope.launch {
            repository.getRecentCities().collect {
                _recentCities.value = it
            }
        }
    }

    fun deleteCity(id: Int) {
        viewModelScope.launch {
            repository.deleteCity(id)
        }
    }

    fun deleteAllCities() {
        viewModelScope.launch {
            repository.deleteAllCities()
        }
    }
}

