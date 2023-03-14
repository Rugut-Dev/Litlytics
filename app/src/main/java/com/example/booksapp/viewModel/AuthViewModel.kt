package com.example.booksapp.viewModel

import android.content.Context
import android.widget.Toast
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.booksapp.repository.AuthRepository
import kotlinx.coroutines.launch

class AuthViewModel(
    //get repository
    private val repository: AuthRepository = AuthRepository()
): ViewModel(){

    //get current user
    val currentUser = repository.currentUser

    //Verify if user is logged in
    val hasUser:Boolean
        get() = repository.hasUser()

    var regUiState by mutableStateOf(RegUiState())
        private set

    //change userName state
    fun onUserNameChange(userName:String){
        regUiState = regUiState.copy(userName = userName)
    }

    //change password state
    fun onPasswordChange(password:String){
        regUiState = regUiState.copy(password = password)
    }

    //change userNameSignUp state
    fun onUserNameSignUpChange(userNameSignUp:String){
        regUiState = regUiState.copy(userNameSignUp = userNameSignUp)
    }

    //change passwordSignUp state
    fun onPasswordSignUpChange(passwordSignUp:String){
        regUiState = regUiState.copy(passwordSignUp = passwordSignUp)
    }

    //change confirmPassword state
    fun onConfirmPasswordChange(confirmPassword:String){
        regUiState = regUiState.copy(confirmPassword = confirmPassword)
    }

    //validate login
    private fun validateLogin():Boolean{
        return regUiState.userName.isNotEmpty() && regUiState.password.isNotEmpty()
    }
    //validate signUp
    private fun validateSignUp():Boolean{
        return regUiState.userNameSignUp.isNotEmpty() && regUiState.passwordSignUp.isNotEmpty() && regUiState.confirmPassword.isNotEmpty()
    }

    //create user
    fun createUser(context: Context) = viewModelScope.launch {
        try {
            if(!validateSignUp()){
                throw IllegalArgumentException("Please fill all fields")
            }
            //loading
            regUiState = regUiState.copy(isLoading = true)
            //check if password and confirm password are the same
            if (regUiState.passwordSignUp != regUiState.confirmPassword){
                throw IllegalArgumentException("Password and confirm password must be the same")
            }
            regUiState = regUiState.copy(signUpError = null)

            //create user
            repository.createUser(
                regUiState.userNameSignUp,
                regUiState.passwordSignUp
            ){isSuccess->
                if (isSuccess){
                    Toast.makeText(context, "Bingo!!", Toast.LENGTH_SHORT).show()
                    regUiState = regUiState.copy(isSuccess = true)
                }else{
                    Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show()
                    regUiState = regUiState.copy(isSuccess = false)
                }
            }
        }catch (e:Exception){
            regUiState = regUiState.copy(signUpError = e.message)
        }
        //finally {
        //  regUiState = regUiState.copy(isLoading = false)
        //}
    }

    //login user
    fun loginUser(context: Context) = viewModelScope.launch {
        try {
            if(!validateLogin()){
                throw IllegalArgumentException("Please fill all fields")
            }
            //loading
            regUiState = regUiState.copy(isLoading = true)
            //login user
            repository.login(
                regUiState.userName,
                regUiState.password
            ){isSuccess->
                if (isSuccess){
                    Toast.makeText(context, "Bingo!!", Toast.LENGTH_SHORT).show()
                    regUiState = regUiState.copy(isSuccess = true)
                }else{
                    Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show()
                    regUiState = regUiState.copy(isSuccess = false)
                }
            }
        }catch (e:Exception){
            regUiState = regUiState.copy(loginError = e.message)
        }//finally {
        // regUiState = regUiState.copy(isLoading = false)
        //}
    }
}

//viewStates
data class RegUiState(
    val userName:String = "",
    val password:String = "",
    val userNameSignUp:String = "",
    val passwordSignUp:String = "",
    val confirmPassword:String = "",
    val isLoading:Boolean = false,
    val isSuccess:Boolean = false,
    val signUpError:String? = null,
    val loginError:String? = null
)