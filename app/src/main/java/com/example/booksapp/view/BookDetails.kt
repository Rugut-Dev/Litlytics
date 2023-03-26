package com.example.booksapp.view

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.booksapp.components.BookDetailsCard
import com.example.booksapp.model.Notes
import com.example.booksapp.repository.StorageRepository
import com.example.booksapp.utils.ViewState
import com.example.booksapp.viewModel.MainViewModel

@Composable
fun BookDetails(
    viewModel: MainViewModel,
    isbnNo: String,
    userId: String,
    bookRef: String,
    isbn: String,

) {

    val storageRepository = StorageRepository()
    val notes = storageRepository
        //get all notes for particular book
        .getAllNotes(bookRef, isbn, isbnNo).collectAsState(initial = ViewState.Loading)


    val listState = rememberLazyListState()

    val booksState by viewModel.books.collectAsState(initial = ViewState.Loading)
    when (val result = booksState) {
        ViewState.Empty -> Text(text = "No Books Found!")
        is ViewState.Error -> Text(text = "Error Occurred: ${result.exception}")
        ViewState.Loading -> Text(text = "Loading...")
        is ViewState.Success -> {
            val book = result.data.find { it.isbn == isbnNo }
            BookDetailsCard(
                book?.title ?: "",
                book?.authors ?: listOf(),
                book?.thumbnailUrl ?: "",
                book?.isbn ?: "",
            )
        }
    }

}

@Composable
fun NoteCard(note: Notes) {
    Card(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth(),
        elevation = 8.dp,
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            // Display the note title
            Text(
                text = note.title,
                style = MaterialTheme.typography.h6
            )

            // Display the note content
            Text(
                text = note.content,
                style = MaterialTheme.typography.body1,
                modifier = Modifier.padding(top = 8.dp)
            )

            // Display the note timestamp
            Text(
                text = note.timestamp.toString(),
                style = MaterialTheme.typography.caption,
                modifier = Modifier.padding(top = 8.dp)
            )
        }
    }
}


/**
@Composable
fun BookDetailCard(
                   
                   title: String,
                   authors: List<String>,
                   thumbnailUrl: String,
                   categories: List<String>) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
        ) {
            LazyColumn(
                state = listState,
            ) {
                item {
                    TopBarBookDetail()
                }

                item {

                    BookDetailTab(
                        selectedTab = viewModel.selectedTab,
                        onTabSelected = viewModel::onTabSelected
                    )
                    if (viewModel.selectedTab == 0) {
                        Description()
                    } else if (viewModel.selectedTab == 1) {
                        BookExcerpts()
                    }
                }
            }
        }
    }
@Composable
fun Description() {
    Box(modifier = Modifier.padding(start = 16.dp, end = 16.dp)) {
        Text(
            text = "Android is an open source mobile phone platform based on the Linux operating system and developed by the Open Handset Alliance, a consortium of over 30 hardware, software and telecom companies that focus on open standards for mobile devices. Led by search giant, Google, Android is designed to deliver a better and more open and cost effective mobile experience.    Unlocking Android: A Developer's Guide provides concise, hands-on instruction for the Android operating system and development tools. This book teaches important architectural concepts in a straightforward writing style and builds on this with practical and useful examples throughout. Based on his mobile development experience and his deep knowledge of the arcane Android technical documentation, the author conveys the know-how you need to develop practical applications that build upon or replace any of Androids features, however small.    Unlocking Android: A Developer's Guide prepares the reader to embrace the platform in easy-to-understand language and builds on this foundation with re-usable Java code examples. It is ideal for corporate and hobbyists alike who have an interest, or a mandate, to deliver software functionality for cell phones.    WHAT'S INSIDE:        * Android's place in the market      * Using the Eclipse environment for Android development      * The Intents - how and why they are used      * Application classes:            o Activity            o Service            o IntentReceiver       * User interface design      * Using the ContentProvider to manage data      * Persisting data with the SQLite database      * Networking examples      * Telephony applications      * Notification methods      * OpenGL, animation & multimedia      * Sample Applications  ",
            color = com.example.booksapp.ui.theme.Text,
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.body1
        )
    }
}
@Composable
fun BookExcerpts() {
    Box(modifier = Modifier.padding(start = 16.dp, end = 16.dp)) {
        Text(text = "Book Excerpt Show here",
        color = com.example.booksapp.ui.theme.Text,
        textAlign = TextAlign.Center,
        style = MaterialTheme.typography.body1
        )
    }
}
**/
/**
Box(
modifier = Modifier
.fillMaxSize()
.background(Color.White)
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min)
            .background(Color.Green)
            .clip(RoundedCornerShape(bottomStart = 16.dp, bottomEnd = 16.dp))
            .layout { measurable, constraints ->
                val placeable = measurable.measure(constraints)
                layout(placeable.width, placeable.height) {
                    placeable.placeRelative(0, 0)
                }
            }
    )
}
**/