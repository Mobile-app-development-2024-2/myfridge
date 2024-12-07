package com.example.myfridge.feature.auth.signup

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.myfridge.R
import com.example.myfridge.ui.theme.MintBlue
import com.example.myfridge.ui.theme.MintWhite

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
                    .background(color = MintWhite, shape = RoundedCornerShape(size = 50.dp))
                    .padding(start = 21.dp, top = 72.dp, end = 21.dp, bottom = 72.dp),
                verticalArrangement = Arrangement.SpaceBetween,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Text(
                        text = "SIGNUP",
                        style = TextStyle(
                            fontSize = 40.sp,
                            lineHeight = 40.sp,
                            fontWeight = FontWeight(400),
                            color = MintBlue,
                        )
                    )
                    Image(
                        painter = painterResource(id = R.drawable.logo_fridge),
                        contentDescription = null,
                        modifier = Modifier.size(100.dp)
                    )
                }
                Spacer(modifier = Modifier.size(32.dp))
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text(text = "Name") }
                )
                Spacer(modifier = Modifier.size(8.dp))
                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text(text = "Email") }
                )
                Spacer(modifier = Modifier.size(8.dp))
                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text(text = "Password") },
                    visualTransformation = PasswordVisualTransformation()
                )
                Spacer(modifier = Modifier.size(8.dp))
                OutlinedTextField(
                    value = confirm,
                    onValueChange = { confirm = it },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text(text = "Confirm Password") },
                    visualTransformation = PasswordVisualTransformation(),
                    isError = password.isNotEmpty() && confirm.isNotEmpty() && confirm != password
                )
                Spacer(modifier = Modifier.size(16.dp))
                if (uiState.value == SignUpState.Loading) {
                    CircularProgressIndicator()
                } else {
                    Button(
                        onClick = { viewModel.signUp(name, email, password) },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MintBlue
                        ),
                        enabled = name.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty() && confirm.isNotEmpty() && confirm == password
                    ) {
                        Text(text = stringResource(id = R.string.signup))
                    }
                    TextButton(onClick = { }) {
                        Text(text = stringResource(id = R.string.signuptext))
                    }

                }
            }
        }
    }
}