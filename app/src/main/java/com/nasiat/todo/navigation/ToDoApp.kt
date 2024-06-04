package com.nasiat.todo.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.nasiat.todo.screens.HomeScreen
import com.nasiat.todo.screens.SignIn
import com.nasiat.todo.screens.VerifyOTP


@Composable
fun ToDoApp(viewModel: NavigationViewModel = viewModel()) {

    val navController = rememberNavController()

    val user = viewModel.user.collectAsState().value

    NavHost(
        navController = navController,
        startDestination = if (user.phoneNo.isNotBlank()) {
            Route.HOME.name
        } else {
            Route.SIGN_IN.name
        }
    ) {
        composable(route = Route.SIGN_IN.name) {
            SignIn(
                viewModel = viewModel,
              navigateToOTPScreen = {
                  navigate(navController, Route.SIGN_IN.name, Route.VERIFY_OTP.name)
              }
            )
        }


        composable(route = Route.VERIFY_OTP.name) {
            VerifyOTP(
                viewModel = viewModel,
                navigateToPromptScreen = {
                    navigate(navController, Route.VERIFY_OTP.name, Route.HOME.name)
                }
            )
        }


        composable(route = Route.HOME.name) {
            HomeScreen()
        }
    }

}


private fun navigate(
    navController: NavController,
    source: String,
    destination: String
) {
    navController.navigate(route = destination) {
        popUpTo(route = source) {
            inclusive = true
        }
        launchSingleTop = true
    }

}