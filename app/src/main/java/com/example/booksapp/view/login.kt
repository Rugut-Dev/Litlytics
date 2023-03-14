package com.example.booksapp.view

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.booksapp.viewModel.AuthViewModel

@Composable
fun LoginScreen(
    navController: NavController,
    authViewModel: AuthViewModel,
    onNavToLibrary: () -> Unit,
    onNavToSignUp: () -> Unit
){
    //instance of reg
    val regUiState = authViewModel?.regUiState
    val isError = regUiState?.loginError != null
    val context = LocalContext.current

    //create login page
    Box(
        modifier = Modifier
            .wrapContentHeight()
            .wrapContentWidth()
            .padding(20.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .padding(bottom = 40.dp)
                .fillMaxSize()
        ) {
            //Spacer(modifier = Modifier.height(32.dp))
            Text(
                text = "Login",
                modifier = Modifier.padding(16.dp)
            )
            if(isError){
                Text(
                    text = regUiState?.loginError.toString(),
                    modifier = Modifier.padding(16.dp),
                    color = MaterialTheme.colors.error
                )
            }

            TextField(
                value = regUiState?.userName ?: "",
                onValueChange = { authViewModel?.onUserNameChange(it) },
                label = { Text("Username or Email") },
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Filled.Person,
                        contentDescription = "Username or Email"
                    )
                },
                isError = isError
            )

            TextField(
                value = regUiState?.password ?: "",
                onValueChange = { authViewModel?.onPasswordChange(it) },
                label = { Text("Password") },
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Filled.Lock,
                        contentDescription = "Password"
                    )
                },
                isError = isError,
                visualTransformation = PasswordVisualTransformation()
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = { authViewModel?.loginUser(context) },
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
            ) {
                Text("Log In")
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxSize(),
                horizontalArrangement = Arrangement.Center
            ) {
                Text(text ="Don't have an account?")
                TextButton(
                    onClick = { onNavToSignUp.invoke() },
                    modifier = Modifier.padding(8.dp)
                ) {
                    Text(text = "Sign Up")
                }
            }
            //loading effect
            if(regUiState?.isLoading == true){
                CircularProgressIndicator()
            }
        }
    }

    //navigate to home if user is logged in
    LaunchedEffect(key1 = authViewModel.hasUser){
        if(authViewModel.hasUser){
            onNavToLibrary.invoke()
        }
    }
}
