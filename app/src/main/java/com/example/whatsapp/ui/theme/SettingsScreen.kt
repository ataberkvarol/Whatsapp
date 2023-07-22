package com.example.whatsapp.ui.theme

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Shapes
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.whatsapp.CViewModel
import com.example.whatsapp.R
import com.example.whatsapp.TitleText

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun settingsScreen(navController: NavController, vm: CViewModel) {
 val aramaYapiliyormu = remember { mutableStateOf(true) }
    val tf = remember {
        mutableStateOf("")
    }
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    if (aramaYapiliyormu.value) {
                        TextField(value = tf.value, onValueChange = {
                            tf.value = it
                        },
                            label = { Text(text = "Search") },
                            colors = TextFieldDefaults.textFieldColors(
                                containerColor = Color.Transparent,
                                focusedIndicatorColor = Color.White,
                                unfocusedIndicatorColor = Color.White,
                                textColor = Color.Black
                            )
                        )
                        Column {
                            Text(text = " Settings")
                        }
                    }
                },
                  colors = TopAppBarDefaults.smallTopAppBarColors(containerColor = colorResource(id = R.color.white)
            ),
            )
      },
        content = {
            Text(text = "test")
        },

        )
        Column(modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Bottom)

    {
        BottomNavigationMenu(
            selectedItem = BottomNavigation.SETTINGS,
            navController = navController)

    }

    }







    
    /*
    Box(modifier = Modifier
        .fillMaxSize()
        .background(Color.White)) {
        Column(

            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        )
        {  TitleText(txt = "Settings")

        }
        Row(modifier = Modifier
            .fillMaxSize()
            .wrapContentHeight(),
        verticalAlignment = Alignment.CenterVertically)
        {
            GeneralSettingsItem()
        }

        Column(modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Bottom)
        {
            BottomNavigationMenu(
                selectedItem = BottomNavigation.SETTINGS,
                navController = navController)
        }
    }
}
@Composable
fun GeneralSettingsItem() {
    val focus = LocalFocusManager.current
    Card(
        modifier = Modifier
            .padding(bottom = 8.dp)
            .fillMaxWidth() ) {
        Text(text = "Notifications"
            ,modifier =Modifier.wrapContentHeight())
        Row(
            modifier = Modifier.padding()
        ) {

            Box(
                modifier = Modifier
                    .size(60.dp)
                    .clip(shape = CircleShape),

                )
            {
            Icon(
                    painter = painterResource(id = R.drawable.baseline_chat_24),
                    contentDescription = "",
                    tint = Color.Unspecified,
                    modifier = Modifier.padding(8.dp)
                )

            }

        }
    }

     */

