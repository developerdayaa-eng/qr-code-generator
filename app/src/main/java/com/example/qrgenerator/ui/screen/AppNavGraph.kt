package com.example.qrgenerator.ui.screen

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.qrgenerator.Screen

@Composable
fun AppNavGraph() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Screen.Input.route
    ) {

        composable(Screen.Input.route) {
            InputScreen(navController)
        }

        composable(
            route = Screen.Qr.route,
            arguments = listOf(
                navArgument("title") { type = NavType.StringType },
                navArgument("data") { type = NavType.StringType }
            )
        ) { backStackEntry ->

            val title = backStackEntry.arguments?.getString("title").orEmpty()
            val data = backStackEntry.arguments?.getString("data").orEmpty()

            QrScreen(
                title = title,
                qrData = data
            )
        }
    }
}
