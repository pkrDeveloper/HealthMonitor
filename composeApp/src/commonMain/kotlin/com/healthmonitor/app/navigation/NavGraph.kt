package com.healthmonitor.app.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.healthmonitor.app.business.CameraScreen
import com.healthmonitor.app.business.dashboard.DashboardScreen
import com.healthmonitor.app.business.homescreen.LoginScreen

@Composable
fun NavigationGraph(navController: NavHostController) {
    NavHost(navController = navController, startDestination = "login") {
        composable("login") { LoginScreen(navController) }
        composable("dashboard") { DashboardScreen(navController) }
        composable("camera") { CameraScreen(navController) }
    }
}
