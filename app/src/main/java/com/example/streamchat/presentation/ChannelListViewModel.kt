package com.example.streamchat.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.streamchat.ChatApplication
import com.example.streamchat.domain.CreateChannelEvent
import io.getstream.chat.android.client.ChatClient
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import java.util.UUID

class ChannelListViewModel(
    private val client: ChatClient
):ViewModel() {

    private val _createChannelEvent = MutableSharedFlow<CreateChannelEvent>()
    val createChannelEvent = _createChannelEvent.asSharedFlow()

    fun createChannel(channelName:String , channelType:String="messaging"){
        val trimmedCN = channelName.trim()
        val cid = UUID.randomUUID().toString()

        viewModelScope.launch {
            if (trimmedCN.isEmpty()){
                _createChannelEvent.emit(
                    CreateChannelEvent.Error("Channel name cannot be empty")
                )
                return@launch
            }
            client.createChannel(
                channelType = channelType,
                channelId = cid,
                memberIds = listOf("admin1"),
                extraData = mapOf(
                    "name" to trimmedCN,
                    "image" to "https://bit.ly/2TIt8NR"
                )
            ).enqueue{
                if (it.isSuccess){
                    viewModelScope.launch {
                        _createChannelEvent.emit(
                            CreateChannelEvent.Success
                        )
                    }
                }
                else{
                    viewModelScope.launch {
                        _createChannelEvent.emit(
                            CreateChannelEvent.Error(it.errorOrNull()?.message?:"Unknown error")
                        )
                    }
                }
            }
        }

    }

    fun logout(){
        viewModelScope.launch {
            client.disconnect(false)
        }
    }

    companion object{
        val Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as ChatApplication)
                ChannelListViewModel(application.chatClient)
            }
        }
    }
}