package com.example.myfridge.feature.food

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ColorMatrix
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.HorizontalAlignmentLine
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
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
    var selectedFood by remember { mutableStateOf<Food?>(null) }
    var isDetailVisible by remember { mutableStateOf(false) }

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
                                selectedFood = item
                                isDetailVisible = true
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

        if (isDetailVisible && selectedFood != null) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .clickable { isDetailVisible = false },
                contentAlignment = Alignment.CenterEnd
            ) {
                Column(
                    modifier = Modifier
                        .width(300.dp)
                        .height(600.dp)
                        .background(Color.White)
                        .padding(16.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(onClick = { isDetailVisible = false}) {
                            Icon(
                                painter = painterResource(id = R.drawable.button_delete),
                                contentDescription = "Close"
                            )
                        }
                        Image(
                            painter = painterResource(id = R.drawable.logo_food),
                            contentDescription = null,
                            modifier = Modifier
                                .padding(vertical = 5.dp, horizontal = 10.dp)
                                .align(Alignment.CenterVertically)
                        )
                    }
                    Text(
                        text = selectedFood!!.name + "\n가성비 알아보기",
                        style = MaterialTheme.typography.titleLarge,
                        textAlign = TextAlign.End,
                        modifier = Modifier.fillMaxWidth(),
                        fontWeight = FontWeight.Bold,
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Image(
                        painter = painterResource(id = R.drawable.dotted_line2),
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(10.dp),
                        colorFilter = ColorFilter.colorMatrix(ColorMatrix().apply {
                            setToSaturation(0f)
                        })
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                            .border(
                                width = 4.dp,
                                color = Color(0xFF01D1C4),
                                shape = RoundedCornerShape(16.dp)
                            )
                            .clip(RoundedCornerShape(16.dp))
                            .background(Color.White),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "0원",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(top = 12.dp, bottom = 4.dp)
                        )
                        Text(
                            text = "저렴하게 구매했습니다",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(top = 4.dp, bottom = 12.dp)
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Image(
                        painter = painterResource(id = R.drawable.dotted_line2),
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(10.dp),
                        colorFilter = ColorFilter.colorMatrix(ColorMatrix().apply {
                            setToSaturation(0f)
                        })
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "내가 구매한 가격",
                        style = MaterialTheme.typography.titleMedium,
                        textAlign = TextAlign.End,
                        modifier = Modifier.fillMaxWidth().padding(4.dp),
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "${selectedFood!!.price}원",
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(color = MintWhite)
                            .padding(8.dp),
                        textAlign = TextAlign.End
                    )
                    Text(
                        text = "vs",
                        style = MaterialTheme.typography.titleLarge,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Text(
                        text = "가성비 가격",
                        style = MaterialTheme.typography.titleMedium,
                        textAlign = TextAlign.End,
                        modifier = Modifier.fillMaxWidth().padding(4.dp),
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "0원",
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(color = MintWhite)
                            .padding(8.dp),
                        textAlign = TextAlign.End
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        onClick = {
                            // sortedFoodList = sortedFoodList.filter { it != selectedFood }
                            // todo : viewModel을 통해 삭제
                            isDetailVisible = false
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.White,
                            contentColor = Color.Red
                        ),
                        border = BorderStroke(2.dp, Color.Red)
                    ) {
                        Text(text = "항목 삭제", color = Color.Red)
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Button(
                        onClick = { /** todo */ },
                        modifier = Modifier
                            .fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF01D1C4),
                            contentColor = Color.White
                        ),
                        shape = RoundedCornerShape(4.dp)
                    ) {
                        Text(text = "장보기로 이동")
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
