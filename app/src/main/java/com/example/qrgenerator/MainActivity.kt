package com.example.qrgenerator

import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import com.example.qrgenerator.ui.screen.AppNavGraph

sealed class Screen(val route: String) {

    object Input : Screen("input")

    object Qr : Screen("qr?title={title}&data={data}") {
        fun createRoute(title: String, data: String): String {
            return "qr?title=${Uri.encode(title)}&data=${Uri.encode(data)}"
        }
    }
}


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MaterialTheme {
                Surface {
                    AppNavGraph()
                }
            }
        }
    }
}
