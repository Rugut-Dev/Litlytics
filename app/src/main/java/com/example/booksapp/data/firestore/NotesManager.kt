package com.example.booksapp.data.firestore



/**
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import java.util.*

data class Note(
    val title: String = "",
    val content: String = "",
    val book_ref: String = "",
    val page_ref: Int? = null,
    val timestamp: Timestamp? = null
)

class NotesRepository {

    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()

    fun addNote(
        title: String,
        content: String,
        bookRef: String,
        pageRef: String
    ) {
        val note = Note(
            title = title,
            content = content,
            bookRef = bookRef,
            pageRef = pageRef,
            timestamp = Date().time
        )
        firestore.collection("notes").add(note)
    }

    fun updateNote(
        noteId: String,
        title: String,
        content: String,
        bookRef: String,
        pageRef: String
    ) {
        val note = Note(
            title = title,
            content = content,
            bookRef = bookRef,
            pageRef = pageRef,
            timestamp = Date().time
        )
        firestore.collection("notes").document(noteId).set(note)
    }

    fun deleteNote(
        noteId: String
    ) {
        firestore.collection("notes").document(noteId).delete()
    }

    fun getNotesForBook(
        bookRef: String,
        onResult: (List<Note>) -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        firestore.collection("notes")
            .whereEqualTo("bookRef", bookRef)
            .get()
            .addOnSuccessListener { querySnapshot ->
                onResult.invoke(querySnapshot.toNotes())
            }
            .addOnFailureListener { exception ->
                onFailure.invoke(exception)
            }
    }

    private fun QuerySnapshot.toNotes(): List<Note> {
        return documents.mapNotNull { document ->
            document.toObject(Note::class.java).apply {
                this.id = document.id
            }
        }
    }

    data class Note(
        var id: String = "",
        val title: String = "",
        val content: String = "",
        val bookRef: String = "",
        val pageRef: String = "",
        val timestamp: Long = 0
    )
}
**/