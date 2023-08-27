package com.example.whatsapp.ui.theme

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale.Companion.Fit
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.whatsapp.CViewModel
import com.example.whatsapp.utilities.CommonDivider
import com.example.whatsapp.utilities.CommonImage
import com.example.whatsapp.DestinationScreen
import com.example.whatsapp.R
import com.example.whatsapp.data.Message
import com.example.whatsapp.utilities.navigateTo
import kotlinx.coroutines.launch

@Composable
fun personalChatScreen (navController: NavController, vm: CViewModel, chatId: String){
    LaunchedEffect(key1 = Unit) {
        vm.populateChat(chatId)
    }
    BackHandler {
        vm.depopulateChat()
    }

    var reply by rememberSaveable { mutableStateOf("") }
    val currentChat = vm.chats.value.first { it.chatId == chatId }
    val myId = vm.userData.value
    val chatUser = if (myId?.userId == currentChat.user1.userId) currentChat.user2
    else currentChat.user1
    val onSendReply = {
        vm.onSendReply(chatId, reply)
        reply = ""
    }
    val chatMessages = vm.chatMessages
    Column(modifier = Modifier.fillMaxSize()) {
        ChatHeader(name = chatUser.number ?: "", imageUrl = chatUser.imageUrl ?: "") { // name nmber karııklğın dolay yanış geliyor
            navController.popBackStack()
            vm.depopulateChat()
        }
        Messages(
            modifier = Modifier.weight(1f),
            chatMessages = chatMessages.value,
            currentUserId = myId?.userId ?: ""
        )
        ReplyBox(reply = reply, onReplyChange = { reply = it }, onSendReply = onSendReply, navController = navController)
    }
}
@Composable
fun ChatHeader(name: String, imageUrl: String, onBackClicked: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            Icons.Rounded.ArrowBack,
            contentDescription = null,
            modifier = Modifier
                .clickable { onBackClicked.invoke() }
                .padding(8.dp)
        )
        CommonImage(
            data = imageUrl,
            modifier = Modifier
                .padding(8.dp)
                .size(50.dp)
                .clip(CircleShape), contentScale = Fit
        )
        Text(
            text = name,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(start = 4.dp)
        )
    }
    CommonDivider()
}
@Composable
fun Messages(modifier: Modifier, chatMessages: List<Message>, currentUserId: String) {
    LazyColumn(modifier = modifier) {
        items(chatMessages) { msg ->
            msg.message?.let {
                val alignment = if (msg.sentBy == currentUserId) Alignment.End
                else Alignment.Start
                val color = if (msg.sentBy == currentUserId) Color(0xFF68C400)
                else Color(0xFFC0C0C0)

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    horizontalAlignment = alignment
                ) {
                    Text(
                        text = msg.message,
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(color)
                            .padding(12.dp),
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReplyBox(navController: NavController,reply: String, onReplyChange: (String) -> Unit, onSendReply: () -> Unit) {
    val scope = rememberCoroutineScope()
    var showDialog by remember { mutableStateOf(false) }
    Column(modifier = Modifier.fillMaxWidth()) {
        CommonDivider()
        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            IconButton(onClick = { showDialog = true  })
            {
                Icon(
                    painter = painterResource(id = R.drawable.baseline_view_headline_24),
                    contentDescription = "test",
                    modifier = Modifier.size(24.dp)
                )
            }
            TextField(value = reply, onValueChange = onReplyChange, maxLines = 3, modifier = Modifier.width(256.dp))
            Button(onClick = onSendReply) {
                Text(text = "Send")
            }
        }
    }
    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text(text = "Chat Extensions") },
            confirmButton = {
                IconButton(onClick = { navigateTo(navController, DestinationScreen.CameraScreen.route)  })
                {
                    Icon(
                        painter = painterResource(id = R.drawable.baseline_camera_alt_24),
                        contentDescription = "test",
                        modifier = Modifier.size(24.dp)
                    )
                   // Text("Camera")
                }
            },
            dismissButton = {
                IconButton(onClick = { navigateTo(navController, DestinationScreen.MapScreen.route)  })
                {
                    Icon(
                        painter = painterResource(id = R.drawable.baseline_navigation_24),
                        contentDescription = "test",
                        modifier = Modifier.size(24.dp)
                    )
                    //Text("Location")
                }
            },
        )
    }
}
