package com.example.myfridge.feature.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController

@Composable
fun HomeScreen(navController: NavController) {
    val viewModel = hiltViewModel<HomeViewModel>()
    val uiState = viewModel.state.collectAsState()

    LaunchedEffect(key1 = uiState.value) {
        if (uiState.value == SignOutState.LoggedOut) {
            navController.navigate("login") {
                popUpTo("home") { inclusive = true }
            }
        }

    }

    Column (
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ){
        Text(text = "home page")
        IconButton(onClick = { viewModel.signOut() }) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ExitToApp,
                contentDescription = null,
                tint = Color.Gray
            )
        }

        Button(
            onClick = {
                navController.navigate("essentialsRegister") {
                    popUpTo("home") { inclusive = true }
                }
            }
        ) {
            Text("register essentials")
        }
        Button(
            onClick = {
                navController.navigate("essentialsList") {
                    popUpTo("home") { inclusive = true }
                }
            }
        ) {
            Text("essentials list")
        }
    }
}