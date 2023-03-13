package com.example.booksapp.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home

object NavBarItems {
    val BarItems = listOf(
        BarItem(
            title = "Library",
            image = Icons.Filled.Home,
            route = "library"
        ),
        BarItem(
            title = "My Activity",
            image = Icons.Filled.AccountBox,
            route = "activity"
        ),
        BarItem(
            title = "Saved",
            image = Icons.Filled.Favorite,
            route = "saved"
        )
    )
}