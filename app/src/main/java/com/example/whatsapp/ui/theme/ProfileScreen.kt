package com.example.whatsapp.ui.theme

import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.whatsapp.CViewModel
import com.example.whatsapp.utilities.CommonDivider
import com.example.whatsapp.utilities.CommonImage
import com.example.whatsapp.DestinationScreen
import com.example.whatsapp.utilities.navigateTo
import com.example.whatsapp.utilities.CommonProcessSpinner
import com.example.whatsapp.R

@Composable
fun ProfileScreen(navController: NavController, vm: CViewModel) {
    val inProgress = vm.inProgress.value
    if (inProgress)
        CommonProcessSpinner()
    else {
        val userData = vm.userData.value
        var name by rememberSaveable { mutableStateOf(userData?.name ?: "") }
        var number by rememberSaveable { mutableStateOf(userData?.number ?: "") }
        var imageUrl by rememberSaveable { mutableStateOf(userData?.imageUrl?: "")} //eklendi
        var status by rememberSaveable { mutableStateOf(userData?.status ?: "") }

        val scrollState = rememberScrollState()
        val focus = LocalFocusManager.current

        Column {
            ProfileContent(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(scrollState)
                    .padding(8.dp),
                vm = vm,
                name = name,
                number = number,
                status = status,
                onNameChange = { name = it },
                onNumberChange = { number = it },
                onStatusChange = { status = it },
                onSave = {
                    focus.clearFocus(true)
                    vm.updateProfileData(name, number,imageUrl,status)
                },
                onBack = {
                    focus.clearFocus(true)
                    navigateTo(navController, DestinationScreen.ChatListScreen.route)
                },
                onLogout = {
                    vm.onLogout()
                    navigateTo(navController, DestinationScreen.Login.route)
                }
            )

            BottomNavigationMenu(
                selectedItem = BottomNavigation.PROFILE,
                navController = navController
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileContent(
    modifier: Modifier,
    vm: CViewModel,
    name: String,
    number: String,
    status: String,
    onNameChange: (String) -> Unit,
    onNumberChange: (String) -> Unit,
    onStatusChange: (String) -> Unit,
    onSave: () -> Unit,
    onBack: () -> Unit,
    onLogout: () -> Unit
) {
    val imageUrl = vm.userData.value?.imageUrl

    Column(modifier = modifier) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Button(
                onClick = {
                    onBack.invoke()
                },
            ) {
                Text(text = "Back")
            }
            Button(
                onClick = {
                    onSave.invoke()
                },
            ) {
                Text(text = "Save")
            }
        }

        CommonDivider()

        ProfileImage(imageUrl = imageUrl, vm = vm)

        CommonDivider()

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            OutlinedTextField(

                value = name,
                onValueChange = onNameChange,
                singleLine = true,
                shape = RoundedCornerShape(50),
                leadingIcon = { Icon(painter = painterResource(id = R.drawable.person_profile), contentDescription = null) },
                placeholder = { Text(text = "Enter your name")},
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                colors = TextFieldDefaults.textFieldColors(
                    textColor = Color.Black,
                    containerColor = Color.Transparent
                ),
                label = { Text(text = "Name",modifier = Modifier.width(100.dp)) })
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {

            OutlinedTextField(
                value = number,
                onValueChange = onNumberChange,
                singleLine = true,
                shape = RoundedCornerShape(50),
                leadingIcon = { Icon(painter = painterResource(id = R.drawable.baseline_phone_24), contentDescription = null) },
                placeholder = { Text(text = "Enter your phone number")},
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                colors = TextFieldDefaults.textFieldColors(
                    textColor = Color.Black,
                    containerColor = Color.Transparent
                ) ,
                label = { Text(text = "Phone number",modifier = Modifier.width(100.dp)) })
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp),
            verticalAlignment = Alignment.CenterVertically ,
            horizontalArrangement = Arrangement.Center
        ) {

            OutlinedTextField(
                value = status,
                onValueChange = onStatusChange,
                singleLine = true,
                shape = RoundedCornerShape(50),
                leadingIcon = { Icon(painter = painterResource(id = R.drawable.baseline_spatial_audio_off_24), contentDescription = null) },
                placeholder = { Text(text = "Enter status")},
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                colors = TextFieldDefaults.textFieldColors(
                    textColor = Color.Black,
                    containerColor = Color.Transparent
                ) ,
                label = { Text(text = "Status",modifier = Modifier.width(100.dp)) })
        }

        CommonDivider()

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            Button(
                onClick = {
                    onLogout.invoke()
                },
            ) {
                Text(text = "LOG OUT")
            }
        }

    }
}

@Composable
fun ProfileImage(imageUrl: String?, vm: CViewModel) {

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
    ) { uri: Uri? ->
        uri?.let {
            vm.uploadProfileImage(uri)
        }
    }
    Log.e("launcher", launcher.toString())

    Box(modifier = Modifier.height(IntrinsicSize.Min)) {
        Column(modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .clickable {
                launcher.launch("image/*")
            },
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Card(shape = CircleShape, modifier = Modifier
                .padding(8.dp)
                .size(100.dp)) {
                CommonImage(data = imageUrl)
            }
            Text(text = "Change profile picture")
        }
        val isLoading = vm.inProgress.value
        if (isLoading)
            CommonProcessSpinner()
    }
}
