import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.whatsapp.CViewModel
import com.example.whatsapp.DestinationScreen
import com.example.whatsapp.R
import com.example.whatsapp.TitleText
import com.example.whatsapp.navigateTo
import com.example.whatsapp.ui.theme.BottomNavigation
import com.example.whatsapp.ui.theme.BottomNavigationMenu
//import com.example.whatsapp.ui.theme.CommonProgressSpinner
//COMMON PRGRES SPINNER I YAZ
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatListScreen(navController: NavController, vm: CViewModel) {
    val inProgress = vm.inProgressChats.value
    if (inProgress)
        //CommonProgressSpinner()
    else {
        val chats = vm.chats.value
        val userData = vm.userData.value

        val showDialog = remember { mutableStateOf(false) }
        val onFabClick: () -> Unit = { showDialog.value = true }
        val onDismiss: () -> Unit = { showDialog.value = false }
        var isSearched by remember { mutableStateOf(false) }
        var tf by remember { mutableStateOf(TextFieldValue("")) }
        val onAddChat: (String) -> Unit = {
            vm.onAddChat(it)
            showDialog.value = false
        }
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                            if (isSearched) {
                                Log.e("if", "if")
                                BasicTextField(
                                    value = tf,
                                    onValueChange = {
                                        tf = it;if (it.text.isEmpty()) {
                                        isSearched = false
                                    }
                                    },
                                    textStyle = LocalTextStyle.current.copy(color = Color.Black),
                                    singleLine = true,
                                    decorationBox = { innerTextField ->
                                        Row(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(horizontal = 8.dp),
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Icon(
                                                painter = painterResource(id = R.drawable.baseline_search_24),
                                                contentDescription = "Search",
                                                modifier = Modifier.size(24.dp)
                                            )
                                            Spacer(modifier = Modifier.width(8.dp))
                                            innerTextField()
                                            Spacer(modifier = Modifier.width(154.dp))

                                            Column(
                                                horizontalAlignment = Alignment.End
                                            ) {
                                                IconButton(onClick = { isSearched = false })
                                                {
                                                    Icon(
                                                        painter = painterResource(id = R.drawable.baseline_cancel_24),
                                                        contentDescription = "test",
                                                        modifier = Modifier.size(24.dp)
                                                    )
                                                }
                                            }
                                        }
                                    }
                                )
                            } else {

                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.Start
                                ) {
                                    Text(
                                        text = "Chats",
                                        style = TextStyle(
                                            fontSize = 35.sp,
                                            fontWeight = FontWeight.ExtraBold
                                        )
                                    )
                                    Spacer(modifier = Modifier.width(220.dp))
                                    Column(
                                        horizontalAlignment = Alignment.End
                                    ) {
                                        Button(
                                            onClick = { isSearched = true },
                                            colors = ButtonDefaults.buttonColors(containerColor = Color.DarkGray)
                                        )
                                        {
                                            Icon(
                                                painter = painterResource(id = R.drawable.baseline_search_24),
                                                contentDescription = "Search",
                                                modifier = Modifier.size(24.dp)
                                            )
                                        }
                                    }
                                }
                            }
                    })
                    },
            /*
            content = {
                Log.e(chats.toString(),chats.toString())
                if (chats.isEmpty()) {
                    Log.e("chats", chats.toString())
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp) // Add some padding to the column for visibility
                            .background(Color.LightGray), // Add a background color for visibility
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Bottom
                    ) {
                            Text(text = "No chats available")
                    }
                }
                    else {
                        LazyColumn(modifier = Modifier.padding(1.dp)) {
                            items(chats) { chat ->
                                val chatUser = if (chat.user1.userId == userData?.userId) chat.user2
                                else chat.user1
                                CommonRow(
                                    imageUrl = chatUser.imageUrl ?: "",
                                    name = chatUser.name ?: "---"
                                ) {
                                    chat.chatId?.let {id ->
                                        navigateTo(
                                            navController,
                                            DestinationScreen.PersonalChat.createRoute(id)
                                        )
                                    }
                                }
                            }
                        }
                    }
            },
             */
            content = {
                Log.e("chats", chats.toString())
                Box(
                    modifier = Modifier
                        .fillMaxSize()
//                        .padding(16.dp) // Add some padding to the column for visibility
                ) {
                    if (chats.isEmpty()) {
                        Log.e("chats", chats.toString())
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(Color.LightGray), // Add a background color for visibility
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Text(text = "No chats available")
                        }
                    } else {
                        LazyColumn(modifier = Modifier.padding(1.dp)) {
                            items(chats) { chat ->
                                val chatUser = if (chat.user1.userId == userData?.userId) chat.user2
                                else chat.user1
                                CommonRow(
                                    imageUrl = chatUser.imageUrl ?: "",
                                    name = chatUser.name ?: "---"
                                ) {
                                    chat.chatId?.let { id ->
                                        navigateTo(
                                            navController,
                                            DestinationScreen.PersonalChat.createRoute(id)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            },

                    floatingActionButton = { FAB(showDialog.value, onFabClick, onDismiss, onAddChat) },
                    bottomBar = {
                BottomNavigationMenu(selectedItem = BottomNavigation.CHATLIST, navController = navController)
            }

        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FAB(
    showDialog: Boolean,
    onFabClick: () -> Unit,
    onDismiss: () -> Unit,
    onAddChat: (String) -> Unit
) {

    val addChatNumber = remember { mutableStateOf("") }

    if (showDialog)
        AlertDialog(
            onDismissRequest = onDismiss,
            confirmButton = {
                Button(onClick = { onAddChat(addChatNumber.value) }) {
                    Text(text = "Add chat")
                }
            },
            title = { Text(text = "Add chat") },
            text = {
                OutlinedTextField(
                    value = addChatNumber.value,
                    onValueChange = { addChatNumber.value = it },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
            }
        )

    FloatingActionButton(
        onClick = onFabClick,
        containerColor = MaterialTheme.colorScheme.secondary,
        shape = CircleShape,
        modifier = Modifier.padding(bottom = 40.dp)
    ) {
        Icon(
            imageVector = Icons.Rounded.Add,
            contentDescription = "Add chat",
            tint = Color.White,
        )
    }
}