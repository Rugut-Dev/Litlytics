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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.booksapp.data.firestore.Book
import com.example.booksapp.navigation.MainActions
import com.example.booksapp.ui.theme.Background
import com.example.booksapp.viewModel.NoteUiState
import com.example.booksapp.viewModel.NotesViewModel
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch

@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun UploadScreen(
    notesViewModel: NotesViewModel?,
    onNavigate: (String) -> Unit,
    actions:MainActions,
    noteId: String,
    //navController: NavController
) {


    val noteUiState = notesViewModel?.noteUiState ?: NoteUiState()
    //validate the form
    val isFormsNotBlank =
        noteUiState.title.isNotBlank() && noteUiState.content.isNotBlank() && noteUiState.bookRef.isNotBlank()
    //check if note Id is not blank for update
    val isNoteIdBlank = noteId.isEmpty()

    //if upon launching the screen, the note fields are blank, then show an add icon, else show an edit icon
    val icon = if(isNoteIdBlank){
        Icons.Filled.Add
    }else {
        Icons.Filled.Refresh
    }

    //fetch note if note id is not blank
    LaunchedEffect(key1 = Unit) {
        if (!isNoteIdBlank) {
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
        topBar = {
            TopAppBar(
                title = { Text(text = "Upload") },
                navigationIcon = {
                    IconButton(onClick = { actions.upPress() }) {
                        Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = null)
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    if(isFormsNotBlank && isNoteIdBlank){
                        notesViewModel?.addNote()
                    }else{
                        notesViewModel?.updateNote(noteId)
                    }
            }) {
                Icon(imageVector = icon, contentDescription = null)
            }
        },
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(color = Background)
                .padding(padding)
        ) {
            //snack bar
            if (noteUiState.noteAddedStatus) {
                scope.launch {
                    val snackbarResult = scaffoldState.snackbarHostState.showSnackbar(
                        message = "Note added successfully",
                        actionLabel = "Ok"
                    )
                    if (snackbarResult == SnackbarResult.ActionPerformed) {
                        onNavigate.invoke("library")
                    }
                    notesViewModel?.resetNoteAddedStatus()
                }
            }
            if (noteUiState.updateNoteStatus) {
                scope.launch {
                    val snackbarResult = scaffoldState.snackbarHostState.showSnackbar(
                            message = "Note updated successfully",
                            actionLabel = "Ok"
                        )
                    if (snackbarResult == SnackbarResult.ActionPerformed) {
                        actions.upPress()
                    }
                    notesViewModel?.resetNoteAddedStatus()
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
                   // Log.d("NoteContent", "onValueChange called with value: $it")
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

