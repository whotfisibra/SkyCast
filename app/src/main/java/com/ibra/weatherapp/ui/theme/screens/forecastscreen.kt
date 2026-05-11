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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.ibra.weatherapp.data.model.ForecastItem
import com.ibra.weatherapp.navigation.ROUTE_HOME
import com.ibra.weatherapp.navigation.ROUTE_PINNED
import com.ibra.weatherapp.navigation.ROUTE_RECENT
import com.ibra.weatherapp.viewmodel.ForecastUiState
import com.ibra.weatherapp.viewmodel.WeatherViewModel

fun getTimeLabel(dateTime: String): String {
    val hour = dateTime.substring(11, 13).toIntOrNull() ?: 0
    return when (hour) {
        in 5..11 -> "🌅 Morning"
        in 12..16 -> "☀️ Afternoon"
        in 17..20 -> "🌇 Evening"
        else -> "🌙 Night"
    }
}

fun formatDay(dateTime: String): String {
    return try {
        val parts = dateTime.substring(0, 10).split("-")
        val months = listOf(
            "", "Jan", "Feb", "Mar", "Apr", "May", "Jun",
            "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"
        )
        val month = months[parts[1].toInt()]
        val day = parts[2].toInt()
        "$day $month"
    } catch (e: Exception) {
        dateTime.substring(0, 10)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ForecastScreen(navController: NavController, viewModel: WeatherViewModel) {
    val forecastState by viewModel.forecastState.collectAsState()

    Scaffold(
        containerColor = Color.Transparent,
        bottomBar = {
            NavigationBar(containerColor = Color.White.copy(alpha = 0.15f)) {
                NavigationBarItem(
                    selected = false,
                    onClick = { navController.navigate(ROUTE_PINNED) },
                    icon = {
                        Icon(
                            Icons.Default.Home,
                            contentDescription = "Pinned",
                            tint = Color.White
                        )
                    },
                    label = { Text("Pinned", color = Color.White, fontSize = 11.sp) },
                    colors = NavigationBarItemDefaults.colors(
                        indicatorColor = Color.White.copy(alpha = 0.2f)
                    )
                )
                NavigationBarItem(
                    selected = false,
                    onClick = { navController.navigate(ROUTE_HOME) },
                    icon = {
                        Icon(
                            Icons.Default.Search,
                            contentDescription = "Search",
                            tint = Color.White
                        )
                    },
                    label = { Text("Search", color = Color.White, fontSize = 11.sp) },
                    colors = NavigationBarItemDefaults.colors(
                        indicatorColor = Color.White.copy(alpha = 0.2f)
                    )
                )
                NavigationBarItem(
                    selected = false,
                    onClick = { navController.navigate(ROUTE_RECENT) },
                    icon = {
                        Icon(
                            Icons.Default.List,
                            contentDescription = "Recent",
                            tint = Color.White
                        )
                    },
                    label = { Text("Recent", color = Color.White, fontSize = 11.sp) },
                    colors = NavigationBarItemDefaults.colors(
                        indicatorColor = Color.White.copy(alpha = 0.2f)
                    )
                )
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(Color(0xFF1A237E), Color(0xFF42A5F5))
                    )
                )
                .padding(innerPadding)
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                TopAppBar(
                    title = {
                        Text(
                            text = "5 Day Forecast",
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = { navController.popBackStack() }) {
                            Icon(
                                imageVector = Icons.Default.ArrowBack,
                                contentDescription = "Back",
                                tint = Color.White
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color.Transparent
                    )
                )

                when (forecastState) {
                    is ForecastUiState.Idle -> {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "No forecast data available",
                                color = Color.White,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                    is ForecastUiState.Loading -> {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator(color = Color.White)
                        }
                    }
                    is ForecastUiState.Success -> {
                        val data = (forecastState as ForecastUiState.Success).data

                        val groupedByDay = data.list.groupBy {
                            it.dateTime.substring(0, 10)
                        }

                        Text(
                            text = "${data.city.name}, ${data.city.country}",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            modifier = Modifier.padding(horizontal = 16.dp)
                        )
                        Text(
                            text = "Tap a day to see hourly breakdown",
                            fontSize = 12.sp,
                            color = Color.White.copy(alpha = 0.6f),
                            modifier = Modifier.padding(
                                horizontal = 16.dp,
                                vertical = 4.dp
                            )
                        )
                        Spacer(modifier = Modifier.height(8.dp))

                        LazyColumn(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(horizontal = 16.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            items(groupedByDay.entries.toList()) { (date, items) ->
                                ExpandableDayCard(date = date, items = items)
                            }
                        }
                    }
                    is ForecastUiState.Error -> {
                        val message = (forecastState as ForecastUiState.Error).message
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "Error: $message",
                                color = Color.White,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.padding(16.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ExpandableDayCard(date: String, items: List<ForecastItem>) {
    var expanded by remember { mutableStateOf(false) }
    val firstItem = items.first()
    val minTemp = items.minOf { it.main.temp }.toInt()
    val maxTemp = items.maxOf { it.main.temp }.toInt()

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White.copy(alpha = 0.2f)
        )
    ) {
        Column {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { expanded = !expanded }
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = formatDay(date),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Text(
                        text = firstItem.weather[0].description
                            .replaceFirstChar { it.uppercase() },
                        fontSize = 12.sp,
                        color = Color.White.copy(alpha = 0.7f)
                    )
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    WeatherIcon(iconCode = firstItem.weather[0].icon, size = 40.dp)
                    Spacer(modifier = Modifier.width(8.dp))
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "↑ $maxTemp°C",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Text(
                            text = "↓ $minTemp°C",
                            fontSize = 12.sp,
                            color = Color.White.copy(alpha = 0.7f)
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Icon(
                        imageVector = if (expanded)
                            Icons.Default.KeyboardArrowUp
                        else
                            Icons.Default.KeyboardArrowDown,
                        contentDescription = "Expand",
                        tint = Color.White
                    )
                }
            }

            if (expanded) {
                Divider(color = Color.White.copy(alpha = 0.2f), thickness = 0.5.dp)
                items.forEach { item ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.width(100.dp)) {
                            Text(
                                text = getTimeLabel(item.dateTime),
                                fontSize = 13.sp,
                                color = Color.White,
                                fontWeight = FontWeight.Medium
                            )
                            Text(
                                text = item.dateTime.substring(11, 16),
                                fontSize = 11.sp,
                                color = Color.White.copy(alpha = 0.5f)
                            )
                        }
                        WeatherIcon(iconCode = item.weather[0].icon, size = 36.dp)
                        Text(
                            text = item.weather[0].description
                                .replaceFirstChar { it.uppercase() },
                            fontSize = 12.sp,
                            color = Color.White.copy(alpha = 0.8f),
                            modifier = Modifier.width(90.dp),
                            textAlign = TextAlign.Center
                        )
                        Text(
                            text = "${item.main.temp.toInt()}°C",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                    Divider(
                        color = Color.White.copy(alpha = 0.08f),
                        thickness = 0.5.dp,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}

@Composable
fun ForecastCard(item: ForecastItem) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White.copy(alpha = 0.2f)
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = item.dateTime.substring(0, 10),
                    fontSize = 14.sp,
                    color = Color.White.copy(alpha = 0.7f)
                )
                Text(
                    text = item.dateTime.substring(11, 16),
                    fontSize = 12.sp,
                    color = Color.White.copy(alpha = 0.5f)
                )
            }
            WeatherIcon(iconCode = item.weather[0].icon, size = 48.dp)
            Text(
                text = item.weather[0].description.replaceFirstChar { it.uppercase() },
                fontSize = 14.sp,
                color = Color.White,
                textAlign = TextAlign.Center
            )
            Text(
                text = "${item.main.temp.toInt()}°C",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }
    }
}

@Preview(showSystemUi = true)
@Composable
private fun ForecastPreview() {
    ForecastScreen(navController = rememberNavController(), viewModel = viewModel())
}