package com.example.booksapp.navigation

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Box
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.HiltViewModelFactory
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.booksapp.repository.StorageRepository
import com.example.booksapp.view.*
import com.example.booksapp.viewModel.AuthViewModel
import com.example.booksapp.viewModel.MainViewModel
import com.example.booksapp.viewModel.NotesViewModel

object EndPoints {
    const val ID = "id"
}
@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@ExperimentalComposeUiApi
@Composable
fun NavGraph() {
    val navController = rememberNavController()
    Box() {
        Scaffold(
            content = {
                NavigationHost(navController = navController, authViewModel = AuthViewModel())
            },
            bottomBar = { /*BottomNavigationBar(navController = navController)*/ }
        )
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun NavigationHost(navController: NavHostController, authViewModel: com.example.booksapp.viewModel.AuthViewModel){

    val actions = remember(navController) { MainActions(navController) }
    val context = LocalContext.current

    NavHost(navController=navController, startDestination = RegRoutes.Login.route) {
        composable(RegRoutes.Login.route) {
            LoginScreen(
                navController = navController, onNavToLibrary = {
                    navController.navigate("library") {
                        launchSingleTop = true
                        popUpTo("login") {
                            inclusive = true
                        }
                    }
                },
                authViewModel = authViewModel) {
                navController.navigate("signUp") {
                    launchSingleTop = true
                    popUpTo("login") {
                        inclusive = true
                    }
                }
            }
        }
        composable(RegRoutes.SignUp.route){
            SignUpScreen(navController = navController, onNavToLibrary = { navController.navigate("library"){
                launchSingleTop = true
                popUpTo("signUp"){
                    inclusive = true
                }
            } },
                authViewModel = authViewModel
            ) {
                navController.navigate("login")
            }
        }

        // Library
        composable(NavRoutes.Library.route) {
            val viewModel: MainViewModel = viewModel(
                factory = HiltViewModelFactory(LocalContext.current, it)
            )
            viewModel.getAllBooks(context = context)
            Library(viewModel, actions, notesViewModel = NotesViewModel(repository = StorageRepository()))
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
            BookDetails(
                viewModel,
                isbnNo,
                notesViewModel = NotesViewModel(repository = StorageRepository()),
                actions = actions
            )
        }
        //My Activity
        composable(NavRoutes.MyActivity.route){
            MyActivityScreen()
        }

        //Saved
        composable(NavRoutes.Saved.route){
            SavedScreen()
        }

        //Upload
        composable(NavRoutes.Upload.route){
            UploadScreen(notesViewModel = NotesViewModel(repository = StorageRepository()),
                onNavigate = {
                    navController.navigate("library")
                },
                repository = StorageRepository(),
                actions = actions
            )
        }
    }
}


     /**

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
        //My Activity
        composable(NavRoutes.MyActivity.route){
            MyActivityScreen()
        }

        //Saved
        composable(NavRoutes.Saved.route){
            SavedScreen()
        }
    }
}
**/


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
    //navigate to login- view
    val gotoLogin: () -> Unit = {
        navController.navigate(RegRoutes.Login.route)
    }
    //navigate to upload- view
    val gotoUpload: () -> Unit = {
        navController.navigate(NavRoutes.Upload.route)
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