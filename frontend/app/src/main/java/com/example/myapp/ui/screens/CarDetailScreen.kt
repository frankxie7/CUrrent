package com.example.myapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.myapp.viewmodel.HomeViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CarDetailScreen(carId: String, navController: NavController, viewModel: HomeViewModel = hiltViewModel()) {
    val listings by viewModel.listings.collectAsState(initial = emptyList())
    val listing = listings.find { it.id.toString() == carId } ?: return

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Car Details") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .background(Color.Gray)
            )

            Spacer(Modifier.height(16.dp))

            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = listing.vehicle_type,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "$${listing.price_per_day}/day",
                    fontSize = 20.sp
                )
            }

            Spacer(Modifier.height(12.dp))

            Column {
                Text(
                    text = "Available from: ${listing.available_from}",
                    fontSize = 14.sp
                )
                Text(
                    text = "Available to: ${listing.available_to}",
                    fontSize = 14.sp
                )
                Text(
                    text = "Currently Rented: ${if (listing.is_rented) "Yes" else "No"}",
                    fontSize = 14.sp,
                    color = if (listing.is_rented) Color.Red else Color.Green
                )
            }

            Spacer(Modifier.weight(1f))

            Button(
                onClick = {
                    navController.navigate("checkout/${listing.id}")
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Rent This Vehicle", fontSize = 18.sp)
            }
        }
    }
}
