package com.healthmonitor.app

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.rememberNavController
import com.healthmonitor.app.auth.AuthManager
import com.healthmonitor.app.navigation.NavigationGraph

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AuthManager.initGoogleSignIn(this)

        setContent {
            val navController = rememberNavController()
            NavigationGraph(navController)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 9001) {
            AuthManager.handleSignInResult(data) { success ->
                // No direct UI updates here; Navigation is handled in LoginScreen
            }
        }
    }
}
