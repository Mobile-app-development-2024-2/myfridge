package com.example.myfridge.feature.essentials

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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.myfridge.R
import com.example.myfridge.ui.theme.MintBlue
import com.example.myfridge.ui.theme.MintWhite
import com.google.firebase.auth.FirebaseAuth

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EssentialsRegisterScreen(navController: NavController) {
    val currentUser = FirebaseAuth.getInstance().currentUser
    val userEmail = currentUser?.email ?: ""

    val viewModel: EssentialsViewModel = hiltViewModel()
    LaunchedEffect(key1 = true) {
        viewModel.listenForEssentials(userEmail)
    }

    var what by remember { mutableStateOf("") }
    var where by remember { mutableStateOf("") }
    var price by remember { mutableStateOf("") }

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
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = topAppBarColors(
                    containerColor = MintWhite,
                    titleContentColor = MintBlue
                ),
                title = {
                    Text(
                        text = "생필품 직접 입력 페이지",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )

                }

            )
        }
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
                verticalArrangement = Arrangement.SpaceBetween,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .background(color = MintWhite, shape = RoundedCornerShape(size = 50.dp))
                    .padding(start = 20.dp, top = 50.dp, end = 20.dp, bottom = 50.dp)
            ) {

                Row(
                    horizontalArrangement = Arrangement.spacedBy(10.dp, Alignment.End),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.login_mascot),
                        contentDescription = "image description",
                        contentScale = ContentScale.FillBounds,
                        modifier = Modifier.size(100.dp)
                    )
                    Text(
                        text = "찬장 보관하기",
                        style = TextStyle(
                            fontSize = 32.sp,
                            lineHeight = 32.sp,
                            color = MintBlue,
                            textAlign = TextAlign.Right,
                        )
                    )
                }
                Spacer(modifier = Modifier.size(32.dp))
                OutlinedTextField(
                    value = what,
                    onValueChange = { what = it },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text(text = "무슨 물건인가요?") },
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        containerColor = Color.White
                    )
                )
                Spacer(modifier = Modifier.size(32.dp))
                OutlinedTextField(
                    value = where,
                    onValueChange = { where = it },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text(text = "업태가 어떻게 되나요?") },
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        containerColor = Color.White
                    )
                )
                Spacer(modifier = Modifier.size(32.dp))
                OutlinedTextField(
                    value = price,
                    onValueChange = { price = it },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text(text = "가격이 얼마인가요?") },
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        containerColor = Color.White
                    )
                )
                Spacer(modifier = Modifier.size(32.dp))
                Button(
                    onClick = {
                        viewModel.addEssentials(userEmail, what, where, price)
                        navController.navigate("home")
                    },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = what.isNotEmpty() && where.isNotEmpty() && price.isNotEmpty()
                ) {
                    Text(text = stringResource(id = R.string.register))
                }


            }

        }
    }
}
