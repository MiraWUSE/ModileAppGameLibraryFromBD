package com.example.gamelibrary

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.gamelibrary.navigation.AppNavGraph
import com.example.gamelibrary.navigation.Screen
import com.example.gamelibrary.ui.viewmodel.GameViewModel
import com.example.gamelibrary.ui.viewmodel.ProfileViewModel
import com.example.gamelibrary.ui.viewmodel.ViewModelFactory

// 🎨 Цвета для Bottom Navigation в стиле Steam
object BottomNavColors {
    val Background = Color(0xFF171A21)        // Тёмный фон
    val Surface = Color(0xFF1B2838)           // Фон панели
    val Selected = Color(0xFF66C0F4)          // Синий для выбранного
    val Unselected = Color(0xFFC7D5E0)        // Серый для невыбранного
}

@Composable
fun GameLibraryApp(viewModelFactory: ViewModelFactory) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    // Получаем ViewModel
    val gameViewModel: GameViewModel = viewModel(factory = viewModelFactory)
    val profileViewModel: ProfileViewModel = viewModel(factory = viewModelFactory)

    // Адаптивность
    val screenWidthDp = LocalConfiguration.current.screenWidthDp
    val isTablet = screenWidthDp > 600

    // Скрываем BottomBar на экранах деталей, поиска и на планшетах
    val showBottomBar = when (currentRoute) {
        Screen.Home.route, Screen.Library.route, Screen.Profile.route -> !isTablet
        else -> false
    }

    Scaffold(
        bottomBar = {
            if (showBottomBar) {
                SteamBottomNavigationBar(
                    navController = navController,
                    currentRoute = currentRoute
                )
            }
        }
    ) { innerPadding ->
        AppNavGraph(
            navController = navController,
            gameViewModel = gameViewModel,
            profileViewModel = profileViewModel,
            onGameClick = { id -> navController.navigate(Screen.Detail.createRoute(id)) },
            onSearchClick = { navController.navigate(Screen.Search.route) },
            modifier = Modifier.padding(innerPadding)
        )
    }
}

// 🎮 Bottom Navigation Bar в стиле Steam
@Composable
fun SteamBottomNavigationBar(navController: NavHostController, currentRoute: String?) {
    NavigationBar(
        containerColor = BottomNavColors.Surface,
        contentColor = BottomNavColors.Unselected,
        tonalElevation = 8.dp
    ) {
        val items = listOf(
            Screen.Home to "Home",
            Screen.Library to "Library",
            Screen.Profile to "Profile"
        )

        items.forEach { (screen, label) ->
            NavigationBarItem(
                icon = {
                    Icon(
                        imageVector = when(screen) {
                            Screen.Home -> Icons.Filled.Home
                            Screen.Library -> Icons.Filled.Apps
                            Screen.Profile -> Icons.Filled.Person
                            else -> Icons.Filled.Home
                        },
                        contentDescription = label
                    )
                },
                label = {
                    Text(
                        text = label,
                        color = if (currentRoute == screen.route)
                            BottomNavColors.Selected
                        else
                            BottomNavColors.Unselected
                    )
                },
                selected = currentRoute == screen.route,
                onClick = {
                    if (currentRoute != screen.route) {
                        navController.navigate(screen.route) {
                            popUpTo(navController.graph.startDestinationId) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = BottomNavColors.Selected,
                    unselectedIconColor = BottomNavColors.Unselected,
                    selectedTextColor = BottomNavColors.Selected,
                    unselectedTextColor = BottomNavColors.Unselected,
                    indicatorColor = BottomNavColors.Background
                )
            )
        }
    }
}