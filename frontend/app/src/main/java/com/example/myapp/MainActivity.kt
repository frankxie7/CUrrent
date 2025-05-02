package com.example.myapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.myapp.ui.screens.CarDetailScreen
import com.example.myapp.ui.screens.CheckoutScreen
import com.example.myapp.ui.screens.DeliveryScreen
import com.example.myapp.ui.screens.HomeScreen
import com.example.myapp.ui.theme.MyAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyAppTheme {
                val navController = rememberNavController()
                NavHost(navController = navController, startDestination = "home") {
                    composable("home") {
                        HomeScreen(navController)
                    }
                    composable(
                        "carDetail/{carId}",
                        arguments = listOf(navArgument("carId") { type = NavType.StringType })
                    ) {
                        val carId = it.arguments?.getString("carId") ?: ""
                        CarDetailScreen(carId = carId, navController = navController)
                    }
                    composable(
                        "checkout/{carId}",
                        arguments = listOf(navArgument("carId") { type = NavType.StringType })
                    ) {
                        val carId = it.arguments?.getString("carId") ?: ""
                        CheckoutScreen(carId = carId, navController = navController)
                    }
                    composable(
                        "delivery/{carId}",
                        arguments = listOf(navArgument("carId") { type = NavType.StringType })
                    ) {
                        val carId = it.arguments?.getString("carId") ?: ""
                        DeliveryScreen(carId = carId, navController = navController)
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AppPreview() {
    MyAppTheme {
        val navController = rememberNavController()
        HomeScreen(navController = navController)
    }
}
