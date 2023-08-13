package com.example.whatsapp.ui.theme

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment.Companion.Bottom
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.whatsapp.DestinationScreen
import com.example.whatsapp.R
import com.example.whatsapp.utilities.navigateTo

enum class BottomNavigation(val icon:Int, val navDestination: DestinationScreen){
    PROFILE(R.drawable.person_profile,DestinationScreen.ProfileScreen),
    CHATLIST(R.drawable.baseline_chat_24,DestinationScreen.ChatListScreen),
    STATUSLIST(R.drawable.baseline_stream_24,DestinationScreen.StatusListScreen),
    SETTINGS(R.drawable.baseline_settings_24,DestinationScreen.SettingsScreen)
}
@Composable
fun BottomNavigationMenu(selectedItem:BottomNavigation, navController: NavController){
    Row(modifier = Modifier.run {
        fillMaxWidth()
        .wrapContentHeight(Bottom)
        .padding(top = 4.dp)
        .background(Color.White)
    })
    {
        for (item in BottomNavigation.values()){
            Image(painter = painterResource(id = item.icon),
                    contentDescription = null,
                    modifier = Modifier
                    .size(40.dp)
                    .background(Color.LightGray)
                        .padding(4.dp)
                    .weight(1f)
                    .clickable {
                               navigateTo(navController,item.navDestination.route)
                    },
            )
        }
    }
}
