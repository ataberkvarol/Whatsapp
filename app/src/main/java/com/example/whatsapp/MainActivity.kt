package com.example.whatsapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.whatsapp.ui.theme.ChatListScreen
import com.example.whatsapp.ui.theme.SignupScreen
import com.example.whatsapp.ui.theme.SingleStatusScreen
import com.example.whatsapp.ui.theme.StatusListScreen
import com.example.whatsapp.ui.theme.WhatsappTheme
import com.example.whatsapp.ui.theme.loginScreen
import com.example.whatsapp.ui.theme.personalChatScreen
import com.example.whatsapp.ui.theme.profileScreen
import dagger.hilt.android.AndroidEntryPoint


sealed class DestinationScreen(val route: String){

    object Signup : DestinationScreen("signup")
    object Login : DestinationScreen("login")
    object ProfileScreen : DestinationScreen("ProfileScreen")
    object ChatListScreen : DestinationScreen("ChatListScreen")
    object StatusScreen : DestinationScreen("StatusScreen"){
        fun createRoute(id:String) = "StatusScreen/$id"
    }
    object StatusListScreen : DestinationScreen("StatusListScreen")
    object PersonalChat : DestinationScreen("PersonalChat/{chatId}"){
        fun createRoute(id:String) = "PersonalChat/$id"
    }


}

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ChatAppNavigation()
        }
    }
}

@Composable
fun ChatAppNavigation() {
    val navController = rememberNavController()
    val vm = hiltViewModel<CViewModel>()
    
    NavHost(navController = navController , startDestination =  DestinationScreen.Login.route){
        composable(DestinationScreen.Signup.route){
            SignupScreen(navController, vm)
        }
        composable(DestinationScreen.Login.route){
            loginScreen( navController,vm )
        }
        composable(DestinationScreen.ProfileScreen.route){
            profileScreen( navController, vm)
        }
        composable(DestinationScreen.StatusListScreen.route){
            StatusListScreen(navController = navController)
        }
        composable(DestinationScreen.ChatListScreen.route){
            ChatListScreen(navController = navController)
        }
        composable(DestinationScreen.StatusScreen.route){
            SingleStatusScreen(statusId = "123")
        }
        composable(DestinationScreen.StatusScreen.route){
            personalChatScreen(chatId = "123")
        }
    }
    
}
