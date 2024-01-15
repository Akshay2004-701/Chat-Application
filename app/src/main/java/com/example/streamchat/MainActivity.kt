package com.example.streamchat

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.LifecycleOwner
import com.example.streamchat.presentation.LoginScreen
import com.example.streamchat.ui.theme.StreamChatTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val lifecycleOwner:LifecycleOwner = LocalLifecycleOwner.current
            StreamChatTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    LoginScreen(
                        lfc = lifecycleOwner,
                    ){
                        val intent = Intent(applicationContext,ChannelListActivity::class.java)
                        startActivity(intent)
                    }
                }
            }
        }
    }

}



