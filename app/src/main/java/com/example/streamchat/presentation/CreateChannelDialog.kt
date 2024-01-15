package com.example.streamchat.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.streamchat.ui.theme.StreamChatTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChannelDialog(
    modifier: Modifier = Modifier,
    dismiss:(String)->Unit
){
    var channelName by remember {
        mutableStateOf("")
    }

    AlertDialog(onDismissRequest = { dismiss(channelName) }){
        Column(
            modifier = modifier.padding(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "Enter channel name", color = Color.White)

            TextField(
                value = channelName,
                onValueChange = {channelName=it},
                label = {
                        Text(text = "Channel Name")
                },
                modifier=Modifier.fillMaxWidth())

            Button(onClick = { dismiss(channelName) } ,
                modifier=Modifier.fillMaxWidth()) {
                Text(text = "Create Channel")
            }
        }
    }
}
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun DialogPreview(){
    StreamChatTheme {
//        ChannelDialog()
    }
}