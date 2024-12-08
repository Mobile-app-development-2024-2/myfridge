package com.example.myfridge.feature.food

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.myfridge.R
import com.example.myfridge.feature.model.Food
import com.example.myfridge.ui.CustomOutlinedTextField
import com.example.myfridge.ui.theme.DeepGreen
import com.example.myfridge.ui.theme.MintWhite
import com.example.myfridge.ui.theme.fontMint
import com.google.firebase.auth.FirebaseAuth

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FoodListScreen(navController: NavController) {

    val currentUser = FirebaseAuth.getInstance().currentUser
    val userEmail = currentUser?.email ?: ""

    val viewModel: FoodViewModel = hiltViewModel()
    LaunchedEffect(key1 = true) {
        viewModel.listenForFood(userEmail)
    }

    val foodList by viewModel.foodList.collectAsState(emptyList())
    var searchWhat by remember { mutableStateOf("") }

    val filteredList = if (searchWhat.isEmpty()) {
        foodList
    } else {
        foodList.filter { it.name.contains(searchWhat, ignoreCase = true) }
    }

    var sortedFoodList by remember { mutableStateOf(emptyList<Food>()) }
    LaunchedEffect(filteredList) {
        sortedFoodList = filteredList
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = MintWhite,
        topBar = {
            CenterAlignedTopAppBar(
                navigationIcon = {
                    IconButton(onClick = { navController.navigate("home") }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                title = {
                    Text(
                        text = stringResource(id = R.string.fList),
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
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                CustomOutlinedTextField(
                    value = searchWhat,
                    onValueChange = { searchWhat = it },
                    label = stringResource(id = R.string.search),
                    fieldColor = Color.White
                )
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = R.drawable.button_sortedby),
                    contentDescription = null,
                    modifier = Modifier
                        .height(30.dp)
                        .clickable { sortedFoodList = filteredList.sortedBy { it.expDate } },
                    contentScale = ContentScale.FillHeight
                )
            }
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(sortedFoodList) { item ->
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(80.dp)
                            .clickable {
                                // 단일 페이지
                            },
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.logo_food),
                                contentDescription = null,
                                modifier = Modifier
                                    .fillMaxHeight()
                                    .padding(vertical = 5.dp, horizontal = 10.dp)
                                    .align(Alignment.CenterVertically)
                            )
                            Column(
                                modifier = Modifier.align(Alignment.CenterVertically)
                            ) {
                                Text(
                                    text = item.name,
                                    color = DeepGreen,
                                    fontWeight = FontWeight.Bold
                                )
                                Image(
                                    painter = painterResource(id = R.drawable.mini_dotted_line),
                                    contentDescription = null
                                )
                                Text(
                                    text = "유통기한: " + item.expDate,
                                    color = DeepGreen
                                )
                            }
                            Spacer(modifier = Modifier.weight(1f))
                            Image(
                                painter = painterResource(id = R.drawable.pointer),
                                contentDescription = null,
                                modifier = Modifier
                                    .size(30.dp)
                                    .padding(16.dp)
                            )
                        }
                        Image(
                            painter = painterResource(id = R.drawable.dotted_line2),
                            contentDescription = null,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(10.dp)
                                .padding(1.dp)
                        )

                    }
                }
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun FoodListScreenPreview() {
    val context = LocalContext.current
    val mockNavController = remember { NavController(context) }

    FoodListScreen(navController = mockNavController)
}
