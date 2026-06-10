package com.emby.player.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.emby.player.ui.screen.detail.DetailScreen
import com.emby.player.ui.screen.home.HomeScreen
import com.emby.player.ui.screen.login.LoginScreen
import com.emby.player.ui.screen.player.PlayerScreen

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Screen.Login.route
    ) {
        composable(Screen.Login.route) {
            LoginScreen(
                onLoginSuccess = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                }
            )
        }

        composable(Screen.Home.route) {
            HomeScreen(
                onItemClick = { itemId ->
                    navController.navigate(Screen.Detail.createRoute(itemId))
                }
            )
        }

        composable(
            route = Screen.Detail.route,
            arguments = listOf(navArgument("itemId") { type = NavType.StringType })
        ) {
            DetailScreen(
                onPlayClick = { itemId ->
                    navController.navigate(Screen.Player.createRoute(itemId))
                },
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }

        composable(
            route = Screen.Player.route,
            arguments = listOf(navArgument("itemId") { type = NavType.StringType })
        ) {
            PlayerScreen(
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }
    }
}
