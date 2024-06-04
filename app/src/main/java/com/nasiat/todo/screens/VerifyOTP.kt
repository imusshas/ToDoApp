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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nasiat.todo.navigation.AuthenticationUIEvent
import com.nasiat.todo.navigation.NavigationViewModel

@Composable
fun VerifyOTP(
    viewModel: NavigationViewModel,
    navigateToPromptScreen: () -> Unit
) {

    val otp = viewModel.otp.collectAsState().value
    val otpError = viewModel.otpError.collectAsState().value

    val navigationState = viewModel.navigateToPromptScreen.collectAsState().value

    Box(modifier = Modifier
        .fillMaxSize()
        .background(MaterialTheme.colorScheme.secondary)) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp, vertical = 48.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
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

            Column (
                modifier = Modifier.fillMaxWidth().weight(2f),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                OutlinedTextField(
                    value = otp,
                    onValueChange = { currentOTP ->
                        viewModel.onEvent(AuthenticationUIEvent.OTPChanged(currentOTP))
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp, 0.dp),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Done
                    ),
                    label = {
                        Text(
                            text = "OTP",
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Center
                        )
                    },
                    placeholder = {
                        Text(
                            text = "* * * * * *",
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Center
                        )
                    },
                    textStyle = TextStyle(textAlign = TextAlign.Center),
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
                    isError = otpError != null,
                    supportingText = {
                        Text(
                            text = otpError.orEmpty(),
                            color = MaterialTheme.colorScheme.onError
                        )
                    }
                )
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = {
                        viewModel.onEvent(AuthenticationUIEvent.VerifyOTPButtonClicked)
                        if (navigationState) {
                            navigateToPromptScreen()
                        }
                    },
                    shape = RoundedCornerShape(15)
                ) {
                    Text(text = "Verify")
                }
            }
        }
    }



}