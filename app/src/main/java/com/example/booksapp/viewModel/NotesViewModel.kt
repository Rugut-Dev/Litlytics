package com.example.booksapp.viewModel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.booksapp.model.Notes
import com.example.booksapp.repository.StorageRepository
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseUser

class NotesViewModel(
    private val repository:StorageRepository
): ViewModel() {
    var noteUiState by mutableStateOf(NoteUiState())
        private set

    //check if user is logged in
    private val hasUser: Boolean
        get() = repository.hasUser()

    //get current user
    private val user:FirebaseUser?
        get() = repository.user

    //title state
    fun onTitleChange(title: String) {
        noteUiState = noteUiState.copy(title = title)
    }

    //content state
    fun onContentChange(content: String) {
        noteUiState = noteUiState.copy(content = content)
        Log.d("NoteContent", "onContentChange called with value: $content")
    }

    //book ref state
    fun onBookRefChange(bookRef: String) {
        noteUiState = noteUiState.copy(bookRef = bookRef)
    }

    //page ref state
    fun onPageRefChange(pageRef: Int) {
        noteUiState = noteUiState.copy(pageRef = pageRef)
    }

    //add note
    fun addNote() {
        if (hasUser) {
            repository.addNote(
                user_id = user!!.uid,
                title = noteUiState.title,
                content = noteUiState.content,
                book_ref = noteUiState.bookRef,
                page_ref = noteUiState.pageRef,
                timestamp = Timestamp.now()
            ){
                noteUiState = noteUiState.copy(noteAddedStatus = it)
            }
        }
    }

    fun setEditFields(note: Notes) {
        noteUiState = noteUiState.copy(
            title = note.title,
            content = note.content,
            bookRef = note.book_ref,
            pageRef = note.page_ref
        )
    }

    //get selected note
    fun getNote(noteId:String){
        repository.getNote(
            noteId = noteId,
            onError = {

            }
        ){
            noteUiState = noteUiState.copy(selectedNote = it)
            noteUiState.selectedNote?.let { it1 -> setEditFields(it1) }
        }
    }

    //update note
    fun updateNote(noteId: String) {
        if (hasUser && "user_id" == user!!.uid) {
            repository.updateNote(
                noteId = noteId,
                title = noteUiState.title,
                content = noteUiState.content,
                book_ref = noteUiState.bookRef,
                page_ref = noteUiState.pageRef,
                user_id = noteUiState.selectedNote?.user_id!!,
                timestamp = Timestamp.now()
            ){
                noteUiState = noteUiState.copy(updateNoteStatus = it)
            }
        }
    }

    fun resetNoteAddedStatus() {
        noteUiState = noteUiState.copy(noteAddedStatus = false, updateNoteStatus = false)
    }

    fun resetState(){
        noteUiState = NoteUiState()
    }
}

//data class for ui state
data class NoteUiState(
    val title: String = "",
    val content: String = "",
    val bookRef: String = "",
    val pageRef: Int? = null,
    val noteAddedStatus: Boolean = false,
    val updateNoteStatus: Boolean = false,
    val selectedNote: Notes? = null,
    val isLoading: Boolean = false,
    val error: String = ""
)