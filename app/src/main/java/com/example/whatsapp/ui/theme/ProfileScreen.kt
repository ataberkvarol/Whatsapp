package com.example.whatsapp.ui.theme

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.whatsapp.CViewModel
import com.example.whatsapp.CommonDivider
import com.example.whatsapp.DestinationScreen
import com.example.whatsapp.navigateTo
import kotlin.math.log

@Composable
fun profileScreen (navController: NavController, vm:CViewModel){
    val inProgress = vm.inProgress.value
    if (inProgress)
       // CommonProgressSpinner()
    else{
        val userData = vm.userData.value
        var name by rememberSaveable{ mutableStateOf(userData?.name ?:"") }
        var number by rememberSaveable{ mutableStateOf(userData?.number ?:"") }
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
                onNameChange = {name = it},
                onNumberChange = {number = it},
                onSave = {
                    focus.clearFocus(force = true)
                },
                onBack = {
                    focus.clearFocus(force = true)
                    navigateTo(navController,DestinationScreen.ChatListScreen.route)
                },
                onLogout = {
                    vm.onLogout()
                  //  focus.clearFocus(force = true)
                    navigateTo(navController,DestinationScreen.Login.route)
                }
            )
            BottomNavigationMenu(
                selectedItem = BottomNavigation.PROFILE,
                navController = navController
            )
        }
        }
}
/*
@Composable
fun CommonProgressSpinner() {
    TODO("Not yet implemented")
}

 */

@OptIn(ExperimentalMaterial3Api::class)
@Composable
// ilk açılış noktası burası bunun olmaması gerekir
fun ProfileContent(modifier:Modifier, vm:CViewModel,name:String,number:String,onNameChange:(String) -> Unit, onNumberChange:(String) -> Unit,onSave:()-> Unit, onBack: ()-> Unit, onLogout: () -> Unit ) {

    Column(modifier = Modifier) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            Text(text = "Back", modifier = Modifier.clickable { onBack.invoke() })
            Text(text = "Save", modifier = Modifier.clickable { onSave.invoke() })
        }
        CommonDivider()
       // ProfileImage()
        CommonDivider()
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp), verticalAlignment = Alignment.CenterVertically
        )
        {
            Text(text = "Name", modifier = Modifier.width(100.dp))
            TextField(value = name, onValueChange = onNameChange)
        }
    }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp), verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = "Name", modifier = Modifier.width(100.dp))
        TextField(value = number, onValueChange = onNumberChange)
    }
    CommonDivider()
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
            horizontalArrangement = Arrangement.Center
    ) {
        Text(text = "logout", modifier = Modifier.clickable { onLogout.invoke() })
    }
    @Composable
    fun ProfileImage() {
    }
}