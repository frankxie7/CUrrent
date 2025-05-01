package com.example.current.ui.screens

import android.R.attr.onClick
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.ExperimentalMaterial3Api // it may leave soon but wtv
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController

enum class VehicleType { Car, Bike, Scooter }
data class Car(
    val id: String,
    val name: String,
    val type: VehicleType,
    val pricePerDay: Double,
    val description: String,
    val listedDate: String,
    val availability: String,
    val pickupWindow: String,
    val imageUrl: String,
    val renterName: String,
    val renterRating: Double
)
val allCars = listOf(
    Car(
        id = "1",
        name = "Lightning McQueen",
        type = VehicleType.Car,
        pricePerDay = 120.0,
        description = """Model: Rust-eze Lightning GT
Top Speed: 200 mph (321 km/h)
0–60 mph: 3.2 seconds
Engine: V8 rear-mounted piston engine
Horsepower: 750 hp
Transmission: 6-speed manual with turbo boost override
Fuel Type: 100% synthetic race fuel
Tires: Lightyear racing slicks
Special: Crew radio, trash talk cam, Kachow underglow""",
        listedDate = "April 27, 2025, 3:45 PM",
        availability = "April 29 – May 2, 2025",
        pickupWindow = "8:00 AM – 8:00 PM",
        imageUrl = "https://images.unsplash.com/photo-1603826561566-b1735c34c893", // red race car
        renterName = "Owen Wilson",
        renterRating = 5.0
    ))

@Composable
fun CarListItem(car: Car, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(12.dp)
    ) {
        Column(Modifier.weight(1f)) {
            Text(text = car.name, style = MaterialTheme.typography.titleMedium)
            Text(text = "$${car.pricePerDay}/day", style = MaterialTheme.typography.bodyMedium)
        }
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
@Override
fun HomeScreen(
    navController: NavController
) {
    var selectedType by remember { mutableStateOf(VehicleType.Car) }
    val filteredCars = allCars.filter { it.type == selectedType }

    Scaffold( // good for automatic padding and more smart than rows
        topBar = {
            TopAppBar( // just the upper bar
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
                    IconButton(onClick = { }) { // if i click on the icon, then it does some shit
                        Icon(
                            Icons.Default.Menu,
                            contentDescription = "Menu"
                        ) // uses default icon for menu
                    }
                },
                actions = {
                    IconButton(onClick = { /* profile */ }) {
                        Icon(
                            Icons.Default.Person,
                            contentDescription = "Profile"
                        ) // uses default icon for profile
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
                VehicleType.entries.forEachIndexed { index, type -> // for each of the values in Vehcile types, it creates something there
                    Tab(
                        selected = selectedType.ordinal == index,
                        onClick = { selectedType = type },
                        text = { Text(type.name) }
                    )
                }
            }

            Spacer(Modifier.height(16.dp));

            LazyColumn {
                items(filteredCars) { car ->
                    CarListItem(car = car) {
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
