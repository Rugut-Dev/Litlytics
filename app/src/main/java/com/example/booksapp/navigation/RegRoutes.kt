package com.example.booksapp.navigation

sealed class RegRoutes(val route:String) {
    object Login : NavRoutes("login")
    object SignUp : NavRoutes("signUp")
}
