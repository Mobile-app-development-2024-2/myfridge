package com.example.myfridge

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.myfridge.feature.auth.signin.SignInScreen
import com.example.myfridge.feature.auth.signup.SignUpScreen
import com.google.firebase.auth.FirebaseAuth

@Composable
fun MainApp() {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color(0xFF01D1C4)
    ) {
        val navController = rememberNavController()
        val currentUser = FirebaseAuth.getInstance().currentUser
        val start = if (currentUser != null) "home" else "signin"

        NavHost(navController = navController, startDestination = "signin") {
            composable(route = "signin") {
                 SignInScreen(navController = navController)
            }
            composable(route = "signup") {
                 SignUpScreen(navController = navController)
            }
//            composable(route = "home") {
//                 HomeScreen(navController = navController)
//            }
//            composable(route = "chat/{channelID}&{channelName}", arguments = listOf(
//                navArgument("channelID") {
//                    type = NavType.StringType
//                },
//                navArgument("channelName") {
//                    type = NavType.StringType
//                }
//            )) {
//                val channelID = it.arguments?.getString("channelID") ?: ""
//                val channelName = it.arguments?.getString("channelName") ?: ""
//                ChatScreen(navController = navController, channelID = channelID, channelName = channelName)
//            }
        }
    }
}