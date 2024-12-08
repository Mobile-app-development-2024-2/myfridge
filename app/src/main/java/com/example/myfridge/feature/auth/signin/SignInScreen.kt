package com.example.myfridge.feature.auth.signin

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
import androidx.compose.ui.tooling.preview.Preview
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
fun SignInScreen(navController: NavController) {
    val viewModel: SignInViewModel = hiltViewModel()
    val uiState = viewModel.state.collectAsState()

    var email by remember {
        mutableStateOf("")
    }
    var password by remember {
        mutableStateOf("")
    }

    val context = LocalContext.current
    LaunchedEffect(key1 = uiState.value) {
        when (uiState.value) {
            is SignInState.Success -> {
                navController.navigate("home") {
                    popUpTo("login") { inclusive = true }
                }
            }

            is SignInState.Error -> {
                Toast.makeText(context, "Sign In Failed", Toast.LENGTH_SHORT).show()
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
                    .padding(start = 21.dp, top = 72.dp, end = 21.dp, bottom = 72.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Image(
                    painter = painterResource(id = R.drawable.logo_login),
                    contentDescription = null,
                    modifier = Modifier.size(250.dp)
                )
                Spacer(modifier = Modifier.size(16.dp))
                CustomOutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = "이메일",
                    iconId = R.drawable.icon_email
                )
                Spacer(modifier = Modifier.size(8.dp))
                CustomOutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = "비밀번호",
                    isPassword = true,
                    iconId = R.drawable.icon_password
                )
                Spacer(modifier = Modifier.size(16.dp))
                if (uiState.value == SignInState.Loading) {
                    CircularProgressIndicator()
                } else {
                    CustomRegisterButton(
                        text = stringResource(id = R.string.signin),
                        onClick = { viewModel.signIn(email, password) },
                        enabled = email.isNotEmpty() && password.isNotEmpty(),
                    )
                    TextButton(
                        onClick = { navController.navigate("signup") },
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        Text(
                            text = stringResource(id = R.string.signintext),
                            color = fontMint
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun SignInScreenPreview() {
    val context = LocalContext.current
    val mockNavController = remember { NavController(context) }

    SignInScreen(navController = mockNavController)
}
