package com.example.whatsapp.ui.theme

import android.annotation.SuppressLint
import android.content.Context
import android.media.browse.MediaBrowser
import android.net.Uri
import android.util.Log
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.widget.FrameLayout
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
import androidx.compose.material3.Snackbar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
//import androidx.media3.common.MediaItem
//import androidx.media3.common.Player
//import androidx.media3.exoplayer.ExoPlayer
import androidx.navigation.NavController
import com.example.whatsapp.CViewModel
import com.example.whatsapp.CommonDivider
import com.example.whatsapp.DestinationScreen
import com.example.whatsapp.R
import com.example.whatsapp.navigateTo
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout.RESIZE_MODE_ZOOM
import java.security.KeyStore.PasswordProtection
import com.google.android.exoplayer2.ui.StyledPlayerView
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import kotlin.math.log


@SuppressLint("ResourceType")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun loginScreen(navController:NavController, vm:CViewModel,videoUri:Uri){
    val context = LocalContext.current
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
            var passwordVisibility by remember{ mutableStateOf(false) }
            var name by remember { mutableStateOf(TextFieldValue("")) }
            val exoPlayer = remember { context.buildExoPlayer(uri = videoUri) }
            val context = LocalContext.current
            val focus = LocalFocusManager.current

            val icon = if (passwordVisibility)
                painterResource(id = R.drawable.baseline_visibility_24)
            else
                painterResource(id = R.drawable.baseline_visibility_off_24)


            DisposableEffect(
                AndroidView(
                    factory = { it.buildPlayerView(exoPlayer) },
                    modifier = Modifier.fillMaxSize()
                )
            ) {
                onDispose {
                    exoPlayer.release()
                }
            }

            Image(
                painter = painterResource(id = R.raw.logo2),
                contentDescription = null,
                modifier = Modifier
                    .width(200.dp)
                    .padding(8.dp)
            )

            /*
            Text(
                text = "Log In",
                modifier = Modifier.padding(8.dp),
                fontSize = 30.sp,
                fontFamily = FontFamily.Default
            )

             */
            CommonDivider()
            OutlinedTextField(
                value = emailState.value,
                singleLine = true,
                leadingIcon = { Icon(painter =painterResource(id = R.drawable.baseline_email_24), contentDescription = null)} ,
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
                leadingIcon = { Icon(painter = painterResource(id = R.drawable.baseline_password_24), contentDescription = null) },
                shape = RoundedCornerShape(50),
                trailingIcon = {
                    IconButton(onClick = { passwordVisibility = !passwordVisibility }) {
                        Icon(painter = icon, contentDescription = "qwe")
                    }
                },
                placeholder = { Text(text = "Enter your password") },
                onValueChange = {
                    // Log.e(it.text.length.toString(), it.text.length.toString())
                    if (it.text.length <= MaxLength) {
                        passwordState.value = it
                    }
                },
                modifier = Modifier.padding(8.dp),
                label = { Text(text = "password") },
                visualTransformation = if (passwordVisibility) VisualTransformation.None
                else PasswordVisualTransformation()
            )
            Button(
                onClick = {
                    focus.clearFocus(force = true)
                    Log.e("loginscreen"+passwordState.value.text,"loginscreen"+passwordState.value.text)
                   isSignedIn = vm.onLogin(
                        emailState.value.text,
                        passwordState.value.text
                    )
                    if(isSignedIn){
                        navigateTo(navController,DestinationScreen.ProfileScreen.route)
                    }else
                        ShowToast(context,"Login was failed")
                },
                //modifier = Modifier.clickable{ navigateTo(navController,DestinationScreen.ProfileScreen.route) }
            ) {
                Text(text = "LOG IN")
                /*
                if (!isSignedIn){
                    Snackbar(){
                        Text(text = "")
                    }
                }

                 */
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
/*
private fun getVideoUri(): Uri {
    //val rawId = res.""("clouds", "raw", packageName)
    //resources.get
    val rawId2 = R.raw.clouds
    //val videoUri = "android.resource://$packageName/$rawId"
    return Uri.parse(rawId2.toString())
}

 */

fun ShowToast(context: Context,message:String){
    Toast.makeText(context,"$message", Toast.LENGTH_LONG).show()
}

private fun Context.buildExoPlayer(uri: Uri) =
    ExoPlayer.Builder(this).build().apply {
        setMediaItem(MediaItem.fromUri(uri))
        repeatMode = Player.REPEAT_MODE_ALL
        playWhenReady = true
        prepare()
    }
private fun Context.buildPlayerView(exoPlayer: ExoPlayer) =
    StyledPlayerView(this).apply {
        player =exoPlayer
        layoutParams = FrameLayout.LayoutParams(MATCH_PARENT, MATCH_PARENT)
        useController = false
        resizeMode = RESIZE_MODE_ZOOM
    }
private fun Context.doLogin() {
    Toast.makeText(
        this,
        "Something went wrong, try again later!",
        Toast.LENGTH_SHORT
    ).show()
}
