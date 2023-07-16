package com.example.whatsapp.ui.theme

import android.graphics.fonts.FontFamily
import android.util.Log
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.whatsapp.CViewModel
import com.example.whatsapp.DestinationScreen
import com.example.whatsapp.data.passwordChecker
import com.example.whatsapp.navigateTo

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun resetPassword(navController: NavController, vm: CViewModel) {
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
            val passwordState = remember { mutableStateOf(TextFieldValue()) }
            val focus = LocalFocusManager.current
            Text(
                text = "Reset Password",
                modifier = Modifier.padding(8.dp),
                fontSize = 30.sp,
            )
            OutlinedTextField(
                value = emailState.value,
                singleLine = true,
                shape = RoundedCornerShape(50),
                placeholder = { Text(text = "Enter your email") },
                onValueChange = {
                    if (it.text.length <= MaxLength)
                        emailState.value = it
                },
                modifier = Modifier.padding(8.dp),
                label = { Text(text = "email") })

            OutlinedTextField(
                value = passwordState.value,
                singleLine = true,
                shape = RoundedCornerShape(50),
                placeholder = { Text(text = "Enter your password") },
                onValueChange = {
                    if (it.text.length <= MaxLength)
                        passwordState.value = it
                },
                modifier = Modifier.padding(8.dp),
                label = { Text(text = "password") },
                visualTransformation = PasswordVisualTransformation()
            )
            Button(
                onClick = {
                    focus.clearFocus(force = true)
                    vm.onLogin(
                        emailState.value.text,
                        passwordState.value.text
                    )
                },
                modifier = Modifier.padding(8.dp)
            ) {
                Text(text = "RESET")
            }
            Text(text = "Go back to login screen",
                color = Color.Blue,
                modifier = Modifier
                    .padding(8.dp)
                    .clickable {
                        navigateTo(navController, DestinationScreen.Login.route)
                    })
            Column(verticalArrangement = Arrangement.spacedBy(5.dp)) {
                Log.e("state",passwordState.value.text)
                ConditionRow(condition = "Minimum 8 characters", check = passwordChecker().checkNumberOfChar(passwordState.value.text))
                ConditionRow(condition = "Minimum 1 uppercase character", check = passwordChecker().checkUpperCase(passwordState.value.text))
                ConditionRow(condition = "Minimum 1 lowercase character", check = passwordChecker().checkLowerCase(passwordState.value.text))
                ConditionRow(condition = "Minimum 1 numeric character", check = passwordChecker().checkNumberOfDigit(passwordState.value.text))
                ConditionRow(condition = "Minimum 1 special character", check = passwordChecker().checkSymbol(passwordState.value.text))
                ConditionRow(condition = "Must not has space character", check = passwordChecker().checkSpaces(passwordState.value.text))
            }
        }
    }
}
@Composable
fun ConditionRow(condition: String, check:Boolean){

    val color by animateColorAsState(targetValue =
    if (check){
        Color.Green
    } else {
        Color.Red
    }, label = "text color")

    val icon = if (check){
        Icons.Rounded.Check
    }else{
        Icons.Rounded.Close
    }
    
    Row {
        Icon(imageVector = icon, contentDescription ="status icon")
        Spacer(modifier = Modifier.width(10.dp))
        Text(text = condition,color = color)
        
    }

}