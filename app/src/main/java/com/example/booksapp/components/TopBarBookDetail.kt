package com.example.booksapp.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.layout
import androidx.compose.ui.unit.dp
import com.example.booksapp.ui.theme.HeadTile
import com.example.booksapp.ui.theme.typography

@Composable
fun TopBarBookDetail() {

        Box(modifier = Modifier
            .height(IntrinsicSize.Min)
            .fillMaxWidth()
            .background(color = HeadTile)
            .clip(RoundedCornerShape(bottomStart = 90.dp, bottomEnd = 90.dp))
            .layout { measurable, constraints ->
                val placeable = measurable.measure(constraints)
                layout(placeable.width, placeable.height) {
                    placeable.placeRelative(0, 0)
                }
            }
        ) {

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(color = Color.Transparent),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically
            ) {
                ImageLoader()
                Column {
                    Text(
                        text = "How Innovation Works",
                        style = MaterialTheme.typography.h6,
                        color = com.example.booksapp.ui.theme.Text
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Morgan Housel",
                        style = typography.caption,
                        color = com.example.booksapp.ui.theme.Text
                    )
                }
            }
        }

    }



/**
Box(
modifier = Modifier
.fillMaxSize()
.background(color = Background)
) {}
**/