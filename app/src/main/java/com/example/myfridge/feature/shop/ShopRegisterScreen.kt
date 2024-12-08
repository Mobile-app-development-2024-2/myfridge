package com.example.myfridge.feature.shop

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.myfridge.R
import com.example.myfridge.ui.CustomCurvedTopAppBar
import com.example.myfridge.ui.CustomOutlinedTextField
import com.example.myfridge.ui.CustomRegisterButton
import com.example.myfridge.ui.theme.MintBlue
import com.example.myfridge.ui.theme.MintWhiteLight
import com.example.myfridge.ui.theme.fontMint
import com.google.firebase.auth.FirebaseAuth

@Composable
fun ShopRegisterScreen(navController: NavController) {
    val currentUser = FirebaseAuth.getInstance().currentUser
    val userEmail = currentUser?.email ?: ""

    val viewModel: ShopViewModel = hiltViewModel()
    LaunchedEffect(key1 = true) {
        viewModel.listenForShop(userEmail)
    }

    var name by remember { mutableStateOf("") }

    var selectedButton by remember { mutableIntStateOf(0) }

    //success/fail message
    val uiState = viewModel.state.collectAsState()
    val context = LocalContext.current
    LaunchedEffect(key1 = uiState.value) {
        when (uiState.value) {
            is ShopState.Success -> {
                Toast.makeText(context, "Register Success", Toast.LENGTH_SHORT).show()
                navController.navigate("shopRegister")
            }

            is ShopState.Error -> {
                Toast.makeText(context, "Register Failed", Toast.LENGTH_SHORT).show()
            }

            else -> {}
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = MintBlue,
        topBar = {
            CustomCurvedTopAppBar(
                titleText = stringResource(id = R.string.sRegister),
                onNavigationClick = { navController.navigate("shopList") }
            )
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
                .padding(vertical = 36.dp, horizontal = 16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.logo_shop_circle),
                contentDescription = null,
                modifier = Modifier.size(230.dp)
            )
            Spacer(modifier = Modifier.size(32.dp))
            Image(
                painter = painterResource(id = R.drawable.dotted_line),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(12.dp)
            )
            Spacer(modifier = Modifier.size(16.dp))
            Column(
                modifier = Modifier
                    .wrapContentSize()
                    .padding(16.dp)
                    .background(color = MintWhiteLight, shape = RoundedCornerShape(size = 10.dp)),
            ) {
                Column(
                    modifier = Modifier.padding(32.dp)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.text_item_name),
                        contentDescription = null,
                        modifier = Modifier.height(25.dp),
                        contentScale = ContentScale.FillHeight
                    )
                    Spacer(modifier = Modifier.size(16.dp))
                    CustomOutlinedTextField(
                        value = name,
                        onValueChange = { name = it },
                        label = stringResource(id = R.string.whatsName)
                    )
                    Spacer(modifier = Modifier.size(32.dp))
                    Image(
                        painter = painterResource(id = R.drawable.text_item_eorf),
                        contentDescription = null,
                        modifier = Modifier.height(25.dp),
                        contentScale = ContentScale.FillHeight
                    )
                    Spacer(modifier = Modifier.size(16.dp))

                    Row(
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Button(
                            onClick = { selectedButton = 1 },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (selectedButton == 1) MintBlue else Color.White
                            ),
                            modifier = Modifier
                                .border(
                                    width = 1.dp,
                                    color = Color.LightGray,
                                    shape = RoundedCornerShape(size = 10.dp)
                                )
                                .width(145.dp)
                                .height(40.dp),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Text(
                                text = "식재료",
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = 16.sp,
                                color = if (selectedButton == 1) Color.White else MintBlue
                            )
                        }
                        Button(
                            onClick = { selectedButton = 2 },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (selectedButton == 2) MintBlue else Color.White
                            ),
                            modifier = Modifier
                                .border(
                                    width = 1.dp,
                                    color = Color.LightGray,
                                    shape = RoundedCornerShape(size = 10.dp)
                                )
                                .width(145.dp)
                                .height(40.dp),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Text(
                                text = "생필품",
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = 16.sp,
                                color = if (selectedButton == 2) Color.White else MintBlue
                            )
                        }
                    }
                }
            }
            Column(
                modifier = Modifier.padding(horizontal = 32.dp)
            ) {
                CustomRegisterButton(
                    text = "장보기 등록하기",
                    onClick = {
                        viewModel.addShop(
                            userEmail, name, "", "", eorf = (selectedButton == 2)
                        )
                    },
                    enabled = name.isNotEmpty() && selectedButton != 0,
                    buttonColor = Color.White,
                    textColor = fontMint
                )
            }
        }
    }
}
