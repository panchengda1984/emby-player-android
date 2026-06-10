package com.emby.player.ui.navigation

sealed class Screen(val route: String) {
    object Login : Screen("login")
    object Home : Screen("home")
    object Library : Screen("library/{libraryId}") {
        fun createRoute(libraryId: String) = "library/$libraryId"
    }
    object Detail : Screen("detail/{itemId}") {
        fun createRoute(itemId: String) = "detail/$itemId"
    }
    object Player : Screen("player/{itemId}") {
        fun createRoute(itemId: String) = "player/$itemId"
    }
}
