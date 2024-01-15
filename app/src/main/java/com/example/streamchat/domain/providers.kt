package com.example.streamchat.domain

import android.content.Context
import com.example.streamchat.R
import io.getstream.chat.android.client.ChatClient
import io.getstream.chat.android.client.logger.ChatLogLevel
import io.getstream.chat.android.offline.plugin.factory.StreamOfflinePluginFactory
import io.getstream.chat.android.state.plugin.config.StatePluginConfig
import io.getstream.chat.android.state.plugin.factory.StreamStatePluginFactory


fun provideOfflineStreamPlugin(ctx: Context): StreamOfflinePluginFactory {
    return StreamOfflinePluginFactory(
        appContext = ctx
    )
}
fun provideStatePlugin(ctx: Context): StreamStatePluginFactory {
    return StreamStatePluginFactory(
        config = StatePluginConfig(
            backgroundSyncEnabled = true,
            userPresence = true
        ),
        ctx
    )
}
fun chatClientProvider(
    context: Context,
    streamOfflinePluginFactory: StreamOfflinePluginFactory,
    streamStatePluginFactory: StreamStatePluginFactory
): ChatClient {
    return ChatClient.Builder(context.getString(R.string.api_key),context)
        .withPlugins(streamOfflinePluginFactory,streamStatePluginFactory)
        .logLevel(ChatLogLevel.ALL)
        .build()
}