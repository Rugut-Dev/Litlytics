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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.booksapp.viewModel.AuthViewModel

@Composable
fun SignUpScreen(
    navController: NavController,
    authViewModel: AuthViewModel,
    onNavToLibrary: () -> Unit,
    onNavToLogin: () -> Unit
){
    //instance of reg
    val regUiState = authViewModel?.regUiState
    val isError = regUiState?.signUpError != null
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
                text = "Sign Up",
                modifier = Modifier.padding(16.dp)
            )
            if(isError){
                Text(
                    text = regUiState?.signUpError.toString(),
                    modifier = Modifier.padding(16.dp),
                    color = MaterialTheme.colors.error
                )
            }

            TextField(
                value = regUiState?.userNameSignUp ?: "",
                onValueChange = { authViewModel?.onUserNameSignUpChange(it) },
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
                value = regUiState?.passwordSignUp ?: "",
                onValueChange = { authViewModel?.onPasswordSignUpChange(it) },
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

            TextField(
                value = regUiState?.confirmPassword?: "",
                onValueChange = { authViewModel?.onConfirmPasswordChange(it) },
                label = { Text("Confirm Password") },
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
                onClick = { authViewModel?.createUser(context) },
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
            ) {
                Text("Sign Up")
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxSize(),
                horizontalArrangement = Arrangement.Center
            ) {
                Text(text ="Already have an account?")
                // Spacer(modifier = Modifier.size(8.dp)
                TextButton(
                    onClick = { onNavToLogin.invoke() },
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(text = "Sign In")
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