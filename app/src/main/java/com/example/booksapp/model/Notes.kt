package com.example.booksapp.model

import com.google.firebase.Timestamp

data class Notes(
    val user_id: String = "",
    val title: String = "",
    val content: String = "",
    val book_ref: String = "",
    val page_ref: Int? = null,
    val timestamp: Timestamp = Timestamp.now(),
    val documentId: String = ""
)
