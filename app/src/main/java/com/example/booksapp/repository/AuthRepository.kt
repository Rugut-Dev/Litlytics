package com.example.booksapp.repository

import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AuthRepository {
    //get current user
    val currentUser:FirebaseUser? = Firebase.auth.currentUser

    //Verify if user is logged in
    fun hasUser():Boolean = Firebase.auth.currentUser != null

    //get current user id
    fun getUserId():String = Firebase.auth.currentUser?.uid.orEmpty()

    //create user
    suspend fun createUser(
        email: String,
        password: String,
        onComplete:(Boolean) -> Unit
    ) = withContext(Dispatchers.IO){
        Firebase.auth
            .createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful){
                    onComplete(true)
                }else{
                    onComplete(false)
                }
            }
    }

    //login user
    suspend fun login(
        email: String,
        password: String,
        onComplete:(Boolean) -> Unit
    ) = withContext(Dispatchers.IO){
        Firebase.auth
            .signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful){
                    onComplete(true)
                }else{
                    onComplete(false)
                }
            }
    }

}



//{
//               if (it.isSuccessful){
//                   onComplete(true)
//            }else{
//                   onComplete(false)
//               }
//            }