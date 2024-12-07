package com.example.myfridge.feature.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.myfridge.R
import com.example.myfridge.ui.theme.MintBlue
import com.example.myfridge.ui.theme.MintWhite

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddNewItemScreen(navController: NavController) {

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
                    IconButton(onClick = { navController.navigate("home") }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = topAppBarColors(
                    containerColor = MintWhite,
                    titleContentColor = MintBlue
                ),
                title = {
                    Text(
                        text = "새로운 물품 등록",
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
            verticalArrangement = Arrangement.SpaceEvenly,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Button(
                onClick = {  },
                modifier = Modifier
                    .size(200.dp)
                    .graphicsLayer(rotationZ = 15f),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonColors(
                    contentColor = MintBlue,
                    containerColor = Color.White,
                    disabledContentColor = MintBlue,
                    disabledContainerColor = Color.White
                )
            ) {
                Column(
                    verticalArrangement = Arrangement.SpaceEvenly,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.logo_food),
                        contentDescription = "image description",
                        modifier = Modifier.size(100.dp)
                    )
                    Text(
                        text = "식재료 등록하기",
                        color = MintBlue,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(4.dp)
                    .background(
                        brush = Brush.linearGradient(
                            colors = listOf(Color.Transparent, Color.White, Color.Transparent)
                        )
                    )
            )

            Button(
                onClick = { navController.navigate("essentialsRegister") },
                modifier = Modifier
                    .size(200.dp)
                    .graphicsLayer(rotationZ = -15f),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonColors(
                    contentColor = MintBlue,
                    containerColor = Color.White,
                    disabledContentColor = MintBlue,
                    disabledContainerColor = Color.White
                )
            ) {
                Column(
                    verticalArrangement = Arrangement.SpaceEvenly,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.logo_essentials),
                        contentDescription = "image description",
                        modifier = Modifier.size(100.dp)
                    )
                    Text(
                        text = "생필품 등록하기",
                        color = MintBlue,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun EssentialsSingleScreenPreview() {
    val context = LocalContext.current
    val mockNavController = remember { NavController(context) }

    AddNewItemScreen(navController = mockNavController)
}
