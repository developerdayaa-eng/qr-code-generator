package com.example.qrgenerator

import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.qrgenerator.ui.screen.InputScreen
import com.example.qrgenerator.ui.screen.QrScreen

sealed class Screen {
    object Input : Screen()
    object Qr : Screen()
}

@Composable
fun App() {
    var screen by remember { mutableStateOf<Screen>(Screen.Input) }
    var title by remember { mutableStateOf("") }
    var qrData by remember { mutableStateOf("") }

    MaterialTheme(colorScheme = darkColorScheme()) {
        BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
            val isWide = maxWidth > 600.dp

            if (isWide) {
                // Desktop / Tablet → show phone frame on dark bg
                PhoneShell {
                    AppContent(
                        screen = screen,
                        title = title,
                        qrData = qrData,
                        onTitleChange = { title = it },
                        onQrDataChange = { qrData = it },
                        onScreenChange = { screen = it }
                    )
                }
            } else {
                // Mobile → full screen
                AppContent(
                    screen = screen,
                    title = title,
                    qrData = qrData,
                    onTitleChange = { title = it },
                    onQrDataChange = { qrData = it },
                    onScreenChange = { screen = it }
                )
            }
        }
    }
}

@Composable
private fun AppContent(
    screen: Screen,
    title: String,
    qrData: String,
    onTitleChange: (String) -> Unit,
    onQrDataChange: (String) -> Unit,
    onScreenChange: (Screen) -> Unit
) {
    when (screen) {
        is Screen.Input -> InputScreen(
            title = title,
            qrData = qrData,
            onTitleChange = onTitleChange,
            onQrDataChange = onQrDataChange,
            onNavigate = { onScreenChange(Screen.Qr) }
        )
        is Screen.Qr -> QrScreen(
            title = title,
            qrData = qrData,
            onBack = { onScreenChange(Screen.Input) }
        )
    }
}
