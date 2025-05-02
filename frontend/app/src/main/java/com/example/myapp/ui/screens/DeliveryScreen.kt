package com.example.myapp.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import androidx.compose.foundation.clickable
import androidx.compose.ui.Alignment
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun DeliveryScreen(
    carId: String,
    navController: NavController
) {
    val car = allCars.find { it.id == carId } ?: return

    Box(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Column(modifier = Modifier.fillMaxSize()) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                Text(
                    text = "✕",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .padding(8.dp)
                        .clickable {
                            navController.popBackStack(route = "home", inclusive = false)
                        }
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Delivered to your door",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text(text = "Car: ${car.name}", fontSize = 18.sp, fontWeight = FontWeight.Medium)
            Text(text = "Location: TBD", fontSize = 18.sp, fontWeight = FontWeight.Medium)
            Text(text = "Rental Dates: TBD", fontSize = 18.sp, fontWeight = FontWeight.Medium)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DeliveryScreenPreview() {
    val dummyNavController = rememberNavController()
    DeliveryScreen(
        carId = "1",
        navController = dummyNavController
    )
}
