package com.example.booksapp.view

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun MyActivityScreen(){
    Box(modifier = Modifier.fillMaxSize()){
        Text(text = "My excerpts show here")
    }
}