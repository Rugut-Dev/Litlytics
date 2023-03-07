package com.example.booksapp.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.HiltViewModelFactory
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.booksapp.view.BookDetails
import com.example.booksapp.view.Library
import com.example.booksapp.viewModel.MainViewModel
import kotlinx.serialization.ExperimentalSerializationApi

object EndPoints {
    const val ID = "id"
}
@OptIn(ExperimentalSerializationApi::class)
@ExperimentalComposeUiApi
@Composable
fun NavGraph() {
    val navController = rememberNavController()
    val actions = remember(navController) { MainActions(navController) }
    val context = LocalContext.current

    NavHost(navController, startDestination = NavRoutes.Library.route) {
        // Library
        composable(NavRoutes.Library.route) {
            val viewModel: MainViewModel = viewModel(
                factory = HiltViewModelFactory(LocalContext.current, it)
            )
            viewModel.getAllBooks(context = context)
            Library(viewModel, actions)
        }

      // Task Details
        composable(
            "${NavRoutes.BookDetails.route}/{id}",
            arguments = listOf(navArgument(EndPoints.ID) { type = NavType.StringType })
        ) {
            val viewModel = hiltViewModel<MainViewModel>(it)
            val isbnNo = it.arguments?.getString(EndPoints.ID)
                ?: throw IllegalStateException("'Book ISBN No' shouldn't be null")

            viewModel.getBookById(context = context, isbnNo = isbnNo)
            //BookDetails(viewModel, actions)
            BookDetails(viewModel,isbnNo ,actions)
        }
    }
}



class MainActions(navController: NavController) {

    val upPress: () -> Unit = {
        navController.navigateUp()
    }
    //navigate to book details- view
    val gotoBookDetails: (String) -> Unit = { isbnNo ->
        navController.navigate("${NavRoutes.BookDetails.route}/$isbnNo")
    }

    val gotoBookList: () -> Unit = {
        navController.navigate(NavRoutes.Library.route)
    }
}


/**
@Composable
fun MainScreen(){
    val navController = rememberNavController()
    Scaffold(
        content = {NavigationHost(navController = navController)},
        bottomBar = {BottomNavBar(navController = navController)}
    )
}


@Composable
fun NavigationHost(navController: NavHostController) {
    NavHost(navController = navController, startDestination = "activity") {
        composable("activity") {
            MyActivity()
        }
        composable("library") {
            Library()
        }
        composable("saved") {
            Saved()
        }
    }
}
        **/