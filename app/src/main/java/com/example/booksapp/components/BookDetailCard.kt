package com.example.booksapp.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter

@Composable
fun BookDetailsCard(
    title: String,
    authors: List<String>,
    thumbnailUrl: String,
    isbnNo: String,
   // categories: List<String>
) {
        // white box layout
        Box(
            Modifier
                .fillMaxWidth()
                .size(130.dp, 200.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(color = Color.Transparent),
                //.padding(start = 20.dp, end = 16.dp, top = 40.dp),
                //.background(MaterialTheme.colors.onSurface),
            contentAlignment = Alignment.Center,
        ) {
            // Content
            //BookImageContentView(title, thumbnailUrl)
            Image(
                painter = rememberImagePainter(
                    data = thumbnailUrl
                ),
                contentDescription = title,
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(12.dp))
                    .background(color = Color.Transparent),
                alignment = Alignment.Center
            )
        }
    }

/**
@Composable
fun BookImageContentView(
    title: String,
    //authors: List<String>,
    thumbnailUrl: String,
    //categories: List<String>
) {
    // content
    Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {

        // image

/**
        Spacer(modifier = Modifier.height(16.dp))

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colors.onSurface)
                .padding(bottom = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = title,
                style = typography.h6,
                textAlign = TextAlign.Center,
                color = Text
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = authors.toString(),
                style = typography.caption,
                textAlign = TextAlign.Center,
                color = Text.copy(0.7F)
            )

         //   Spacer(modifier = Modifier.height(12.dp))
           // Row {
             //   categories.forEach {
                //     ChipView(category = it)
                //}
           // }
        }
        //display list of notes from firebase here
**/

    }
}

**/
