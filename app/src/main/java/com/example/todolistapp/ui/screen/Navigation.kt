package com.example.todolistapp.ui.screen

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

@Composable
fun Navigation(
    viewModel: HomeScreenViewModel,
    binViewModel: RecycleBinViewModel
) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Screen.Home.route) {
        // Home Screen
        composable(Screen.Home.route) {
            HomeScreen(
                viewModel = viewModel,
                onNavigateToRecycleBin = { navController.navigate(Screen.RecycleBin.route) }
            )
        }

        // Recycle Bin Screen
        composable(Screen.RecycleBin.route) {
            RecycleBinScreen(
                viewModel = binViewModel,
                onNavigateToCurrentList = { navController.navigate(Screen.Home.route) },
                onRestoreTask = { },
            )
        }
    }
}
