package com.example.booksapp.view

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.booksapp.components.BookDetailsCard
import com.example.booksapp.model.Notes
import com.example.booksapp.navigation.MainActions
import com.example.booksapp.ui.theme.Background
import com.example.booksapp.ui.theme.Card
import com.example.booksapp.ui.theme.HeadTile
import com.example.booksapp.ui.theme.Primary
import com.example.booksapp.utils.ViewState
import com.example.booksapp.viewModel.MainViewModel
import com.example.booksapp.viewModel.NotesUiState
import com.example.booksapp.viewModel.NotesViewModel
import com.google.firebase.auth.FirebaseAuth
import java.text.SimpleDateFormat
import java.util.*

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun BookDetails(
    viewModel: MainViewModel,
    isbnNo: String,//passed from library
    notesViewModel: NotesViewModel?,
    actions: MainActions
) {
    val notesUiState = notesViewModel?.notesUiState ?: NotesUiState.Loading

    if (notesUiState == NotesUiState.Loading) {
            notesViewModel?.getAllNotes(isbnNo)
    }

    val scaffoldState = rememberScaffoldState()
Box() {
    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            TopAppBar(
                title = {
                    val booksState by viewModel.books.collectAsState(initial = ViewState.Loading)
                    when (val result = booksState) {
                        ViewState.Empty -> Text(text = "No Books Found!")
                        is ViewState.Error -> Text(text = "Error Occurred: ${result.exception}")
                        ViewState.Loading -> Text(text = "Loading...")
                        is ViewState.Success -> {
                            val book = result.data.find { it.isbn == isbnNo }
                            //print the books title
                            Text(text = book?.title ?: "")
                        }
                        else -> {
                            Text(text = "Unknown Error Occurred!")
                        }
                    }
                },
                navigationIcon = {
                    IconButton(onClick = { actions.upPress() }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                backgroundColor = Primary,
                contentColor = HeadTile,
                elevation = 12.dp
            )
        },
        content = {


            //lazy column to allow for scrolling
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Background)
            ) {
                item {
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
                        else -> {
                            Text(text = "Unknown Error Occurred!")
                        }
                    }
                }
                //list of notes exhibiting the notes card
                when (val result = notesUiState) {
                    NotesUiState.Loading -> item {
                        Text(text = "Loading...")
                    }
                    is NotesUiState.Error -> item {
                       // Log .d("NotesUiState1", "Loading Notes")
                        Text(text = "Error Occurred: ${result.throwable}")
                    }
                    is NotesUiState.Success -> {
                        val notes = result.notes.toMutableList()
                        if (notes.isEmpty()) {
                            item {
                                Text(text = "No notes found!")
                            }
                        } else {
                            notes.forEach { note ->
                                item {
                                    NoteItem(notes = note, notesViewModel = notesViewModel!!)
                                }
                            }
                        }
                    }
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

//note card for each note with  fields coresponding to the note model-> user_id, title, content, book_ref, page_ref, timestamp, documentId
@Composable
fun NoteItem(
    notes: Notes,
    notesViewModel: NotesViewModel
) {
    //val repository = StorageRepository()
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .background(Card)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = notes.title,
                style = MaterialTheme.typography.h6,
                color = com.example.booksapp.ui.theme.Text,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            Text(
                text = notes.content,
                style = MaterialTheme.typography.body1,
                color = com.example.booksapp.ui.theme.Text,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            //Timestamp at the bottom of the note card
            Text(
                text = formatDate(notes.timestamp),
                style = MaterialTheme.typography.body1,
                color = Color.LightGray,
                modifier = Modifier.padding(bottom = 4.dp)
            )
            Text(
                text = notes.page_ref.toString(),
                style = MaterialTheme.typography.body1,
                color = Color.Black,
                modifier = Modifier.padding(bottom = 8.dp)
            )
        }
        //delete button
        //show button if the user is the owner of the note
        var showDialog by remember { mutableStateOf(false) }
        if (showDialog) {
            AlertDialog(
                onDismissRequest = {
                    showDialog = false
                },
                title = {
                    Text(text = "Delete Note")
                },
                text = {
                    Text(text = "Are you sure you want to delete this note?")
                },
                confirmButton = {
                    TextButton(onClick = {
                        notesViewModel.deleteNote(noteId = notes.documentId)
                        showDialog = false
                    }) {
                        Text("Delete")
                    }
                },
                dismissButton = {
                    TextButton(onClick = {
                        showDialog = false
                    }) {
                        Text("Cancel")
                    }
                }
            )
        }

        if (notes.user_id == FirebaseAuth.getInstance().currentUser?.uid) {
            IconButton(
                onClick = {
                    showDialog = true
                },
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Delete Note",
                    tint = Primary
                )
            }
        }
        //edit button
        //show button if the user is the owner of the note
        if (notes.user_id == FirebaseAuth.getInstance().currentUser?.uid) {
            IconButton(
                onClick = {
                          // navigate to upload note screen with the note data

                },
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = "Edit Note",
                    tint = Primary
                )
            }
        }

    }
}

//private function to convert date
private fun formatDate(timestamp: com.google.firebase.Timestamp):String{
    val sdf = SimpleDateFormat("MM-dd-yy hh:mm", Locale.getDefault())
    return sdf.format(timestamp.toDate())
}


/**

**/


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