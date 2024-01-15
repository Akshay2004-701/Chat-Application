package com.example.streamchat.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.streamchat.R
import com.example.streamchat.domain.subscribeToEvents
import com.example.streamchat.ui.theme.StreamChatTheme

@Composable
fun LoginScreen(
    modifier: Modifier = Modifier,
    viewModel: LoginViewmodel = viewModel(factory = LoginViewmodel.factory),
    lfc:LifecycleOwner,
    launchToChannelList:()->Unit
){
    val state = viewModel.state.collectAsState()
    val token = stringResource(id = R.string.jwt_token)
    val ctx = LocalContext.current

    if (state.value.isLoading){
        Box(modifier = modifier.fillMaxSize()) {
            Image(
                painter = painterResource(id = R.drawable.login),
                contentDescription = "login",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.FillBounds
            )

            Column(modifier=Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center) {
                CircularProgressIndicator(
                    modifier = Modifier.padding(16.dp),
                    color = Color.Green)
            }
        }
    }

    else {
        Box(modifier = modifier.fillMaxSize()) {
            Image(
                painter = painterResource(id = R.drawable.login),
                contentDescription = "login",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.FillBounds
            )
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = "Login",
                    color = Color.Green,
                    style = MaterialTheme.typography.displayLarge,
                    modifier = Modifier.padding(top = 64.dp)
                )

                Spacer(modifier = Modifier.height(32.dp))


                OutlinedTextField(
                    value = state.value.username,
                    onValueChange = { viewModel.updateUsername(it) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(10.dp))
                        .padding(16.dp),
                    label = {
                        Text(text = "Username")
                    },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text
                    ),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.Green,
                        unfocusedContainerColor = Color.Green,
                        disabledContainerColor = Color.Green,
                    )
                )

                //user login button
                OutlinedButton(
                    onClick = {
                        subscribeToEvents(lfc,viewModel.loginEvent,ctx,launchToChannelList)
                        viewModel.loginUser(state.value.username, token)
                              },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                        .clip(RoundedCornerShape(15.dp)),
                    colors = ButtonDefaults.outlinedButtonColors(
                        containerColor = Color.Green
                    )
                ) {
                    Text(
                        text = "Login as a User",
                        color = Color.Black,
                        style = MaterialTheme.typography.bodyLarge
                    )
                }

                //guest login button
                OutlinedButton(
                    onClick = {
                        subscribeToEvents(lfc,viewModel.loginEvent,ctx,launchToChannelList)
                        viewModel.loginUser(username = state.value.username)
                              },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                        .clip(RoundedCornerShape(15.dp)),
                    colors = ButtonDefaults.outlinedButtonColors(
                        containerColor = Color.Green
                    )
                ) {
                    Text(
                        text = "Login as a Guest",
                        color = Color.Black,
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }
        }
    }

}

@Preview(showSystemUi = true, showBackground = true)
@Composable
fun LoginScreenPreview(){
StreamChatTheme {
//    LoginScreen()
}
}