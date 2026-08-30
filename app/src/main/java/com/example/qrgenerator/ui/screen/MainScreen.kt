package com.example.qrgenerator.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.qrgenerator.Screen

@Composable
fun InputScreen(navController: NavController) {

    var title by rememberSaveable { mutableStateOf("Investap Production without app protectt v12.69.") }
    var qrData by rememberSaveable { mutableStateOf("") }
    var isInvesTap by rememberSaveable { mutableStateOf(true) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center
    ) {

        Row(
            modifier = Modifier.fillMaxWidth().padding(8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            RadioButton(
                selected = isInvesTap,
                onClick = {
                    isInvesTap = !isInvesTap
                    title = if (isInvesTap) {
                        "Investap Production without app protectt v12.69."
                    } else {
                        "InvestEasy Production without app protectt v7.58."
                    }
                }
            )
            Text(
                text = "Investap Build",
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding( 8.dp)
            )
        }

        OutlinedTextField(
            value = title,
            onValueChange = { title = it },
            label = { Text("Title") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = qrData,
            onValueChange = { qrData = it },
            label = { Text("QR Data") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                if (title.isNotBlank() && qrData.isNotBlank()) {
                    navController.navigate(
                        Screen.Qr.createRoute(title, qrData)
                    )
                }
            },
            modifier = Modifier.fillMaxWidth().height(48.dp)
        ) {
            Text("Generate QR")
        }
    }
}