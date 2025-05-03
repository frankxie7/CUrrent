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
import com.example.myapp.util.CURRENT_USER_ID


object UserSession {
    var currentUserId: Int? = null
}


@Composable
fun ProfileScreen(navController: NavController, viewModel: HomeViewModel = hiltViewModel()) {
    val userVehicles = viewModel.listings.collectAsState().value.filter { it.owner_id == CURRENT_USER_ID }

    Column(modifier = Modifier.padding(16.dp)) {
        Text("Your Vehicles", fontSize = 22.sp, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(12.dp))
        LazyColumn {
            items(userVehicles) { car ->
                CarListItem(car = car, onClick = { /* maybe view details */ })
                HorizontalDivider()
            }
        }
        Spacer(Modifier.height(24.dp))
        Button(onClick = {
            navController.navigate("addListing")
        }) {
            Text("Add Vehicle")
        }
    }
}
