package com.example.whatsapp.ui.theme

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.whatsapp.CViewModel
import com.example.whatsapp.CommonDivider
import com.example.whatsapp.DestinationScreen
import com.example.whatsapp.R
import com.example.whatsapp.navigateTo
import java.security.KeyStore.PasswordProtection

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun loginScreen(navController:NavController, vm:CViewModel){
    var isSignedIn = false
    val MaxLength = 30
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
            val emailState = remember { mutableStateOf(TextFieldValue()) }
            val passwordState = remember { mutableStateOf(TextFieldValue()) }
            var name by remember { mutableStateOf(TextFieldValue("")) }
            val focus = LocalFocusManager.current

            /*
            Image(
                painter = painterResource(id = R.drawable.chat),
                contentDescription = null,
                modifier = Modifier
                    .width(200.dp)
                    .padding(top = 16.dp)
                    .padding(8.dp)

            )
            */
            Text(
                text = "Log In",
                modifier = Modifier.padding(8.dp),
                fontSize = 30.sp,
                fontFamily = FontFamily.Default
            )
            CommonDivider()
            OutlinedTextField(
                value = emailState.value,
                singleLine = true,
                shape = RoundedCornerShape(50),
                placeholder = { Text(text = "Enter your email")},
                onValueChange = {
                    // Log.e(it.text.length.toString(),it.text.length.toString())
                    if (it.text.length <= MaxLength )
                        emailState.value = it
                },
                modifier = Modifier.padding(8.dp),
                label = { Text(text = "email") })
            OutlinedTextField(
                value = passwordState.value,
                singleLine = true,
                shape = RoundedCornerShape(50),
                placeholder = { Text(text = "Enter your password")},
                onValueChange = {
                    // Log.e(it.text.length.toString(),it.text.length.toString())
                    if (it.text.length <= MaxLength )
                        passwordState.value = it
                },
                modifier = Modifier.padding(8.dp),
                label = { Text(text = "password") },
                visualTransformation = PasswordVisualTransformation()
            )
            Button(
                onClick = {
                    focus.clearFocus(force = true)
                    Log.e("loginscreen"+passwordState.value.text,"loginscreen"+passwordState.value.text)
                   isSignedIn = vm.onLogin(
                        emailState.value.text,
                        passwordState.value.text
                    )
                    Log.e(isSignedIn.toString(), isSignedIn.toString())
                    if(isSignedIn)
                        navigateTo(navController,DestinationScreen.ProfileScreen.route)

                },
                //modifier = Modifier.clickable{ navigateTo(navController,DestinationScreen.ProfileScreen.route) }
            ) {
                Text(text = "LOG IN")
            }
            Text(text = "Register",
                color = Color.Blue,
                modifier = Modifier
                    .padding(8.dp)
                    .clickable {
                        navigateTo(navController, DestinationScreen.Signup.route)
                    })

            Text(text = "Forgot my password",
                color = Color.Blue,
                modifier = Modifier
                    .padding(8.dp)
                    .clickable {
                        navigateTo(navController, DestinationScreen.ResetPasswordScreen.route)
                    })
            val isLoading = vm.inProgress.value
            if (isLoading){
                CircularProgressIndicator()
            }

        }
    }
}