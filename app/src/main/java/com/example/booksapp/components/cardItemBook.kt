package com.example.booksapp.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import com.example.booksapp.ui.theme.Card


@Composable
fun CardItemBook(
    title: String,
    author: String,
    thumbnailUrl: String,
    categories: List<String>,
    onItemClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onItemClick() }
            .background(color = Card)
            .padding(16.dp),
        shape = RoundedCornerShape(12.dp),
        elevation = 4.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(90.dp, 110.dp)
                    .padding(end = 16.dp)
            ) {
                Image(
                    painter = rememberImagePainter(data = thumbnailUrl),
                    contentDescription = " ",
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(12.dp)))
            }
            Column {
                Text(
                    text = title,
                    style = MaterialTheme.typography.h6
                )
                Spacer(modifier = Modifier.height(4.dp) )
                Text(
                    text = author,
                    style = MaterialTheme.typography.body2
                )
                Spacer(modifier = Modifier.height(8.dp) )
                Text(
                    text = categories.toString(),
                    style = MaterialTheme.typography.body2
                )
            }
        }
    }
}




/**
@Composable
fun CardItemBook(
    title: String,
    author: String,
    thumbnailUrl: String,
    categories: List<String>,
    onItemClick: () -> Unit
) {
    Card(modifier = Modifier
        .fillMaxWidth()
        .clickable { onItemClick() }
        .background(color = Card)
        .padding(16.dp),
        shape = RoundedCornerShape(12.dp),
        elevation = 4.dp
    ) {
        //Row-image +content
        Row(
            modifier = Modifier.padding(10.dp),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        )
        {

            Image(
                painter = rememberImagePainter(data = thumbnailUrl),
                contentDescription = " ",
                modifier = Modifier.size(90.dp, 110.dp))

            Spacer(modifier = Modifier.width(8.dp))

           TextCard(title, author, categories)
        }
    }
}

@Composable
fun TextCard( title: String,
              author: String,
              categories: List<String>){
    Column {
        Text(text = author, style  = typography.caption, color = com.example.booksapp.ui.theme.Text)
        Spacer(modifier = Modifier.height(10.dp))
        Text(text =title, style  = typography.subtitle2, color = com.example.booksapp.ui.theme.Text)
        Spacer(modifier = Modifier.height(12.dp))
        Row {
            categories.forEach { category ->
                ChipView(category)
                Spacer(modifier = Modifier.width(8.dp))
            }
        }
    }
}

@Composable
fun ChipView(category: String){
    Box(modifier = Modifier
        .clip(RoundedCornerShape(12.dp))
        .background(color = Card.copy(10f))
        .padding(top = 5.dp, bottom = 5.dp, start = 12.dp, end = 12.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(text = category, style  = typography.caption, color = Primary)
    }
}
 **/
