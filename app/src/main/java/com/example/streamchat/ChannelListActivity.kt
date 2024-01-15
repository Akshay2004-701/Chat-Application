package com.example.streamchat

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.lifecycleScope
import com.example.streamchat.domain.CreateChannelEvent
import com.example.streamchat.presentation.ChannelDialog
import com.example.streamchat.presentation.ChannelListViewModel
import io.getstream.chat.android.compose.ui.channels.ChannelsScreen
import io.getstream.chat.android.compose.ui.theme.ChatTheme
import kotlinx.coroutines.launch

class ChannelListActivity:ComponentActivity() {
    private val viewModel:ChannelListViewModel by viewModels(factoryProducer = {ChannelListViewModel.Factory})

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        events()
        setContent {

            var showDialog:Boolean by remember {
                mutableStateOf(false)
            }

            if (showDialog){
                ChannelDialog(dismiss = {
                    viewModel.createChannel(it)
                    showDialog = false
                })
            }

            ChatTheme(isInDarkMode = true){
                ChannelsScreen(
                    title = "Channel List",
                    isShowingSearch = true,
                    onItemClick = {channel->
                    startActivity(MessagesActivity.getIntent(applicationContext,channel.cid))
                    },
                    onBackPressed = { finish() },
                    onHeaderActionClick = {
                     showDialog = true
                    },
                    onHeaderAvatarClick = {
                        viewModel.logout()
                        finish()
                        val intent = Intent(this,MainActivity::class.java)
                        startActivity(intent)
                    }
                )
            }
        }
    }
    private fun events(){
        lifecycleScope.launch{
           viewModel.createChannelEvent.collect{
               when(it){
                   is CreateChannelEvent.Success->{
                       Toast.makeText(
                           applicationContext,
                           "Channel created successfully",
                           Toast.LENGTH_LONG
                       ).show()
                   }
                   is CreateChannelEvent.Error->{
                       Toast.makeText(
                           applicationContext,
                           it.error,
                           Toast.LENGTH_LONG
                       ).show()
                   }
               }
           }
        }
    }
}