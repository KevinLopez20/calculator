package com.example.calculator.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.calculator.ui.calculator.CalculatorScreen
import com.example.calculator.ui.history.HistoryScreen

object Routes {
    const val CALCULATOR = "calculator"
    const val HISTORY = "history"
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Routes.CALCULATOR) {
        composable(Routes.CALCULATOR) {
            CalculatorScreen(
                onNavigateToHistory = { navController.navigate(Routes.HISTORY) }
            )
        }
        composable(Routes.HISTORY) {
            HistoryScreen(
                onBack = { navController.popBackStack() }
            )
        }
    }
}