package com.example.booksapp.app

import android.app.Application
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.HiltAndroidApp


@HiltAndroidApp
class BooksApp: Application() {
    override fun onCreate() {
        super.onCreate()
        // Initialize Firestore
        FirebaseFirestore.getInstance()
    }
}
