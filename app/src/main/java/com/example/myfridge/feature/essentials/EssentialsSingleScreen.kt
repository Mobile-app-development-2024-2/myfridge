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
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.myfridge.R
import com.example.myfridge.ui.theme.DeepGreen
import com.example.myfridge.ui.theme.MintBlue
import com.example.myfridge.ui.theme.MintWhite

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EssentialsSingleScreen(navController: NavController, itemId: String) {

    val viewModel: EssentialsViewModel = hiltViewModel()
    LaunchedEffect(key1 = true) {
        viewModel.listenForSingleEssentials(itemId)
    }

    val essentialsOne by viewModel.essentialsOne.collectAsState()

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
                        text = essentialsOne.what,
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
                    .fillMaxWidth()
                    .padding(vertical = 50.dp)
            ) {

                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.End),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.logo_essentials),
                        contentDescription = "image description",
                        modifier = Modifier.size(100.dp)
                    )
                    Column (

                    ){
                        Text(
                            text = "내가 구매한 가격",
                            style = TextStyle(
                                fontSize = 32.sp,
                                lineHeight = 32.sp,
                                color = DeepGreen,
                                textAlign = TextAlign.Right,
                            )
                        )
                        Text(
                            text = essentialsOne.price + "원",
                            style = TextStyle(
                                fontSize = 32.sp,
                                lineHeight = 32.sp,
                                color = DeepGreen,
                                textAlign = TextAlign.Right,
                            )
                        )
                    }
                }

            }
            Spacer(modifier = Modifier.size(32.dp))
            Column(
                verticalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(color = MintWhite, shape = RoundedCornerShape(size = 50.dp))
            ) {
                Text(
                    text = "가성비 가격 : ",
                    modifier = Modifier
                        .padding(vertical = 16.dp)
                        .align(Alignment.CenterHorizontally),
                    color = DeepGreen,
                    fontWeight = FontWeight.Bold
                )

            }
            Spacer(modifier = Modifier.size(16.dp))
            Column(
                verticalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(color = MintWhite, shape = RoundedCornerShape(size = 50.dp))
            ) {
                Text(
                    text = "원 저렴하게 구매했습니다.",
                    modifier = Modifier
                        .padding(vertical = 16.dp)
                        .align(Alignment.CenterHorizontally),
                    color = DeepGreen,
                    fontWeight = FontWeight.Bold
                )
            }
            Spacer(modifier = Modifier.size(16.dp))
            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(10.dp)),
                colors = ButtonDefaults.buttonColors(Color.White),
                onClick = { navController.navigateUp() }
            ) {
                Text(
                    text = "이전 화면으로 돌아가기",
                    color = DeepGreen,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }

}
