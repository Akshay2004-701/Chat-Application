package com.example.streamchat.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.streamchat.ChatApplication
import com.example.streamchat.domain.LoginEvent
import com.example.streamchat.domain.LoginState
import com.example.streamchat.domain.Utils
import io.getstream.chat.android.client.ChatClient
import io.getstream.chat.android.models.User
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LoginViewmodel(
    private val chatClient: ChatClient
) : ViewModel(){
    private var _state = MutableStateFlow(LoginState())
    val state  = _state.asStateFlow()

    private val _loginEvent = MutableSharedFlow<LoginEvent>()
    val loginEvent = _loginEvent.asSharedFlow()

    fun updateUsername(un:String){
        _state.update {
            it.copy(username = un)
        }
    }

    private fun isValidUserName():Boolean{
        return state.value.username.length > Utils.MIN_USERNAME_LENGTH
    }

    fun loginUser(username:String , token:String? = null){
        val trimmedUsername = username.trim()
        _state.update { it.copy(username = trimmedUsername) }
        viewModelScope.launch{
            //if the person is a registered user
            if (isValidUserName() && token != null) {
                loginRegisteredUser(username,token)
            }
            //if a person is a guest user
            else if (isValidUserName() && token == null) {
                loginGuestUser(username)
            }
            //else emit an error
            else {
                _loginEvent.emit(LoginEvent.ErrorInputTooShort)
            }
        }
    }

    private fun loginRegisteredUser(un:String, token:String){
        val user = User(id = un , name = un)
        chatClient.connectUser(
            user=user,
            token=token
        ).enqueue{result->
            // this result comes from a retrofit client
            if (result.isSuccess){
                viewModelScope.launch {
                    _loginEvent.emit(LoginEvent.Success)
                }
            }
            else{
                viewModelScope.launch {
                    _loginEvent.emit(LoginEvent.ErrorLogin(
                        result.errorOrNull()?.message?:"Unknown error occurred"
                    ))
                }
            }
        }
    }

    private fun loginGuestUser(un:String){
        chatClient.connectGuestUser(
            userId = un, username = un
        ).enqueue{
            if (it.isSuccess){
                viewModelScope.launch {
                    _loginEvent.emit(LoginEvent.Success)
                }
            }
            else{
                viewModelScope.launch {
                    _loginEvent.emit(LoginEvent.ErrorLogin(
                        it.errorOrNull()?.message?:"Unknown error occurred"
                    ))
                }
            }
        }
    }



    companion object{
        val factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as ChatApplication)
                LoginViewmodel(application.chatClient)
            }
        }
    }
}
