package com.healthmonitor.app.auth

import android.app.Activity
import android.content.Intent
import android.util.Log
import com.google.android.gms.auth.api.signin.*
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider

object AuthManager {
    private lateinit var googleSignInClient: GoogleSignInClient
    private val firebaseAuth = FirebaseAuth.getInstance()

    fun initGoogleSignIn(activity: Activity) {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken("603009835331-kcda4loiq1v6rk4mfabu0b6t8eit77nd.apps.googleusercontent.com")
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(activity, gso)
    }

    fun signInIntent(activity: Activity): Intent {
        return googleSignInClient.signInIntent
    }

    fun handleSignInResult(data: Intent?, onResult: (Boolean) -> Unit) {
        val task = GoogleSignIn.getSignedInAccountFromIntent(data)
        try {
            val account = task.getResult(ApiException::class.java)!!
            firebaseAuthWithGoogle(account, onResult)
        } catch (e: ApiException) {
            Log.e("AuthManager", "Google sign-in failed", e)
            onResult(false)
        }
    }

    private fun firebaseAuthWithGoogle(account: GoogleSignInAccount, onResult: (Boolean) -> Unit) {
        val credential = GoogleAuthProvider.getCredential(account.idToken, null)
        firebaseAuth.signInWithCredential(credential)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d("AuthManager", "Firebase sign-in successful: ${firebaseAuth.currentUser?.email}")
                    onResult(true)
                } else {
                    Log.e("AuthManager", "Firebase sign-in failed", task.exception)
                    onResult(false)
                }
            }
    }

    fun signOut() {
        googleSignInClient.signOut()
        firebaseAuth.signOut()
    }
}
