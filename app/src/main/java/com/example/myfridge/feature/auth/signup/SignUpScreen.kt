package com.example.myfridge.feature.auth.signup

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.myfridge.R
import com.example.myfridge.ui.CustomOutlinedTextField
import com.example.myfridge.ui.CustomRegisterButton
import com.example.myfridge.ui.theme.MintBlue
import com.example.myfridge.ui.theme.MintWhiteLight
import com.example.myfridge.ui.theme.fontMint

@Composable
fun SignUpScreen(navController: NavController) {
    val viewModel: SignUpViewModel = hiltViewModel()
    val uiState = viewModel.state.collectAsState()

    var name by remember {
        mutableStateOf("")
    }
    var email by remember {
        mutableStateOf("")
    }
    var password by remember {
        mutableStateOf("")
    }
    var confirm by remember {
        mutableStateOf("")
    }

    val context = LocalContext.current
    LaunchedEffect(key1 = uiState.value) {
        when (uiState.value) {
            is SignUpState.Success -> {
                navController.navigate("home") {
                    popUpTo("login") { inclusive = true }
                    popUpTo("signup") { inclusive = true }
                }
            }

            is SignUpState.Error -> {
                Toast.makeText(context, "Sign Up Failed", Toast.LENGTH_SHORT).show()
            }

            else -> {}
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = MintBlue
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Column(
                modifier = Modifier
                    .background(color = MintWhiteLight, shape = RoundedCornerShape(size = 50.dp))
                    .padding(start = 21.dp, top = 50.dp, end = 21.dp, bottom = 50.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Image(
                    painter = painterResource(id = R.drawable.logo_join),
                    contentDescription = null,
                    modifier = Modifier.size(250.dp)
                )
                Spacer(modifier = Modifier.size(16.dp))
                CustomOutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = "이름"
                )
                Spacer(modifier = Modifier.size(8.dp))
                CustomOutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = "이메일"
                )
                Spacer(modifier = Modifier.size(8.dp))
                CustomOutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = "비밀번호",
                    isPassword = true
                )
                Spacer(modifier = Modifier.size(8.dp))
                CustomOutlinedTextField(
                    value = confirm,
                    onValueChange = { confirm = it },
                    label = "비밀번호 확인",
                    isPassword = true
                )
                Spacer(modifier = Modifier.size(16.dp))
                if (uiState.value == SignUpState.Loading) {
                    CircularProgressIndicator()
                } else {
                    CustomRegisterButton(
                        text = stringResource(id = R.string.signup),
                        onClick = { viewModel.signUp(name, email, password) },
                        enabled = name.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty() && confirm.isNotEmpty() && confirm == password
                    )
                    TextButton(
                        onClick = { navController.navigateUp() },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = stringResource(id = R.string.signuptext),
                            color = fontMint
                        )
                    }

                }
            }
        }
    }
}