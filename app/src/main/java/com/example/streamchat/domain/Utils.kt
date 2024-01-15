package com.example.streamchat.domain

import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import com.example.streamchat.ChannelListActivity
import kotlinx.coroutines.flow.SharedFlow

object Utils {
    const val MIN_USERNAME_LENGTH = 3
}

sealed class LoginEvent{
    data object ErrorInputTooShort:LoginEvent()
    data class ErrorLogin(val error:String):LoginEvent()
    data object Success:LoginEvent()
}

fun subscribeToEvents(
    lifecycleScope:LifecycleOwner,
    loginEvent:SharedFlow<LoginEvent>,
    ctx:Context,
    launchToChannelList:()->Unit
){
    lifecycleScope.lifecycleScope.launchWhenStarted {
        loginEvent.collect{loginEvent->
            when(loginEvent){
                is LoginEvent.ErrorInputTooShort->{
                    Toast.makeText(
                        ctx,
                        "User name should be more than 3 characters",
                        Toast.LENGTH_LONG
                    ).show()
                }
                is LoginEvent.ErrorLogin->{
                    Toast.makeText(
                        ctx,
                        loginEvent.error,
                        Toast.LENGTH_LONG
                    ).show()
                }
                is LoginEvent.Success->{
                    Toast.makeText(
                        ctx,
                        "Login successful",
                        Toast.LENGTH_LONG
                    ).show()
                    launchToChannelList()
                }
            }
        }
    }
}
sealed class CreateChannelEvent {
    data class Error(val error:String):CreateChannelEvent()
    data object Success:CreateChannelEvent()
}