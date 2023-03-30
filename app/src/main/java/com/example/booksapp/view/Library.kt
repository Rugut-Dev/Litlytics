package com.example.booksapp.view

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.booksapp.R
import com.example.booksapp.components.CardItemBook
import com.example.booksapp.components.TextInputField
import com.example.booksapp.model.BookItem
import com.example.booksapp.navigation.MainActions
import com.example.booksapp.ui.theme.HeadTile
import com.example.booksapp.utils.ViewState
import com.example.booksapp.viewModel.MainViewModel
import com.example.booksapp.viewModel.NotesViewModel

@OptIn(ExperimentalComposeUiApi::class)
@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun Library(viewModel: MainViewModel, actions: MainActions, notesViewModel: NotesViewModel?) {
    Box() {
        Scaffold(
            topBar = {
                TopAppBar(
                    //Row for the title and log out button
                    title = {
                        Box(modifier = Modifier.fillMaxWidth()) {
                            Row(
                                horizontalArrangement = Arrangement.Center,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = stringResource(id = R.string.app_name),
                                    textAlign = TextAlign.Center,
                                    color = com.example.booksapp.ui.theme.Text
                                )
                                Spacer(Modifier.weight(1f)) // Add a spacer to push the button to the right
                                IconButton(
                                    onClick = {
                                        notesViewModel?.signOut()
                                        actions.gotoLogin()
                                    },
                                    modifier = Modifier
                                        .size(60.dp)
                                        .padding(end = 16.dp, top = 8.dp, bottom = 8.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.AccountBox,
                                        contentDescription = "Log Out",
                                        tint = Color.LightGray
                                    )
                                }
                            }
                        }
                    }
                )
            },
            content = {
                val booksState by viewModel.books.collectAsState(initial = ViewState.Loading)
                when (val result = booksState) {
                    ViewState.Empty -> Text(text = "No Books Found!")
                    is ViewState.Error -> Text(text = "Error Occurred: ${result.exception}")
                    ViewState.Loading -> Text(text = "Loading...")
                    is ViewState.Success -> {
                        BookList(result.data, actions)
                    }
                }
            },
            floatingActionButton = {
                FloatingActionButton(
                    onClick = { actions.gotoUpload() },
                    modifier = Modifier
                        .size(100.dp)
                        .padding(16.dp)
                        //Space between bottom nav and fab
                        .absolutePadding(bottom = 16.dp, right = 16.dp),
                    contentColor = Color.White,
                    backgroundColor = HeadTile
                ) {
                    Icon(
                        imageVector = Icons.Filled.Add,
                        contentDescription = "Add"
                    )
                }
            },
            isFloatingActionButtonDocked = true,
            floatingActionButtonPosition = FabPosition.End
        )
    }
}
/**


@ExperimentalComposeUiApi
@Composable
fun Library(viewModel: MainViewModel, actions: MainActions) {

    val booksState by viewModel.books.collectAsState(initial = ViewState.Loading)
    when(val result = booksState){
        ViewState.Empty -> Text(text ="No Books Found!")
        is ViewState.Error -> Text(text = "Error Occurred: ${result.exception}")
        ViewState.Loading -> Text(text = "Loading...")
        is ViewState.Success -> {
            BookList(result.data, actions)
        }
    }
}
 **/
@ExperimentalComposeUiApi
@Composable
fun BookList(bookList: List<BookItem>, actions: MainActions) {

    val input = remember{
        mutableStateOf("")
    }

   val listState = rememberLazyListState()

    LazyColumn(
        state = listState,
        contentPadding = PaddingValues(top = 24.dp, bottom = 24.dp),
        modifier = Modifier.background(MaterialTheme.colors.background)
    ) {
        //Search
        item {
            TextInputField(label = stringResource(R.string.search_text), value = input.value, onValueChanged = {
                input.value = it
            } )
        }

        items(bookList.filter { it.title.contains(input.value, ignoreCase = true) }) {book->
            //Log.d("books", "books are ${Book.title}")
            CardItemBook(
               book.title,
               book.authors.toString(),
               book.thumbnailUrl,
               book.categories,
               onItemClick = {
                   actions.gotoBookDetails.invoke(book.isbn)
               }
            )
        }

    }

    /**
    FloatingActionButton(
        onClick = { /* do something */ },
        modifier = Modifier
            .size(56.dp)
            .padding(16.dp)
            .align()
    ) {
        Icon(
            imageVector = Icons.Filled.Add,
            contentDescription = "Add"
        )
    }
    **/
}

/**
@Composable
fun BookList(bookViewModel: BookViewModel = viewModel()) {
val books = bookViewModel.getBooks()
LazyColumn {
items(books) { book ->
Card(
modifier = Modifier
.fillMaxWidth()
.padding(16.dp),
elevation = 8.dp,
shape = MaterialTheme.shapes.medium
) {
Row(
modifier = Modifier.padding(16.dp)
) {
Image(
painter = painterResource(id = R.drawable.book_cover),
contentDescription = "Book cover",
contentScale = ContentScale.Crop,
modifier = Modifier
.size(96.dp, 128.dp)
.padding(end = 16.dp)
)

Column {
Text(
text = book.title,
style = MaterialTheme.typography.h5
)

Text(
text = "By ${book.author}",
style = MaterialTheme.typography.subtitle1
)
}
}
}
}
}
}
 **/
