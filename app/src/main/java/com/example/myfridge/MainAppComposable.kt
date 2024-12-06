package com.example.myfridge

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.myfridge.feature.auth.signin.SignInScreen
import com.example.myfridge.feature.auth.signup.SignUpScreen
import com.example.myfridge.feature.essentials.EssentialsListScreen
import com.example.myfridge.feature.essentials.EssentialsRegisterScreen
import com.example.myfridge.feature.essentials.EssentialsSingleScreen
import com.example.myfridge.feature.home.HomeScreen
import com.google.firebase.auth.FirebaseAuth

@Composable
fun MainApp() {
    Surface(modifier = Modifier.fillMaxSize()) {
        val navController = rememberNavController()
        val currentUser = FirebaseAuth.getInstance().currentUser
        val start = if (currentUser != null) "home" else "login"

        NavHost(navController = navController, startDestination = start) {
            composable(route = "login") {
                SignInScreen(navController = navController)
            }
            composable(route = "signup") {
                SignUpScreen(navController = navController)
            }
            composable(route = "home") {
                HomeScreen(navController = navController)
            }

            composable(route = "essentialsRegister") {
                EssentialsRegisterScreen(navController = navController)
            }
            composable(route = "essentialsList") {
                EssentialsListScreen(navController = navController)
            }
            composable(route = "essentialsSingle?value={value}") { v ->
                val value = v.arguments?.getString("value")
                if (value != null) {
                    EssentialsSingleScreen(navController = navController, itemId = value)
                }
            }
        }
    }
}