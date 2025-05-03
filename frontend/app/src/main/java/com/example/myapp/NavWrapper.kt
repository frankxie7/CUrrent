package com.example.myapp

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.myapp.ui.screens.CarDetailScreen
import com.example.myapp.ui.screens.CheckoutScreen
import com.example.myapp.ui.screens.DeliveryScreen
import com.example.myapp.ui.screens.HomeScreen
import com.example.myapp.ui.screens.ProfileScreen
import com.example.myapp.ui.screens.AddListingScreen
import com.example.myapp.viewmodel.HomeViewModel

@Composable
fun NavWrapper() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "home"
    ) {
        composable("home") {
            HomeScreen(navController = navController)
        }
        composable("carDetail/{carId}") { backStackEntry ->
            val carId = backStackEntry.arguments?.getString("carId") ?: ""
            CarDetailScreen(carId = carId, navController = navController)
        }
        composable("checkout/{carId}") { backStackEntry ->
            val carId = backStackEntry.arguments?.getString("carId") ?: ""
            CheckoutScreen(carId = carId, navController = navController)
        }
        composable("delivery/{carId}") { backStackEntry ->
            val carId = backStackEntry.arguments?.getString("carId") ?: ""
            DeliveryScreen(carId = carId, navController = navController)
        }
        composable("profile") {
            ProfileScreen(navController = navController)
        }
        composable("addListing") {
            AddListingScreen(navController = navController)
        }
    }
}
