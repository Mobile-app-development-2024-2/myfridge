package com.example.myfridge.feature.food

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.myfridge.R
import com.example.myfridge.ui.theme.MintBlue
import com.example.myfridge.ui.theme.MintWhite
import com.example.myfridge.ui.theme.fontMint
import com.google.firebase.auth.FirebaseAuth

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FoodRegisterScreen(navController: NavController) {
    val currentUser = FirebaseAuth.getInstance().currentUser
    val userEmail = currentUser?.email ?: ""

    val viewModel: FoodViewModel = hiltViewModel()
    LaunchedEffect(key1 = true) {
        viewModel.listenForFood(userEmail)
    }

    var fname by remember { mutableStateOf("") }
    var expDate by remember { mutableStateOf("") }
    var fplace by remember { mutableStateOf("") }
    var fprice by remember { mutableStateOf("") }

    //success/fail message
    val uiState = viewModel.state.collectAsState()
    val context = LocalContext.current
    LaunchedEffect(key1 = uiState.value) {
        when (uiState.value) {
            is FoodState.Success -> {
                Toast.makeText(context, "Register Success", Toast.LENGTH_SHORT).show()
                navController.navigate("foodRegister")
            }

            is FoodState.Error -> {
                Toast.makeText(context, "Register Failed", Toast.LENGTH_SHORT).show()
            }

            else -> {}
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = MintBlue,
        topBar = {
            TopAppBar(
                modifier = Modifier.clip(
                    RoundedCornerShape(
                        bottomStart = 16.dp,
                        bottomEnd = 16.dp
                    )
                ),
                navigationIcon = {
                    IconButton(onClick = { navController.navigate("addNewItem") }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                title = {
                    Text(
                        text = stringResource(id = R.string.fRegister),
                        color = fontMint,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                },
                colors = topAppBarColors(MintWhite)

            )
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
                .padding(horizontal = 16.dp, vertical = 48.dp)
                .background(color = Color.White, shape = RoundedCornerShape(size = 50.dp)),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.size(24.dp))
            Image(
                painter = painterResource(id = R.drawable.register_in_fridge),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp)
            )
            Spacer(modifier = Modifier.size(48.dp))

            TextField(
                value = fname,
                onValueChange = { fname = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .clip(RoundedCornerShape(20.dp)),
                label = { Text(text = stringResource(id = R.string.whatsName)) },
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    containerColor = MintWhite,
                    disabledBorderColor = Color.Transparent,
                    focusedBorderColor = Color.Transparent,
                    unfocusedBorderColor = Color.Transparent,
                    unfocusedLabelColor = fontMint,
                    focusedLabelColor = fontMint
                )
            )
            Spacer(modifier = Modifier.size(32.dp))
            TextField(
                value = expDate,
                onValueChange = { expDate = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .clip(RoundedCornerShape(20.dp)),
                label = { Text(text = stringResource(id = R.string.checkExpDate)) },
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    containerColor = MintWhite,
                    disabledBorderColor = Color.Transparent,
                    focusedBorderColor = Color.Transparent,
                    unfocusedBorderColor = Color.Transparent,
                    unfocusedLabelColor = fontMint,
                    focusedLabelColor = fontMint
                )
            )
            Spacer(modifier = Modifier.size(32.dp))
            TextField(
                value = fplace,
                onValueChange = { fplace = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .clip(RoundedCornerShape(20.dp)),
                label = { Text(text = stringResource(id = R.string.wheresPlace)) },
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    containerColor = MintWhite,
                    disabledBorderColor = Color.Transparent,
                    focusedBorderColor = Color.Transparent,
                    unfocusedBorderColor = Color.Transparent,
                    unfocusedLabelColor = fontMint,
                    focusedLabelColor = fontMint
                )
            )
            Spacer(modifier = Modifier.size(32.dp))
            TextField(
                value = fprice,
                onValueChange = { fprice = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .clip(RoundedCornerShape(20.dp)),
                label = { Text(text = stringResource(id = R.string.howsPrice)) },
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    containerColor = MintWhite,
                    disabledBorderColor = Color.Transparent,
                    focusedBorderColor = Color.Transparent,
                    unfocusedBorderColor = Color.Transparent,
                    unfocusedLabelColor = fontMint,
                    focusedLabelColor = fontMint
                )
            )
            Spacer(modifier = Modifier.size(32.dp))
            Image(
                painter = painterResource(id = R.drawable.button_register),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
                    .padding(horizontal = 16.dp)
                    .clickable {
                        viewModel.addFood(userEmail, fname, expDate, fplace, fprice)
                    }
            )

        }
    }
}
