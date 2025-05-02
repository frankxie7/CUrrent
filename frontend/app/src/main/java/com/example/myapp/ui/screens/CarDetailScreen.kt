package com.example.myapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

@Composable
fun CarDetailScreen(carId: String, navController: NavController) {
    val car = allCars.find { it.id == carId } ?: return

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Gray)
            )
        }
        Spacer(Modifier.height(16.dp))
        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = car.name,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "$${car.pricePerDay}/day",
                fontSize = 20.sp,
                fontWeight = FontWeight.Medium
            )
        }

        Spacer(Modifier.height(12.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .background(Color.LightGray, shape = CircleShape)
            )
            Spacer(Modifier.width(12.dp))
            Spacer(Modifier.width(12.dp))
            Column {
                Text(
                    text = car.renterName,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = "★ ${car.renterRating} (95 reviews)",
                    fontSize = 14.sp
                )
            }
        }

        Spacer(Modifier.height(12.dp))

        Column(
        ) {
            Text(
                text = "Description",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(Modifier.height(8.dp))
            Text(
                modifier = Modifier.padding(start = 12.dp),
                text = car.description,
                fontSize = 12.sp,
                lineHeight = 22.sp
            )
        }
        Spacer(Modifier.height(12.dp))
        Text(
            text = "Listed on: ${car.listedDate}",
            fontSize = 14.sp,
            color = Color.Gray
        )

        Spacer(Modifier.height(12.dp))
        Text(
            text = "Available: ${car.availability}",
            fontSize = 14.sp
        )

        Spacer(Modifier.height(12.dp))
        Text(
            text = "Pickup Window: ${car.pickupWindow}",
            fontSize = 14.sp
        )

        Spacer(Modifier.weight(1f))
        Row {
            Button(
                onClick = {
                    navController.navigate("checkout/${car.id}")
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Rent This Car", fontSize = 18.sp)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CarDetailScreenPreview() {
    val navController = rememberNavController()
    CarDetailScreen(carId = "1", navController = navController)
}
