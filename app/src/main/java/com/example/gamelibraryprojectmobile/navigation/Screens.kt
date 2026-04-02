package com.example.gamelibrary.navigation

sealed class Screen(val route: String) {
    object Home : Screen("Home")
    object Library : Screen("Library")
    object Profile : Screen("Profile")
    object Detail : Screen("detail/{gameId}") {
        fun createRoute(id: Int) = "detail/$id"
    }
    object Search : Screen("search")
}