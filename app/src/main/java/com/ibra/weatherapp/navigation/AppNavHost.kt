package com.ibra.weatherapp.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.ibra.weatherapp.ui.theme.screens.ForecastScreen
import com.ibra.weatherapp.ui.theme.screens.HomeScreen
import com.ibra.weatherapp.ui.theme.screens.PinnedScreen
import com.ibra.weatherapp.ui.theme.screens.RecentScreen
import com.ibra.weatherapp.viewmodel.WeatherViewModel

@Composable
fun AppNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    startDestination: String = ROUTE_PINNED
) {
    val viewModel: WeatherViewModel = viewModel()

    NavHost(
        navController = navController,
        modifier = modifier,
        startDestination = startDestination
    ) {
        composable(ROUTE_PINNED) {
            PinnedScreen(navController = navController, viewModel = viewModel)
        }
        composable(ROUTE_HOME) {
            HomeScreen(navController = navController, viewModel = viewModel)
        }
        composable(ROUTE_FORECAST) {
            ForecastScreen(navController = navController, viewModel = viewModel)
        }
        composable(ROUTE_RECENT) {
            RecentScreen(navController = navController, viewModel = viewModel)
        }
    }
}
