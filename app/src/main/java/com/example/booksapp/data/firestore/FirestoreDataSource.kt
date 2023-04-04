package com.example.booksapp.data.firestore

import android.content.Context
import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.BufferedReader
import java.io.InputStreamReader

@Serializable
data class Book(
    val title: String,
    val isbn: String
)

@OptIn(ExperimentalSerializationApi::class)
fun populateBooksCollection(context: Context) {
    val db = FirebaseFirestore.getInstance()
    val formatJson = Json {
        ignoreUnknownKeys = true
        prettyPrint = true
        isLenient = true
    }
    val formattedBooks = mutableListOf<Book>()

    try {
        // Read Books.json from assets folder
        val inputStream = context.assets.open("Books.json")
        val bufferedReader = BufferedReader(InputStreamReader(inputStream))
        val jsonString = bufferedReader.readText()

        // Format the JSON and write to a new file
        val books = formatJson.decodeFromString<List<Book>>(jsonString)
        val formattedJson = formatJson.encodeToString(books)
        context.openFileOutput("formattedBooks.json", Context.MODE_PRIVATE).use {
            it.write(formattedJson.toByteArray())
        }

        // Read the formatted JSON and populate the books collection
        val formattedInputStream = context.openFileInput("formattedBooks.json")
        val formattedBufferedReader = BufferedReader(InputStreamReader(formattedInputStream))
        val formattedJsonString = formattedBufferedReader.readText()
        formattedBooks.addAll(formatJson.decodeFromString<List<Book>>(formattedJsonString))

        // Add each book to the books collection
        for (book in formattedBooks) {
            val bookData = hashMapOf(
                "title" to book.title,
                "isbn" to book.isbn
            )
            db.collection("books").document(book.isbn).set(bookData)
                .addOnSuccessListener {
                   // Log.d(TAG, "Book added: ${book.title}")
                }
                .addOnFailureListener { e ->
                    Log.e(TAG, "Error adding book: ${book.title}", e)
                }
        }
    } catch (e: Exception) {
        Log.e(TAG, "Error populating books collection", e)
    }
}

private const val TAG = "MainActivity"

/**
//script for firebase collection
class FirestoreDataSource {
companion object {
fun populateBooksCollection(context: Context) {

val formatJson = Json {
ignoreUnknownKeys = true
prettyPrint = true
isLenient = true
}

val firestore = FirebaseFirestore.getInstance()
val booksJson = context.assets.open("Books.json").bufferedReader().use { it.readText() }
val books = formatJson.(JSONObject(booksJson).getJSONArray("books"))
//JSONObject(booksJson).getJSONArray("books")

for (i in 0 until books.length()) {
val book = books.getJSONObject(i)
val id = book.getString("isbn")
val title = book.getString("title")
val author = book.getString("author")
val coverImage = book.getString("cover_image")

val bookData = hashMapOf(
"title" to title,
"isbn" to id,
"author" to author,
"cover_image" to coverImage
// Add any other fields you want to store for each book
)

firestore.collection("books").document(id)
.set(bookData)
.addOnSuccessListener {
Log.d(TAG, "Added book with ID: $id")
}
.addOnFailureListener { e ->
Log.e(TAG, "Error adding book with ID: $id", e)
}
}
}
}
}
**/

/**
val firestore = FirebaseFirestore.getInstance()
val booksJson = context.assets.open("books.json").bufferedReader().use { it.readText() }
val books = JSONObject(booksJson).getJSONArray("books")

for (i in 0 until books.length()) {
    val book = books.getJSONObject(i)
    val id = book.getString("id")
    val title = book.getString("title")
    val author = book.getString("author")
    val coverImage = book.getString("cover_image")

    val bookData = hashMapOf(
        "title" to title,
        "author" to author,
        "cover_image" to coverImage
        // Add any other fields you want to store for each book
    )

    firestore.collection("books").document(id)
        .set(bookData)
        .addOnSuccessListener {
            Log.d(TAG, "Added book with ID: $id")
        }
        .addOnFailureListener { e ->
            Log.e(TAG, "Error adding book with ID: $id", e)
        }
}
        **/