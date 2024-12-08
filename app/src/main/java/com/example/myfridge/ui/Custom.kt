package com.example.myfridge.ui

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
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
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myfridge.R
import com.example.myfridge.ui.theme.MintBlue
import com.example.myfridge.ui.theme.MintWhite
import com.example.myfridge.ui.theme.fontMint

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomOutlinedTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    isPassword: Boolean = false,
    iconId: Int? = null,
    fieldColor: Color = MintWhite
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp)),
        label = { Text(text = label) },
        leadingIcon = if (iconId != null) {
            {
                Icon(
                    painter = painterResource(id = iconId),
                    contentDescription = null,
                    modifier = Modifier.size(24.dp),
                    tint = fontMint
                )
            }
        } else null,
        colors = TextFieldDefaults.outlinedTextFieldColors(
            containerColor = fieldColor,
            disabledBorderColor = Color.Transparent,
            focusedBorderColor = Color.Transparent,
            unfocusedBorderColor = Color.Transparent,
            unfocusedLabelColor = fontMint,
            focusedLabelColor = fontMint
        ),
        visualTransformation = if (isPassword) PasswordVisualTransformation() else VisualTransformation.None
    )
}

@Composable
fun CustomRegisterButton(
    text: String,
    onClick: () -> Unit,
    enabled: Boolean,
    buttonColor: Color = MintBlue,
    textColor: Color = Color.White
) {
    Button(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        colors = ButtonDefaults.buttonColors(containerColor = buttonColor),
        shape = RoundedCornerShape(10.dp),
        enabled = enabled
    ) {
        Icon(
            painter = painterResource(id = R.drawable.icon_register),
            contentDescription = null,
            modifier = Modifier.size(24.dp),
            tint = textColor
        )
        Text(
            text = text,
            fontWeight = FontWeight.ExtraBold,
            color = textColor,
            fontSize = 16.sp
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomCurvedTopAppBar(
    titleText: String,
    onNavigationClick: () -> Unit
) {
    CenterAlignedTopAppBar( //글자 가운데
        modifier = Modifier.clip(
            RoundedCornerShape(
                bottomStart = 16.dp,
                bottomEnd = 16.dp
            )
        ),
        navigationIcon = {
            IconButton(onClick = onNavigationClick) {
                Icon(
                    Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back"
                )
            }
        },
        colors = topAppBarColors(
            containerColor = MintWhite,
            titleContentColor = fontMint
        ),
        title = {
            Text(
                text = titleText,
                style = MaterialTheme.typography.titleMedium,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
        }
    )
}
