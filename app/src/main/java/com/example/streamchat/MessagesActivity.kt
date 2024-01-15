package com.example.streamchat

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import io.getstream.chat.android.compose.ui.messages.MessagesScreen
import io.getstream.chat.android.compose.ui.theme.ChatTheme
import io.getstream.chat.android.compose.viewmodel.messages.MessagesViewModelFactory

class MessagesActivity:ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val channelId = intent.getStringExtra(KEY_CHANNEL_ID)
        if (channelId == null){
            finish()
            return
        }

        setContent {
            ChatTheme(isInDarkMode = true) {
                MessagesScreen(
                    viewModelFactory = MessagesViewModelFactory(
                        applicationContext,
                        channelId = channelId
                    ),
                    onBackPressed = {finish()}
                )
            }
        }
    }
    companion object{
        private const val KEY_CHANNEL_ID = "ChannelId"
        fun getIntent(context: Context , channelId:String): Intent {
            return Intent(context , MessagesActivity::class.java).putExtra(
                KEY_CHANNEL_ID , channelId
            )
        }
    }
}