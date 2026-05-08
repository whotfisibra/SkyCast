package com.ibra.weatherapp.ui.theme.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.ibra.weatherapp.navigation.ROUTE_FORECAST
import com.ibra.weatherapp.navigation.ROUTE_RECENT
import com.ibra.weatherapp.viewmodel.WeatherUiState
import com.ibra.weatherapp.viewmodel.WeatherViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

val worldCities = listOf(
    "Nairobi", "Mombasa", "Kisumu", "Nakuru", "Eldoret",
    "Cairo", "Casablanca", "Cape Town", "Colombo", "Chicago",
    "Calgary", "Caracas", "Chengdu", "Chennai", "Chittagong",
    "London", "Lagos", "Lusaka", "Lima", "Lahore", "Lisbon",
    "New York", "Naypyidaw", "Nouakchott", "Niamey", "Naples",
    "Paris", "Perth", "Prague", "Pretoria", "Port Louis",
    "Tokyo", "Tehran", "Toronto", "Tunis", "Tashkent",
    "Dubai", "Dakar", "Dar es Salaam", "Delhi", "Dhaka",
    "Berlin", "Bangkok", "Baghdad", "Baku", "Beijing",
    "Barcelona", "Bogota", "Brisbane", "Brussels", "Budapest",
    "Sydney", "Seoul", "Singapore", "Stockholm", "Santiago",
    "Rome", "Riyadh", "Rabat", "Reykjavik",
    "Madrid", "Manila", "Melbourne", "Mexico City", "Miami",
    "Mumbai", "Moscow", "Maputo", "Muscat",
    "Accra", "Addis Ababa", "Algiers", "Amman", "Amsterdam",
    "Ankara", "Athens", "Auckland",
    "Havana", "Helsinki", "Ho Chi Minh City", "Hong Kong",
    "Istanbul", "Islamabad",
    "Jakarta", "Johannesburg",
    "Kampala", "Karachi", "Kathmandu", "Khartoum", "Kinshasa", "Kuala Lumpur",
    "Oslo", "Ottawa",
    "Vienna", "Warsaw", "Washington", "Wellington",
    "Zurich"
).sorted()

fun getLocalTime(timezoneOffsetSeconds: Int): String {
    val utcTime = System.currentTimeMillis()
    val localTime = utcTime + (timezoneOffsetSeconds * 1000L)
    val sdf = SimpleDateFormat("HH:mm", Locale.getDefault())
    sdf.timeZone = TimeZone.getTimeZone("UTC")
    return sdf.format(Date(localTime))
}

fun getLocalDate(timezoneOffsetSeconds: Int): String {
    val utcTime = System.currentTimeMillis()
    val localTime = utcTime + (timezoneOffsetSeconds * 1000L)
    val sdf = SimpleDateFormat("EEEE, dd MMM", Locale.getDefault())
    sdf.timeZone = TimeZone.getTimeZone("UTC")
    return sdf.format(Date(localTime))
}

fun getTimeDifference(timezoneOffsetSeconds: Int): String {
    val localOffsetSeconds = TimeZone.getDefault().rawOffset / 1000
    val diffSeconds = timezoneOffsetSeconds - localOffsetSeconds
    val diffHours = diffSeconds / 3600
    val diffMinutes = Math.abs((diffSeconds % 3600) / 60)
    return when {
        diffHours == 0 && diffMinutes == 0 -> "Same time as you"
        diffHours > 0 && diffMinutes == 0 -> "+${diffHours}h from you"
        diffHours < 0 && diffMinutes == 0 -> "${diffHours}h from you"
        diffHours > 0 -> "+${diffHours}h ${diffMinutes}m from you"
        diffHours < 0 -> "${diffHours}h ${diffMinutes}m from you"
        else -> "+0h ${diffMinutes}m from you"
    }
}

@Composable
fun WeatherIcon(iconCode: String, size: Dp = 64.dp) {
    AsyncImage(
        model = "https://openweathermap.org/img/wn/$iconCode@2x.png",
        contentDescription = "Weather icon",
        modifier = Modifier.size(size)
    )
}

fun getBackgroundBrush(weatherMain: String): Brush {
    return when (weatherMain.lowercase()) {
        "clear" -> Brush.verticalGradient(
            colors = listOf(Color(0xFFFF8F00), Color(0xFFFFD54F))
        )
        "clouds" -> Brush.verticalGradient(
            colors = listOf(Color(0xFF546E7A), Color(0xFF90A4AE))
        )
        "rain", "drizzle" -> Brush.verticalGradient(
            colors = listOf(Color(0xFF1A237E), Color(0xFF42A5F5))
        )
        "thunderstorm" -> Brush.verticalGradient(
            colors = listOf(Color(0xFF212121), Color(0xFF37474F))
        )
        "snow" -> Brush.verticalGradient(
            colors = listOf(Color(0xFFB0BEC5), Color(0xFFECEFF1))
        )
        "mist", "fog", "haze" -> Brush.verticalGradient(
            colors = listOf(Color(0xFF607D8B), Color(0xFFB0BEC5))
        )
        else -> Brush.verticalGradient(
            colors = listOf(Color(0xFF1A237E), Color(0xFF42A5F5))
        )
    }
}

@Composable
fun HomeScreen(navController: NavController, viewModel: WeatherViewModel) {
    val weatherState by viewModel.weatherState.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()

    val suggestions by remember(searchQuery) {
        derivedStateOf {
            if (searchQuery.length >= 1) {
                worldCities.filter {
                    it.startsWith(searchQuery, ignoreCase = true)
                }.take(5)
            } else {
                emptyList()
            }
        }
    }

    val backgroundBrush = when (weatherState) {
        is WeatherUiState.Success -> {
            val data = (weatherState as WeatherUiState.Success).data
            getBackgroundBrush(data.weather[0].main)
        }
        else -> Brush.verticalGradient(
            colors = listOf(Color(0xFF1A237E), Color(0xFF42A5F5))
        )
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(brush = backgroundBrush)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(40.dp))

            Text(
                text = "SkyCast",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Search bar + autocomplete
            Box(modifier = Modifier.fillMaxWidth()) {
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        OutlinedTextField(
                            value = searchQuery,
                            onValueChange = { viewModel.onSearchQueryChange(it) },
                            placeholder = {
                                Text(
                                    "Enter city name...",
                                    color = Color.White.copy(alpha = 0.5f)
                                )
                            },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(12.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedTextColor = Color.White,
                                unfocusedTextColor = Color.White,
                                focusedBorderColor = Color.White,
                                unfocusedBorderColor = Color.White.copy(alpha = 0.5f)
                            ),
                            singleLine = true
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        IconButton(
                            onClick = {
                                if (searchQuery.isNotEmpty()) {
                                    viewModel.searchWeather(searchQuery)
                                }
                            },
                            modifier = Modifier
                                .background(
                                    Color.White.copy(alpha = 0.2f),
                                    RoundedCornerShape(12.dp)
                                )
                                .size(56.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Search,
                                contentDescription = "Search",
                                tint = Color.White
                            )
                        }
                    }

                    // Autocomplete dropdown
                    if (suggestions.isNotEmpty()) {
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .zIndex(1f),
                            shape = RoundedCornerShape(
                                bottomStart = 12.dp,
                                bottomEnd = 12.dp
                            ),
                            colors = CardDefaults.cardColors(
                                containerColor = Color(0xFF1A237E).copy(alpha = 0.95f)
                            )
                        ) {
                            Column {
                                suggestions.forEachIndexed { index, city ->
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .clickable {
                                                viewModel.onSearchQueryChange(city)
                                                viewModel.searchWeather(city)
                                            }
                                            .padding(horizontal = 16.dp, vertical = 12.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(text = "📍", fontSize = 14.sp)
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text(
                                            text = city,
                                            fontSize = 15.sp,
                                            color = Color.White,
                                            fontWeight = FontWeight.Medium
                                        )
                                    }
                                    if (index < suggestions.size - 1) {
                                        Divider(
                                            color = Color.White.copy(alpha = 0.1f),
                                            thickness = 0.5.dp
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            when (weatherState) {
                is WeatherUiState.Idle -> {
                    Text(
                        text = "Search for a city to see the weather",
                        color = Color.White.copy(alpha = 0.7f),
                        textAlign = TextAlign.Center,
                        fontSize = 16.sp
                    )
                }
                is WeatherUiState.Loading -> {
                    CircularProgressIndicator(color = Color.White)
                }
                is WeatherUiState.Success -> {
                    val data = (weatherState as WeatherUiState.Success).data
                    val localTime = getLocalTime(data.timezone)
                    val localDate = getLocalDate(data.timezone)
                    val timeDiff = getTimeDifference(data.timezone)

                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = Color.White.copy(alpha = 0.2f)
                        )
                    ) {
                        Column(
                            modifier = Modifier.padding(20.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "${data.cityName}, ${data.sys.country}",
                                fontSize = 22.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                            Spacer(modifier = Modifier.height(4.dp))

                            // Local time of the city
                            Text(
                                text = "🕐 $localTime  •  $localDate",
                                fontSize = 14.sp,
                                color = Color.White.copy(alpha = 0.8f)
                            )
                            Spacer(modifier = Modifier.height(2.dp))

                            // Time difference from user
                            Card(
                                shape = RoundedCornerShape(20.dp),
                                colors = CardDefaults.cardColors(
                                    containerColor = Color.White.copy(alpha = 0.15f)
                                )
                            ) {
                                Text(
                                    text = timeDiff,
                                    fontSize = 12.sp,
                                    color = Color.White,
                                    modifier = Modifier.padding(
                                        horizontal = 12.dp,
                                        vertical = 4.dp
                                    )
                                )
                            }

                            Spacer(modifier = Modifier.height(8.dp))
                            WeatherIcon(
                                iconCode = data.weather[0].icon,
                                size = 100.dp
                            )
                            Text(
                                text = "${data.main.temp.toInt()}°C",
                                fontSize = 64.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                            Text(
                                text = data.weather[0].description.replaceFirstChar { it.uppercase() },
                                fontSize = 18.sp,
                                color = Color.White.copy(alpha = 0.8f)
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                                Text(
                                    text = "↓ ${data.main.tempMin.toInt()}°C",
                                    fontSize = 14.sp,
                                    color = Color.White.copy(alpha = 0.7f)
                                )
                                Text(
                                    text = "↑ ${data.main.tempMax.toInt()}°C",
                                    fontSize = 14.sp,
                                    color = Color.White.copy(alpha = 0.7f)
                                )
                            }
                            Spacer(modifier = Modifier.height(16.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceEvenly
                            ) {
                                WeatherInfoItem(
                                    label = "Feels Like",
                                    value = "${data.main.feelsLike.toInt()}°C"
                                )
                                WeatherInfoItem(
                                    label = "Humidity",
                                    value = "${data.main.humidity}%"
                                )
                                WeatherInfoItem(
                                    label = "Wind",
                                    value = "${data.wind.speed} m/s"
                                )
                            }
                            Spacer(modifier = Modifier.height(16.dp))
                            Button(
                                onClick = {
                                    viewModel.getForecast(searchQuery)
                                    navController.navigate(ROUTE_FORECAST)
                                },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color.White.copy(alpha = 0.3f)
                                ),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text("See 5 Day Forecast", color = Color.White)
                            }
                        }
                    }
                }
                is WeatherUiState.Error -> {
                    val message = (weatherState as WeatherUiState.Error).message
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = Color.Red.copy(alpha = 0.3f)
                        )
                    ) {
                        Text(
                            text = "Error: $message",
                            color = Color.White,
                            modifier = Modifier.padding(16.dp),
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = { navController.navigate(ROUTE_RECENT) },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.White.copy(alpha = 0.2f)
                ),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Recently Searched Cities", color = Color.White)
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
fun WeatherInfoItem(label: String, value: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = value,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )
        Text(
            text = label,
            fontSize = 12.sp,
            color = Color.White.copy(alpha = 0.7f)
        )
    }
}

@Preview(showSystemUi = true)
@Composable
private fun HomePreview() {
    HomeScreen(navController = rememberNavController(), viewModel = viewModel())
}