package com.example.whatsapp.ui.theme

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.widget.Toast
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
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.node.modifierElementOf
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.whatsapp.CViewModel
import com.example.whatsapp.DestinationScreen
import com.example.whatsapp.R
import com.example.whatsapp.navigateTo

@SuppressLint("ResourceType")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignupScreen (navController:NavController,vm:CViewModel) {
    val MaxLength = 30
    var isSignedIn = false
    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .wrapContentHeight()
                .verticalScroll(
                    rememberScrollState()
                ),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val nameState = remember { mutableStateOf(TextFieldValue()) }
            val numberState = remember { mutableStateOf(TextFieldValue()) }
            val emailState = remember { mutableStateOf(TextFieldValue()) }
            val passwordState = remember { mutableStateOf(TextFieldValue()) }
            var passwordVisibility by remember{ mutableStateOf(false) }
            var name by remember { mutableStateOf(TextFieldValue("")) }
            val focus = LocalFocusManager.current
            val context = LocalContext.current

            val icon = if (passwordVisibility)
                painterResource(id = R.drawable.baseline_visibility_24)
            else
                painterResource(id = R.drawable.baseline_visibility_off_24)

            Image(
                painter = painterResource(id = R.raw.logo2),
                contentDescription = null,
                modifier = Modifier
                    .width(200.dp)
                    .padding(8.dp)
            )
            /*
            Text(
                text = "Sign Up",
                modifier = Modifier.padding(8.dp),
                fontSize = 30.sp,
                fontFamily = FontFamily.Default
            )

             */
            OutlinedTextField(
                value = nameState.value,
                singleLine = true,
                shape = RoundedCornerShape(50),
                placeholder = { Text(text = "Enter your name")},
                leadingIcon = { Icon(painter =painterResource(id = R.drawable.person_profile), contentDescription = null) },
                onValueChange = {
                   // Log.e(it.text.length.toString(),it.text.length.toString())
                    if (it.text.length <= MaxLength )
                        nameState.value = it
                },
                modifier = Modifier.padding(8.dp),
                label = { Text(text = "name") })

            OutlinedTextField(
                value = numberState.value,
                singleLine = true,
                shape = RoundedCornerShape(50),
                placeholder = { Text(text = "Enter your phone number")},
                leadingIcon = { Icon(painter =painterResource(id = R.drawable.baseline_phone_24), contentDescription = null)},
                onValueChange = {
                    // Log.e(it.text.length.toString(),it.text.length.toString())
                    if (it.text.length <= MaxLength )
                        numberState.value = it
                    },
                modifier = Modifier.padding(8.dp),
                label = { Text(text = "phone number") })

            OutlinedTextField(
                value = emailState.value,
                singleLine = true,
                shape = RoundedCornerShape(50),
                placeholder = { Text(text = "Enter your email")},
                leadingIcon = { Icon(painter =painterResource(id = R.drawable.baseline_email_24), contentDescription = null)},
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
                leadingIcon = { Icon(painter =painterResource(id = R.drawable.baseline_password_24), contentDescription = null)},
                onValueChange = {
                    // Log.e(it.text.length.toString(),it.text.length.toString())
                    if (it.text.length <= MaxLength )
                        passwordState.value = it
                },
                modifier = Modifier.padding(8.dp),
                trailingIcon = {
                    IconButton(onClick = { passwordVisibility = !passwordVisibility }) {
                        Icon(painter = icon, contentDescription = "qwe")
                    }
                },
                label = { Text(text = "password") },
                visualTransformation = if (passwordVisibility) VisualTransformation.None
                else PasswordVisualTransformation()
            )
            Button(
                onClick = {
                    focus.clearFocus(force = true)
                    var isSignedUp = vm.signUp(
                       nameState.value.text ,
                       numberState.value.text,
                       emailState.value.text,
                       passwordState.value.text
                   )
                    if(isSignedUp){
                        navigateTo(navController,DestinationScreen.Login.route)
                    }else
                        ShowToast(context,"Sign up was failed")
                },
                modifier = Modifier.padding(8.dp)
            ) {
                Text(text = "SIGN UP")
            }
            Text(text = "already have a user go to login screen",
                color = Color.Blue,
                modifier = Modifier
                    .padding(8.dp)
                    .clickable {
                        navigateTo(navController, DestinationScreen.Login.route)
                    })
            val isLoading = vm.inProgress.value
            if (isLoading){
                CircularProgressIndicator()
            }
        }
    }
}