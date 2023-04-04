package com.example.booksapp.repository

import android.util.Log
import com.example.booksapp.model.Notes
import com.google.firebase.Timestamp
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

const val NOTES_COLLECTION_REF = "notes"
const val BOOKS_COLLECTION_REF = "books"

class StorageRepository {
    //get current user
    val user = Firebase.auth.currentUser
    fun hasUser(): Boolean = Firebase.auth.currentUser != null

    //get current user id
    fun getUserId(): String = Firebase.auth.currentUser?.uid.orEmpty()
    val usId = getUserId()

    //notes collection reference
    val notesRef: CollectionReference = Firebase
        .firestore.collection(NOTES_COLLECTION_REF)

    //books collection reference
    private val booksRef: CollectionReference = Firebase
        .firestore.collection(BOOKS_COLLECTION_REF)


    //get User Notes for particular book
    fun getUserNotes(
        isbnNo: String
    ): Flow<Resources<List<Notes>>> = callbackFlow {
        var snapShotStateListener: ListenerRegistration? = null

        try {
            snapShotStateListener = notesRef
                .orderBy("page_ref")
                .whereEqualTo("user_id", user)
                .whereEqualTo("book_ref", isbnNo)
                .addSnapshotListener { snapshot, e ->
                    val response = if (snapshot != null) {
                        val notes = snapshot.toObjects(Notes::class.java)
                        Resources.Success(data = notes)
                    } else {
                        Resources.Error(throwable = e?.cause)
                    }
                    trySend(response)

                }
        } catch (e: Exception) {
            trySend(Resources.Error(e.cause))
            e.printStackTrace()
        }

        awaitClose {
            snapShotStateListener?.remove()
        }
    }

    //get all notes for particular book
    fun getAllNotes(isbnNo: String): Flow<List<Notes>> = callbackFlow {
        var snapShotStateListener: ListenerRegistration? = null

        try {
            trySend(
                emptyList<Notes>()
            ).isSuccess
            snapShotStateListener = notesRef
                .orderBy("page_ref")
                .whereEqualTo("book_ref", isbnNo)
                .addSnapshotListener { snapshot, e ->
                   if (snapshot != null) {
                        //print out isbnNo in this function to logcat
                        Log .d("notey", "found")
                        val notes = snapshot.toObjects(Notes::class.java)
                        trySend(notes).isSuccess
                    }
                }
        } catch (e: Exception) {
            trySend(emptyList<Notes>()).isSuccess
            e.printStackTrace()
        }

        awaitClose {
            snapShotStateListener?.remove()
        }
    }

    /**
    fun getAllNotes(isbn: String, isbnNo: String): Flow<NotesUiState> = callbackFlow {
        var snapShotStateListener: ListenerRegistration? = null

        try {
            trySend(NotesUiState.Loading).isSuccess

            snapShotStateListener = notesRef
                .orderBy("page_ref")
                .whereEqualTo("book_ref", isbnNo)
                .addSnapshotListener { snapshot, e ->
                    val response = if (snapshot != null) {
                        //print out isbnNo in this function to logcat
                        val notes = snapshot.toObjects(Notes::class.java)
                        NotesUiState.Success(notes)
                    } else {
                        NotesUiState.Error(e?.cause)
                    }
                    trySend(response).isSuccess
                }
        } catch (e: Exception) {
            trySend(NotesUiState.Error(e.cause)).isSuccess
            e.printStackTrace()
        }

        awaitClose {
            snapShotStateListener?.remove()
        }
    }

**/

    //get note by id
    fun getNote(
        noteId: String,
        onError: (Throwable?) -> Unit,
        onSuccess: (Notes?) -> Unit
    ) {
        notesRef
            .document(noteId)
            .get()
            .addOnSuccessListener {
                onSuccess.invoke(it.toObject(Notes::class.java))
            }
            .addOnFailureListener { result ->
                onError.invoke(result.cause)

            }
    }

    //add note
    fun addNote(
        user_id: String,
        title: String,
        content: String,
        timestamp: Timestamp,
        book_ref: String,
        page_ref: Int?,
        onComplete: (Boolean) -> Unit,
    ) {
        val documentId = notesRef.document().id
        val note = Notes(
            user_id, title, content, book_ref, page_ref, timestamp, documentId
        )
        notesRef
            .document(documentId)
            .set(note)
            .addOnCompleteListener { result ->
                onComplete.invoke(result.isSuccessful)
            }
    }

    //delete note if user is owner
    fun deleteNote(
        noteId: String,
        onComplete: (Boolean, String?) -> Unit,
    ) {
        notesRef
            .document(noteId)
            .get()
            .addOnSuccessListener { documentSnapshot ->
                val note = documentSnapshot.toObject(Notes::class.java)
                if (note != null) {
                    val userId = note.user_id
                    if(userId == usId) {
                        Log.d("StorageRepository userId", "deleteNote: $userId")
                        Log .d("usId value:","val: $usId")
                        notesRef
                            .document(noteId)
                            .delete()
                            .addOnCompleteListener { result ->
                                onComplete.invoke(result.isSuccessful, userId)
                            }
                    }
                } else {
                    onComplete.invoke(false, null)
                }
            }
    }



    //update note if user is owner
    fun updateNote(
        noteId: String,
        user_id: String,
        title: String,
        content: String,
        timestamp: Timestamp,
        book_ref: String,
        page_ref: Int?,
        onComplete: (Boolean) -> Unit,
    ) {
        notesRef
            .document(noteId)
            .get()
            .addOnSuccessListener {
                val note = it.toObject(Notes::class.java)
                //check if user is owner
                if (note?.user_id == user_id)
                {
                    val updatedNote = Notes(
                        user_id, title, content, book_ref, page_ref, timestamp, noteId
                    )
                    notesRef
                        .document(noteId)
                        .set(updatedNote)
                        .addOnCompleteListener { result ->
                            onComplete.invoke(result.isSuccessful)
                        }
                }
            }
    }
    //sign out
    fun signOut() = Firebase.auth.signOut()

}
//sealed class for retrieving data from firestore
sealed class Resources<T>(
    val data: T? = null,
    val throwable: Throwable? = null,
){
    class Success<T>(data: T): Resources<T>(data = data)
    class Error<T>(throwable: Throwable?): Resources<T>(throwable = throwable)
    class Loading<T>: Resources<T>()
}