package com.example.gamelibrary.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.gamelibrary.ui.screens.DetailScreen
import com.example.gamelibrary.ui.screens.HomeScreen
import com.example.gamelibrary.ui.screens.LibraryScreen
import com.example.gamelibrary.ui.screens.ProfileScreen
import com.example.gamelibrary.ui.screens.SearchScreen
import com.example.gamelibrary.ui.viewmodel.GameViewModel
import com.example.gamelibrary.ui.viewmodel.ProfileViewModel

@Composable
fun AppNavGraph(
    navController: NavHostController,
    gameViewModel: GameViewModel,
    profileViewModel: ProfileViewModel,
    onGameClick: (Int) -> Unit,
    onSearchClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Home.route,
        modifier = modifier
    ) {
        // Экраны нижней панели
        composable(Screen.Home.route) {
            HomeScreen(
                onGameClick = onGameClick,
                onSearchClick = onSearchClick,
                gameViewModel = gameViewModel
            )
        }
        composable(Screen.Library.route) {
            LibraryScreen(
                onGameClick = onGameClick,
                gameViewModel = gameViewModel
            )
        }
        composable(Screen.Profile.route) {
            ProfileScreen(
                navController = navController,
                profileViewModel = profileViewModel
            )
        }

        // Вложенные экраны (без нижней панели)
        composable(Screen.Detail.route) { backStackEntry ->
            val id = backStackEntry.arguments?.getString("gameId")?.toIntOrNull() ?: 1
            DetailScreen(
                gameId = id,
                onBack = { navController.popBackStack() },
                gameViewModel = gameViewModel
            )
        }

        composable(Screen.Search.route) {
            SearchScreen(
                onBack = { navController.popBackStack() },
                onGameClick = { id -> navController.navigate(Screen.Detail.createRoute(id)) },
                gameViewModel = gameViewModel
            )
        }
    }
}