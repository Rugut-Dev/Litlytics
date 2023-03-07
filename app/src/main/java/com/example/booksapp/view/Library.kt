package com.example.booksapp.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.booksapp.R
import com.example.booksapp.components.CardItemBook
import com.example.booksapp.components.TextInputField
import com.example.booksapp.model.BookItem
import com.example.booksapp.navigation.MainActions
import com.example.booksapp.utils.ViewState
import com.example.booksapp.viewModel.MainViewModel


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
        item{
            Text(text = "Books",
                style = MaterialTheme.typography.subtitle1,
                color = MaterialTheme.colors.primaryVariant,
                textAlign = TextAlign.Start
            )
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
