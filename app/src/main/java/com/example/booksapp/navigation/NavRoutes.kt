package com.example.booksapp.navigation

sealed class NavRoutes(val route:String){
    object Library : NavRoutes("library")
    object MyActivity : NavRoutes("activity")
    object Saved : NavRoutes("saved")
    object BookDetails : NavRoutes("bookDetails")
    object Upload : NavRoutes("upload")
}
//add book detail Screen
//add upload page screen
//add excerpts view screen
//add genre/search screen
//add menu page screen