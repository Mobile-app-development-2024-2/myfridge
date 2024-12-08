package com.example.myfridge.feature.food

import android.app.DatePickerDialog
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.myfridge.R
import com.example.myfridge.ui.CustomCurvedTopAppBar
import com.example.myfridge.ui.CustomOutlinedTextField
import com.example.myfridge.ui.CustomRegisterButton
import com.example.myfridge.ui.theme.MintBlue
import com.google.firebase.auth.FirebaseAuth
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@Composable
fun FoodRegisterScreen(navController: NavController) {
    val currentUser = FirebaseAuth.getInstance().currentUser
    val userEmail = currentUser?.email ?: ""

    val viewModel: FoodViewModel = hiltViewModel()
    LaunchedEffect(key1 = true) {
        viewModel.listenForFood(userEmail)
    }

    var name by remember { mutableStateOf("") }
    var place by remember { mutableStateOf("") }
    var price by remember { mutableStateOf("") }

    //expDate
    var expDate by remember { mutableStateOf("") }
    val calendar = Calendar.getInstance()
    var showCalendar by remember { mutableStateOf(false) }

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
            CustomCurvedTopAppBar(
                titleText = stringResource(id = R.string.fRegister),
                onNavigationClick = { navController.navigate("addNewItem") }
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
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.register_in_fridge),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(100.dp)
                )
                Spacer(modifier = Modifier.size(32.dp))
                CustomOutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = stringResource(id = R.string.whatsName)
                )
                Spacer(modifier = Modifier.size(32.dp))
                Row(
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Button(
                        onClick = { showCalendar = true },
                        modifier = Modifier
                            .size(30.dp),
                        contentPadding = PaddingValues(0.dp)
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.icon_calendar),
                            contentDescription = null,
                            modifier = Modifier
                                .fillMaxSize()
                                .background(Color.White)
                        )
                    }
                    Spacer(modifier = Modifier.padding(horizontal = 4.dp))
                    CustomOutlinedTextField(
                        value = expDate,
                        onValueChange = { expDate = it },
                        label = stringResource(id = R.string.checkExpDate)
                    )
                }
                if (showCalendar) {
                    val datePicker = DatePickerDialog(
                        context,
                        { _, year, month, dayOfMonth ->
                            val formattedDate =
                                SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                                    .format(Calendar.getInstance().apply {
                                        set(year, month, dayOfMonth)
                                    }.time)
                            expDate = formattedDate
                            showCalendar = false
                        },
                        calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH)
                    )
                    datePicker.show()
                    showCalendar = false //달력 여러번 보여줌
                }


                Spacer(modifier = Modifier.size(32.dp))
                CustomOutlinedTextField(
                    value = place,
                    onValueChange = { place = it },
                    label = stringResource(id = R.string.wheresPlace)
                )
                Spacer(modifier = Modifier.size(32.dp))
                CustomOutlinedTextField(
                    value = price,
                    onValueChange = { price = it },
                    label = stringResource(id = R.string.howsPrice)
                )
                Spacer(modifier = Modifier.size(32.dp))
                CustomRegisterButton(
                    text = stringResource(id = R.string.register),
                    onClick = {
                        viewModel.addFood(
                            userEmail, name, expDate, place, price
                        )
                    },
                    enabled = true
                )
            }
        }
    }
}
