package com.example.booksapp.viewModel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.booksapp.model.BookItem
import com.example.booksapp.utils.ViewState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json


class MainViewModel : ViewModel(){
    

    private val _viewState = MutableStateFlow<ViewState>(ViewState.Loading)
    val books = _viewState.asStateFlow()


    //formats Json
    val formatJson = Json {
        ignoreUnknownKeys = true
        prettyPrint = true
        isLenient = true
    }
    //get all books
    @OptIn(ExperimentalSerializationApi::class)
    fun getAllBooks(context: Context) = viewModelScope.launch{
        try {
            //read Json file
            val myJsonString = context.assets.open("Books.json").bufferedReader().use {
                it.readText()
            }
            //convert Json to list
            val bookList = formatJson.decodeFromString<List<BookItem>>(myJsonString)
            _viewState.value = ViewState.Success(bookList)
        }catch (e: Exception){
            _viewState.value = ViewState.Error(e)
        }
    }
}

