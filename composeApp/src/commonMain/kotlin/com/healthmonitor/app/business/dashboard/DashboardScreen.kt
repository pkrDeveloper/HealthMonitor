package com.healthmonitor.app.business.dashboard

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.healthmonitor.app.auth.AuthManager

@Composable
fun DashboardScreen(navController: NavController) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            IconButton(onClick = { /* Navigate to Profile */ }) {
                Icon(Icons.Default.AccountCircle, contentDescription = "Profile")
            }
            IconButton(onClick = {
                AuthManager.signOut()
                navController.navigate("login") {
                    popUpTo("dashboard") { inclusive = true }
                }
            }) {
                Icon(Icons.Default.ExitToApp, contentDescription = "Logout")
            }
        }

        Spacer(modifier = Modifier.height(20.dp))
        Button(onClick = { navController.navigate("camera") }) {
            Text("Open Camera")
        }
    }
}
