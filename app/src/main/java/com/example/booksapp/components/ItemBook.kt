package com.example.booksapp.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import com.example.booksapp.ui.theme.typography

@Composable
fun ItemBook() {
    Box(modifier = Modifier){
        Column(modifier = Modifier.padding(10.dp)){
            ImageLoader()

            Text(text ="Morgan Housel", style  = typography.caption, color = com.example.booksapp.ui.theme.Text)
            Spacer(modifier = Modifier.height(2.dp))
            Text(text ="How Innovation Works", style = typography.subtitle2, color = com.example.booksapp.ui.theme.Text)
        }
    }
}

@Composable
fun ImageLoader(){
    val url = "https://images-na.ssl-images-amazon.com/images/I/51ZyvZ9ZGJL._SX331_BO1,204,203,200_.jpg"
    Image(
        painter = rememberImagePainter(data = url),
        contentDescription = " ",
        modifier = Modifier.size(110.dp, 176.dp),
        contentScale = ContentScale.Fit
    )
}
