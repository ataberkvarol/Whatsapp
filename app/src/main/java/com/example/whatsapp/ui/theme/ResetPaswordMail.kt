package com.example.whatsapp.ui.theme

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.whatsapp.CViewModel
import com.example.whatsapp.DestinationScreen
import com.example.whatsapp.R
import com.example.whatsapp.utilities.navigateTo

@SuppressLint("ResourceType")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ResetPaswordMail(navController: NavController, vm: CViewModel) {

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .wrapContentHeight()
                .verticalScroll(
                    rememberScrollState()
                ),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val MaxLength = 30
            val emailState = remember { mutableStateOf(TextFieldValue()) }
            val focus = LocalFocusManager.current
            val context = LocalContext.current

            Image(
                painter = painterResource(id = R.raw.logo2),
                contentDescription = null,
                modifier = Modifier
                    .width(200.dp)
                    .padding(8.dp)
            )

            OutlinedTextField(
                value = emailState.value,
                singleLine = true,
                shape = RoundedCornerShape(50),
                placeholder = { Text(text = "Enter your email") },
                leadingIcon = { Icon(painter = painterResource(id = R.drawable.baseline_email_24), contentDescription = null)},
                onValueChange = {
                    if (it.text.length <= MaxLength)
                        emailState.value = it
                },
                modifier = Modifier.padding(8.dp),
                label = { Text(text = "email") })
            Button(
                onClick = {
                    focus.clearFocus(force = true)
                    var wasSend = vm.onResetEmail(
                        emailState.value.text
                    )
                    if (wasSend) {
                        navigateTo(navController, DestinationScreen.ResetPasswordScreen.route)
                    } else
                        ShowToast(context, "Mail could not send")
                },
                modifier = Modifier.padding(8.dp)
            ) {
                Text(text = "SEND MAIL")
            }
            Text(text = "Go back to login screen",
                color = Color.Blue,
                modifier = Modifier
                    .padding(8.dp)
                    .clickable {
                        navigateTo(navController, DestinationScreen.Login.route)
                    })
        }
    }

}