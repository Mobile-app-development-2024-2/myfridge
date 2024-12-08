package com.example.myfridge.feature.home

import android.annotation.SuppressLint
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.myfridge.R
import com.example.myfridge.ui.theme.MintWhite

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
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

    var selectedButton by remember { mutableStateOf<Int?>(null) }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = MintWhite,
        contentColor = MintWhite,
        topBar = {
            Column {
                TopAppBar(
                    modifier = Modifier.height(100.dp),
                    colors = TopAppBarDefaults.topAppBarColors(MintWhite),
                    title = {
                        Image(
                            painter = painterResource(id = R.drawable.logo_myfridge),
                            contentDescription = "image description",
                            modifier = Modifier.size(100.dp)
                        )
                    },
                    actions = {
                        IconButton(onClick = { viewModel.signOut() }) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ExitToApp,
                                contentDescription = "Logout",
                                modifier = Modifier.size(50.dp)
                            )
                        }
                    }
                )
                HorizontalDivider(
                    modifier = Modifier.fillMaxWidth(),
                    thickness = 1.dp,
                    color = Color.LightGray
                )
            }
        }
    ) {
        Column(
            modifier = Modifier
                .padding(it)
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {

                ButtonGroup(
                    navController = navController,
                    selectedButton = selectedButton,
                    onClick = { selectedButton = it }
                )

            }
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.ocr_camera),
                contentDescription = null,
                modifier = Modifier
                    .size(160.dp)
                    .align(Alignment.BottomEnd)
                    .padding(16.dp)
                    .clickable {
                        navController.navigate("addNewItem")
                    }
            )
        }
    }
}

@Composable
fun ButtonGroup(
    navController: NavController,
    selectedButton: Int?,
    onClick: (Int?) -> Unit
) {
    val boxs = listOf(1, 2, 3, 4)
    val boxImages = listOf(
        R.drawable.menu_food,
        R.drawable.menu_essentials,
        R.drawable.menu_shop,
        R.drawable.menu_recipe
    )

    val animationOffsets = boxs.map { boxLabel ->
        animateDpAsState(targetValue = if (selectedButton == boxLabel) (-75).dp else (-180).dp)
    }

    boxs.forEachIndexed { index, boxLabel ->
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(165.dp)
                .offset(x = animationOffsets[index].value)
                .clickable {
                    if (selectedButton == boxLabel) {
                        when (boxLabel) {
                            1 -> navController.navigate("foodList") {
                                popUpTo("home") { inclusive = true }
                            }

                            2 -> navController.navigate("essentialsList") {
                                popUpTo("home") { inclusive = true }
                            }

                            3 -> navController.navigate("page3")
                            4 -> navController.navigate("page4")
                        }
                        onClick(null)
                    } else {
                        onClick(boxLabel)
                    }
                }
        ) {
            Image(
                painter = painterResource(id = boxImages[index]),
                contentDescription = "image description",
                modifier = Modifier
                    .fillMaxSize()
                    .padding(8.dp)
            )
        }
    }
}


@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ButtonGroupPreview() {
    val context = LocalContext.current
    val mockNavController = remember { NavController(context) }

    ButtonGroup(mockNavController, 1, onClick = {})
}
