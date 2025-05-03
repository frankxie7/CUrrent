package com.example.myapp.ui.screens

import android.app.DatePickerDialog
import android.widget.DatePicker
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.util.*
import com.example.myapp.util.CURRENT_USER_ID

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddListingScreen(navController: NavController) {
    val context = LocalContext.current

    var vehicleType by remember { mutableStateOf("Car") }
    val vehicleTypes = listOf("Car", "Bike", "Scooter")
    var expanded by remember { mutableStateOf(false) }

    var pricePerDay by remember { mutableStateOf("") }
    var availableFrom by remember { mutableStateOf("") }
    var availableTo by remember { mutableStateOf("") }

    val calendar = Calendar.getInstance()

    fun showDatePicker(onDateSelected: (String) -> Unit) {
        DatePickerDialog(
            context,
            { _: DatePicker, year: Int, month: Int, day: Int ->
                onDateSelected(String.format("%04d-%02d-%02d", year, month + 1, day))
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        ).show()
    }

    Column(modifier = Modifier.padding(16.dp)) {
        Text("Add a New Vehicle", style = MaterialTheme.typography.titleLarge)

        Spacer(modifier = Modifier.height(16.dp))

        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded }
        ) {
            TextField(
                value = vehicleType,
                onValueChange = {},
                readOnly = true,
                label = { Text("Vehicle Type") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                modifier = Modifier.menuAnchor().fillMaxWidth()
            )
            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                vehicleTypes.forEach { type ->
                    DropdownMenuItem(
                        text = { Text(type) },
                        onClick = {
                            vehicleType = type
                            expanded = false
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        TextField(
            value = pricePerDay,
            onValueChange = { pricePerDay = it },
            label = { Text("Price Per Day") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(12.dp))

        Button(
            onClick = { showDatePicker { availableFrom = it } },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(if (availableFrom.isBlank()) "Select Start Date" else "Start: $availableFrom")
        }

        Spacer(modifier = Modifier.height(12.dp))

        Button(
            onClick = { showDatePicker { availableTo = it } },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(if (availableTo.isBlank()) "Select End Date" else "End: $availableTo")
        }

        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = {
                val json = JSONObject().apply {
                    put("vehicle_type", vehicleType)
                    put("price_per_day", pricePerDay)
                    put("available_from", availableFrom)
                    put("available_to", availableTo)
                    put("owner_id", CURRENT_USER_ID)
                }

                val requestBody = json.toString().toRequestBody("application/json".toMediaType())

                CoroutineScope(Dispatchers.IO).launch {
                    val client = OkHttpClient()
                    val request = Request.Builder()
                        .url("http://10.0.2.2:8000/api/listings/")
                        .post(requestBody)
                        .build()

                    val response = client.newCall(request).execute()
                    if (response.isSuccessful) {
                        navController.navigate("profile") {
                            popUpTo("profile") { inclusive = true }
                        }
                    }
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Submit")
        }
    }
}
