package com.example.myfridge.feature.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
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
                        text = stringResource(id = R.string.registerNewItem),
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
                .padding(it),
            verticalArrangement = Arrangement.SpaceEvenly,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Image(
                painter = painterResource(id = R.drawable.register_food),
                contentDescription = null,
                modifier = Modifier
                    .size(250.dp)
                    .clickable {
                        navController.navigate("foodRegister")
                    }
            )
            Image(
                painter = painterResource(id = R.drawable.dotted_line),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(11.dp)
            )
            Image(
                painter = painterResource(id = R.drawable.register_essentials),
                contentDescription = null,
                modifier = Modifier
                    .size(250.dp)
                    .clickable {
                        navController.navigate("essentialsRegister")
                    }
            )
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
