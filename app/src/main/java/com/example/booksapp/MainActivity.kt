package com.example.booksapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.booksapp.components.ItemBook
import com.example.booksapp.data.firestore.populateBooksCollection
import com.example.booksapp.navigation.NavBarItems
import com.example.booksapp.navigation.NavGraph
import com.example.booksapp.ui.theme.BooksAppTheme
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.AndroidEntryPoint

@ExperimentalComposeUiApi
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private lateinit var db: FirebaseFirestore
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        db = FirebaseFirestore.getInstance()
        populateBooksCollection(applicationContext)
        setContent {

            BooksAppTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    //BookDetails(viewModel = MainViewModel())
                    NavGraph()
                }
                }
            }
        }
    }

@Composable
fun BottomNavigationBar(navController:NavHostController){
    BottomNavigation {
        val backStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = backStackEntry?.destination?.route

        NavBarItems.BarItems.forEach {navItem->
            BottomNavigationItem(selected = currentRoute == navItem.route, onClick = { navController.navigate(navItem.route){
                popUpTo(navController.graph.findStartDestination().id){
                    saveState = true
                }
                launchSingleTop = true
                restoreState = true
            } }, icon = {
                Icon(imageVector = navItem.image, contentDescription = "navItem.title")
            },
                label = {
                    Text(text = navItem.title)
                }
            )
        }
    }
}


@OptIn(ExperimentalComposeUiApi::class)
@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    BooksAppTheme {
        ItemBook()
    }
}
