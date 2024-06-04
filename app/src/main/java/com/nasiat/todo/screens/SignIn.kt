package com.nasiat.todo.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nasiat.todo.navigation.AuthenticationUIEvent
import com.nasiat.todo.navigation.NavigationViewModel

@Composable
fun SignIn(
    viewModel: NavigationViewModel,
    navigateToOTPScreen: () -> Unit
) {
    val phoneNo = viewModel.phoneNo.collectAsState().value
    val error = viewModel.error.collectAsState().value
    val navigationState = viewModel.navigateToOTPScreen.collectAsState().value

    Box(modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.secondary)) {
        Column(
            modifier = Modifier
                .fillMaxSize()

                .padding(vertical = 48.dp, horizontal = 24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(text = "DO", fontWeight = FontWeight.Black, fontSize = 72.sp, color = Color.White)
                Text(text = "What ToDo", fontWeight = FontWeight.Light, fontSize = 16.sp, color = Color.White)
            }


            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(2f),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                OutlinedTextField(
                    value = phoneNo,
                    onValueChange = { phone ->
                        viewModel.onEvent(AuthenticationUIEvent.PhoneNoChanged(phone))
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(0.dp),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Done
                    ),
                    label = { Text(text = "Phone Number") },
                    placeholder = { Text(text = "Enter your phone no") },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color.White,
                        focusedLabelColor = Color.White,
                        focusedTextColor = Color.White,
                        focusedPlaceholderColor = Color.White,
                        unfocusedLabelColor = Color.White,
                        unfocusedTextColor = Color.White,
                        unfocusedPlaceholderColor = Color.White,
                        errorTextColor = Color.White,
                        cursorColor = Color.White,
                        errorLabelColor = Color.White,
                        errorCursorColor = Color.White
                    ),
                    isError = error != null,
                    supportingText = {
                        Text(
                            text = error.orEmpty(),
                            color = MaterialTheme.colorScheme.onError
                        )
                    }
                )

                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = {
                        viewModel.onEvent(AuthenticationUIEvent.SignUpButtonClicked)
                        if (navigationState) {
                            navigateToOTPScreen()
                        }
                    },
                    shape = RoundedCornerShape(15)
                ) {
                    Text(text = "Sign In")
                }
            }
        }
    }


}