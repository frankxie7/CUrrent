package com.example.myapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.myapp.viewmodel.HomeViewModel

@Composable
fun DeliveryScreen(
    carId: String,
    navController: NavController,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val listings by viewModel.listings.collectAsState(initial = emptyList())
    val car = listings.find { it.id.toString() == carId } ?: return

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Your ${car.vehicle_type} is on the way!",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(24.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(180.dp)
                .background(Color.Gray)
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Estimated Arrival: 15 minutes",
            fontSize = 18.sp
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Delivery Location: 123 Main St, Ithaca NY",
            fontSize = 16.sp,
            color = Color.DarkGray
        )
    }
}
