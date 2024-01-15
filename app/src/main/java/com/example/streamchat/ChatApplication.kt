package com.example.streamchat

import android.app.Application
import com.example.streamchat.domain.chatClientProvider
import com.example.streamchat.domain.provideOfflineStreamPlugin
import com.example.streamchat.domain.provideStatePlugin
import io.getstream.chat.android.client.ChatClient
import io.getstream.chat.android.offline.plugin.factory.StreamOfflinePluginFactory
import io.getstream.chat.android.state.plugin.factory.StreamStatePluginFactory

class ChatApplication:Application() {
     lateinit var streamOfflinePluginFactory: StreamOfflinePluginFactory
     lateinit var streamStatePluginFactory: StreamStatePluginFactory
     lateinit var chatClient: ChatClient

    override fun onCreate() {
        super.onCreate()
        streamOfflinePluginFactory = provideOfflineStreamPlugin(this)
        streamStatePluginFactory = provideStatePlugin(this)
        chatClient = chatClientProvider(
            this,
            streamOfflinePluginFactory,
            streamStatePluginFactory
        )
    }
}