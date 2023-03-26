package com.example.booksapp.view

import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.booksapp.data.firestore.Book
import com.example.booksapp.viewModel.NoteUiState
import com.example.booksapp.viewModel.NotesViewModel
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch

@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun UploadScreen(
    notesViewModel: NotesViewModel?,
    onNavigate: (String) -> Unit,
    noteId: String
) {

    val noteUiState = notesViewModel?.noteUiState ?: NoteUiState()
    //validate the form
    val isFormsNotBlank = noteUiState?.title?.isNotBlank() == true &&
            noteUiState.content.isNotBlank() &&
            noteUiState.bookRef.isNotBlank() //&&


    //check if note Id is not blank for update
    val isNoteIdNotBlank = noteId.isNotBlank()
    val icon = if (isFormsNotBlank) Icons.Default.Refresh
    else Icons.Default.Check

    //fetch note if note id is not blank
    LaunchedEffect(key1 = Unit) {
        if (isNoteIdNotBlank) {
            notesViewModel?.getNote(noteId)
        }else{
            notesViewModel?.resetState()
        }
    }

    var searchText by remember { mutableStateOf("") }
    var selectedBook by remember { mutableStateOf<Book?>(null) }
    val dropdownList by remember { mutableStateOf(mutableStateListOf<Book>()) }

    //coroutine scope
    val scope = rememberCoroutineScope()

    //scaffold for snack bar
    val scaffoldState = rememberScaffoldState()

    Scaffold(
        scaffoldState = scaffoldState,
        floatingActionButton = {
            FloatingActionButton(onClick = {
                if (isNoteIdNotBlank){
                    notesViewModel?.updateNote(noteId)
                }else{
                    notesViewModel?.addNote()
                }
            }) {
                Icon(imageVector = icon, contentDescription = null)
            }
        },
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(color = Color.Blue)
                .padding(padding)
        ) {
            //snack bar
            if (noteUiState.noteAddedStatus) {
                scope.launch {
                    scaffoldState.snackbarHostState
                        .showSnackbar(
                            message = "Note added successfully",
                            actionLabel = "Ok"
                        )
                    notesViewModel?.resetNoteAddedStatus()
                    //onNavigate.invoke()
                }
            }
            if (noteUiState.updateNoteStatus) {
                scope.launch {
                    scaffoldState.snackbarHostState
                        .showSnackbar(
                            message = "Note updated successfully",
                            actionLabel = "Ok"
                        )
                    notesViewModel?.resetNoteAddedStatus()
                    //onNavigate.invoke()
                }
            }

            Column(modifier = Modifier.padding(16.dp)) {
                TextField(
                    value = searchText,
                    onValueChange = {
                        searchText = it
                        if (it.length >= 3) {
                            val collectionRef = FirebaseFirestore.getInstance().collection("books")
                            collectionRef.get().addOnSuccessListener { documents ->
                                val books = documents.mapNotNull {
                                    val title = it.getString("title") ?: ""
                                    val isbn = it.getString("isbn") ?: ""
                                    Book(title, isbn)
                                }.filter { book ->
                                    book.title.contains(searchText, ignoreCase = true)
                                }
                                dropdownList.apply {
                                    clear()
                                    addAll(books)
                                }
                            }.addOnFailureListener { exception ->
                                Log.e(TAG, "Error getting documents: ", exception)
                            }
                        } else {
                            dropdownList.clear()
                        }
                    },
                    label = { Text("Search for a book") },
                    modifier = Modifier.fillMaxWidth()
                )

                if (dropdownList.isNotEmpty()) {
                    Box(
                        modifier = Modifier
                            .wrapContentHeight()
                            .background(MaterialTheme.colors.surface)
                            .border(1.dp, MaterialTheme.colors.onSurface.copy(alpha = 0.2f))
                            .align(Alignment.CenterHorizontally)
                            .padding(8.dp)
                    ) {
                        DropdownMenu(
                            expanded = true,
                            onDismissRequest = { dropdownList.clear() },
                        ) {
                            dropdownList.take(5).forEach { book ->
                                val title = book.title
                                DropdownMenuItem(onClick = {
                                    selectedBook = book
                                    notesViewModel?.onBookRefChange(book.isbn)
                                    dropdownList.clear()
                                    searchText = book.title
                                }) {
                                    Text(title)
                                }
                            }
                        }
                    }
                }

                if (selectedBook != null) {
                    Text(
                        text = "Selected Book: ${selectedBook?.title}",
                        color = if (selectedBook?.title?.contains(
                                searchText,
                                ignoreCase = true
                            ) == true
                        ) {
                            MaterialTheme.colors.onBackground
                        } else {
                            Color.Red
                        }
                    )
                }
            }
            //title
            OutlinedTextField(
                value = noteUiState.title,
                onValueChange = {
                    notesViewModel?.onTitleChange(it)
                },
                label = { Text(text = "Title") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                keyboardOptions = KeyboardOptions.Default

            )

            //page number stored as int
            OutlinedTextField(
                value = noteUiState.pageRef?.toString() ?: "",
                onValueChange = {
                    val newPageRef = it.toIntOrNull() ?: 0
                    notesViewModel?.onPageRefChange(newPageRef)
                },
                label = { Text(text = "Page Number") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                keyboardOptions = KeyboardOptions.Default
            )


            //content
            OutlinedTextField(
                value = noteUiState.content,
                onValueChange = {
                    Log.d("NoteContent", "onValueChange called with value: $it")
                    notesViewModel?.onContentChange(it)
                },
                label = { Text(text = "Type Note") },
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(8.dp),
                keyboardOptions = KeyboardOptions.Default
            )

        }
    }
}

