package com.example.myapp.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.myapp.retrofit.Listing
import com.example.myapp.viewmodel.HomeViewModel
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.material.icons.filled.Refresh

enum class VehicleType { Car, Bike, Scooter }

@Composable
fun CarListItem(car: Listing, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(12.dp)
    ) {
        Column(Modifier.weight(1f)) {
            Text(text = car.vehicle_type, fontSize = 18.sp, fontWeight = FontWeight.Bold)
            Text(text = "$${car.price_per_day}/day", fontSize = 14.sp)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavController,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val listings by viewModel.listings.collectAsState(initial = emptyList())
    var selectedType by remember { mutableStateOf(VehicleType.Car) }

    val filteredCars = listings.filter {
        it.vehicle_type.equals(selectedType.name, ignoreCase = true)
    }

    LaunchedEffect(Unit) {
        println("UI listings count: ${listings.size}")
        viewModel.refresh()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Box(modifier = Modifier.fillMaxWidth()) {
                        Text(
                            text = "CUrrent",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.align(Alignment.Center)
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = { }) {
                        Icon(Icons.Default.Menu, contentDescription = "Menu")
                    }
                },
                actions = {
                    IconButton(onClick = { viewModel.refresh() }) {
                        Icon(Icons.Default.Refresh, contentDescription = "Refresh")
                    }
                    IconButton(onClick = {
                        navController.navigate("profile")
                    }) {
                        Icon(Icons.Default.Person, contentDescription = "Profile")
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            Modifier
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            ScrollableTabRow(selectedTabIndex = selectedType.ordinal) {
                VehicleType.entries.forEachIndexed { index, type ->
                    Tab(
                        selected = selectedType.ordinal == index,
                        onClick = { selectedType = type },
                        text = { Text(type.name) }
                    )
                }
            }

            Spacer(Modifier.height(16.dp))

            LazyColumn {
                items(filteredCars) { car ->
                    CarListItem(car = car) {
                        viewModel.selectedListing = car
                        navController.navigate("carDetail/${car.id}")
                    }
                    HorizontalDivider()
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    val dummyNavController = rememberNavController()
    HomeScreen(navController = dummyNavController)
}
