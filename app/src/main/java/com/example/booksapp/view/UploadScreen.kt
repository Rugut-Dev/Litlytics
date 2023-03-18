package com.example.booksapp.view

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun UploadScreen(){
    ComposeTweet(onTweetPublished ={ tweetText -> /* TODO */ })
}

@Composable
fun ComposeTweet(
    onTweetPublished: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var tweetText by remember { mutableStateOf("") }

    Column(modifier = modifier) {
        // Tweet input field
        OutlinedTextField(
            value = tweetText,
            onValueChange = { tweetText = it },
            label = { Text("What's happening?") },
            maxLines = 5,
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        )

        // Add media button
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            IconButton(onClick = { /* TODO */ }) {
                Icon(Icons.Default.Add, contentDescription = null)
            }
            IconButton(onClick = { /* TODO */ }) {
                Icon(Icons.Default.Delete, contentDescription = null)
            }
        }

        // Tweet button
        Button(
            onClick = { onTweetPublished(tweetText) },
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            enabled = tweetText.isNotEmpty()
        ) {
            Text("Tweet")
        }
    }
}

