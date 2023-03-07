package com.example.booksapp.components

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.booksapp.ui.theme.Background
import com.example.booksapp.ui.theme.Card
import kotlin.reflect.KProperty0

@Composable
fun BookDetailTab(selectedTab: Int, onTabSelected: KProperty0<(Int) -> Unit>){
    val tabs = listOf("Book Excerpts", "Description")
    val selectedTabIndex = remember { mutableStateOf(selectedTab) }

    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
        tabs.forEachIndexed { index, tab ->
            val tabModifier = Modifier
                .weight(1f)
                .clickable { selectedTabIndex.value = index }
                .padding(vertical = 8.dp, horizontal = 16.dp)
                .animateContentSize()
                .background(if (selectedTabIndex.value == index) Card else Background)
                .clip(RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp))
                .heightIn(min = 48.dp)
                .align(Alignment.CenterVertically)
            Text(
                text = tab,
                color = com.example.booksapp.ui.theme.Text,
                textAlign = TextAlign.Center,
                modifier = tabModifier
            )
        }
    }
}





/**
@Composable
fun TabRowWithAnimations(selectedTab: Int, onTabSelected: (Int) -> Unit) {
    val tabs = listOf("Tab 1", "Tab 2", "Tab 3")
    val selectedTabIndex = remember { mutableStateOf(selectedTab) }

    Row(Modifier.fillMaxWidth()) {
        tabs.forEachIndexed { index, tab ->
            val tabModifier = Modifier
                .weight(1f)
                .clickable { selectedTabIndex.value = index }
                .padding(vertical = 16.dp, horizontal = 8.dp)
                .animateContentSize()
                .background(if (selectedTabIndex.value == index) Color.Blue else Color.Gray)
                .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))

            Text(
                text = tab,
                color = Color.White,
                textAlign = TextAlign.Center,
                modifier = tabModifier
            )
        }
    }
}
**/