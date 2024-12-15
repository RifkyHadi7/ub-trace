package com.example.ubtrace.presentation.LoginScreen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.ubtrace.domain.auth.AuthState
import com.example.ubtrace.domain.auth.AuthViewModel
import com.example.ubtrace.presentation.LoginScreen.Components.Loading
import com.example.ubtrace.presentation.LoginScreen.Components.Logincard
import com.example.ubtrace.presentation.LoginScreen.Components.topAppbarLogSign

@Composable
internal fun LoginScreen(
    navController: NavController,
    viewModel: AuthViewModel = AuthViewModel(),
    onLoginSuccess: () -> Unit
) {
    val state by viewModel.authState.collectAsState()

    when (state) {
        is AuthState.Idle -> {
            LoginContent(
                state = LoginViewState(),
                onLogin = { email, password ->
                    viewModel.login(email, password)
                },
                onBackClick = { navController.popBackStack() } // Tambahkan aksi kembali
            )
        }
        is AuthState.Loading -> {
            Loading(isLoading = true)
        }
        is AuthState.Success -> {
            onLoginSuccess()
            navController.navigate("home")
        }
        is AuthState.Error -> {
            val errorMessage = (state as AuthState.Error).errorMessage
            LoginContent(
                state = LoginViewState(isError = true, errorMessage = errorMessage),
                onLogin = { email, password ->
                    viewModel.login(email, password)
                },
                onBackClick = { navController.popBackStack() } // Tambahkan aksi kembali
            )
        }
    }
}


@Composable
fun LoginContent(
    state: LoginViewState,
    onLogin: (String, String)-> Unit,
    onBackClick: () -> Unit
){
    Scaffold (
        containerColor = Color(0xFFFFDE59),
        modifier = Modifier.fillMaxSize(),
        topBar = {
            topAppbarLogSign(onBackClick = onBackClick)
        }, content = { paddingValues ->
            Column (
                modifier = Modifier.padding(paddingValues)
                    .fillMaxWidth()
            ){
                Logincard(
                    modifier = Modifier,
                    onLogin = onLogin
                )
            }
        }
    )
}

@Composable
@Preview
fun preview(){
    val navController = rememberNavController()
    LoginContent(
        state = LoginViewState(),
        onLogin = TODO(),
        onBackClick = TODO()
    )
}
