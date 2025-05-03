package com.example.myapp.ui.screens

import android.app.DatePickerDialog
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.myapp.viewmodel.HomeViewModel
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun CheckoutScreen(
    carId: String,
    navController: NavController,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val listings by viewModel.listings.collectAsState(initial = emptyList())
    val car = listings.find { it.id.toString() == carId } ?: return
    val reservationNumber = remember { "#R-${(100000..999999).random()}" }

    val context = LocalContext.current
    val calendar = Calendar.getInstance()
    val formatter = remember { SimpleDateFormat("MMM dd, yyyy", Locale.getDefault()) }

    var startDate by remember { mutableStateOf("") }
    var endDate by remember { mutableStateOf("") }
    var location by remember { mutableStateOf("") }

    fun showDatePicker(onDateSelected: (String) -> Unit) {
        DatePickerDialog(
            context,
            { _, year, month, day ->
                calendar.set(year, month, day)
                onDateSelected(formatter.format(calendar.time))
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        ).show()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(text = car.vehicle_type, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(8.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .background(Color.LightGray)
        )

        Spacer(modifier = Modifier.height(16.dp))
        Text("Reservation #: $reservationNumber", fontWeight = FontWeight.Medium)

        Spacer(modifier = Modifier.height(24.dp))
        Text("Select Dates", fontWeight = FontWeight.SemiBold)

        Spacer(modifier = Modifier.height(8.dp))
        Button(onClick = { showDatePicker { startDate = it } }) {
            Text(text = if (startDate.isEmpty()) "Pick Start Date" else "Start: $startDate")
        }

        Spacer(modifier = Modifier.height(8.dp))
        Button(onClick = { showDatePicker { endDate = it } }) {
            Text(text = if (endDate.isEmpty()) "Pick End Date" else "End: $endDate")
        }

        Spacer(modifier = Modifier.height(24.dp))
        Text("Enter Delivery Location", fontWeight = FontWeight.SemiBold)
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = location,
            onValueChange = { location = it },
            placeholder = { Text("e.g., 123 Main St, Ithaca NY") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.weight(1f))

        Button(
            onClick = {
                navController.navigate("delivery/${car.id}")
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        ) {
            Text("Checkout")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CheckoutScreenPreview() {
    val navController = rememberNavController()
    CheckoutScreen(carId = "1", navController = navController)
}
