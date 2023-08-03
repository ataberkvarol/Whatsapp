package com.example.whatsapp.ui.theme

import android.Manifest
import android.annotation.SuppressLint
import android.preference.PreferenceManager
import android.util.Log
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.mutableStateOf
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.whatsapp.CViewModel
import com.example.whatsapp.R
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.core.content.edit
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState

/*
val Context.dataStore by preferencesDataStore(name = "settings")

object DarkModePreferenceKeys {
    var DARK_MODE_ENABLED = booleanPreferencesKey("dark_mode_enabled")
}

*/
private const val PREF_DARK_THEME = "pref_dark_theme"

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class, ExperimentalPermissionsApi::class)
@Composable
fun settingsScreen(navController: NavController, vm: CViewModel) {

    var isSearched by remember { mutableStateOf(false) }
    var isClicked = remember { mutableStateOf(false) }
   // val permissionState = rememberPermissionState(permission = Manifest.permission.POST_NOTIFICATIONS)
    var tf by remember { mutableStateOf(TextFieldValue("")) } // by olunca bir dahdeğiştiremiyorsun va gibi oluyor
    //var switchState by rememberSaveable { mutableStateOf(({ false })()) }
    var switchState = remember { mutableStateOf(false) }
    Log.e("switchState", switchState.toString())
    var details = remember { mutableStateOf(false) }
    Log.e(isSearched.toString(),isSearched.toString())

    MyAppTheme(isDarkTheme = switchState.value) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        if (isSearched) {
                            Log.e("if","if")
                            BasicTextField(
                                value = tf,
                                onValueChange = { tf = it ;if (it.text.isEmpty()) {
                                    isSearched = false
                                }},
                                textStyle = LocalTextStyle.current.copy(color = Color.Black),
                                singleLine = true,
                                decorationBox = { innerTextField ->
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(horizontal = 8.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Icon(
                                            painter = painterResource(id = R.drawable.baseline_search_24),
                                            contentDescription = "Search",
                                            modifier = Modifier.size(24.dp)
                                        )
                                        Spacer(modifier = Modifier.width(8.dp))
                                        innerTextField()
                                        Spacer(modifier = Modifier.width(154.dp))

                                        Column(
                                            horizontalAlignment = Alignment.End
                                        ) {
                                            IconButton(onClick = { isSearched = false })
                                            {
                                                Icon(
                                                    painter = painterResource(id = R.drawable.baseline_cancel_24),
                                                    contentDescription = "test",
                                                    modifier = Modifier.size(24.dp)
                                                )
                                            }
                                        }
                                    }
                                }
                            )
                        } else {
                            Log.e("asd","else")
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Start
                            ) {
                                Text(
                                    text = "Settings",
                                    style = androidx.compose.ui.text.TextStyle(fontSize = 35.sp, fontWeight = FontWeight.ExtraBold)
                                )
                                Spacer(modifier = Modifier.width(180.dp))
                                Column(
                                    horizontalAlignment = Alignment.End
                                ) {
                                    Button(onClick = {isSearched = true}, colors = ButtonDefaults.buttonColors(containerColor = Color.DarkGray) )
                                    {
                                        Icon( painter = painterResource(id = R.drawable.baseline_search_24),
                                            contentDescription = "Search",
                                            modifier = Modifier.size(24.dp))
                                    }
                                }
                            }
                        }
                    },
                )
            },
            content = {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center
                ) {
                    val filteredItems = if (isSearched) {
                        ITEMS.filter { item ->
                            item.title.contains(tf.text, ignoreCase = true)
                        }
                    }else{
                        ITEMS
                    }
                    items(filteredItems){item -> ItemView(
                        item = item ,
                        switchState = switchState,
                        details = details,
                        navController =  navController,
                        isClicked = isClicked
                    ) }
                }
            },
            bottomBar = {
                BottomNavigationMenu(selectedItem = BottomNavigation.SETTINGS, navController = navController)
            }
        )
    }
}
data class Item(val id: Int, val title: String)
private val ITEMS = listOf(
    Item(1, "Notifications"),
    Item(2, "Dark Mode"),
    Item(3, "Item 3"),
)
@OptIn(ExperimentalAnimationApi::class, ExperimentalPermissionsApi::class)
@Composable
fun ItemView(
    item: Item, switchState: MutableState<Boolean>,
    details:MutableState<Boolean>,
    navController: NavController?,
    isClicked: MutableState<Boolean>
) {
    var permissionState = rememberPermissionState(permission = Manifest.permission.POST_NOTIFICATIONS)
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        shape = RoundedCornerShape(8.dp),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = item.title,
                color = Color.Black
            )
            if (item.title == "Notifications"){
                Spacer(modifier = Modifier.width(150.dp))
                Log.e("permission",permissionState.toString())
                OutlinedButton(onClick = { isClicked.value = true }) {
                    Text(text = "Allow")
                    if (isClicked.value) {
                        permissionState.launchPermissionRequest()
                        Log.e("permission", "true")
                        Log.e("permission", permissionState.status.isGranted.toString())
                    }
                }
                /*
                if (details.value == true){
                    Log.e("details","details:true")

                    if (navController != null) {
                        var visible by remember { mutableStateOf(true) }
                        Log.e("notification","notification:true")

                        AnimatedVisibility(
                            visible = visible,
                            enter = slideInVertically(
                                initialOffsetY = { -40 }
                            ) + expandVertically(
                                expandFrom = Alignment.Top
                            ) + scaleIn(
                                transformOrigin = TransformOrigin(0.5f, 0f)
                            ) + fadeIn(initialAlpha = 0.3f),
                            exit = slideOutVertically() + shrinkVertically() + fadeOut() + scaleOut(targetScale = 1.2f)
                        ){


                        navController.navigate(DestinationScreen.NotificationsScreen.route)}
                    }
                }
                */


            }
            if(item.title == "Dark Mode" ){
                Spacer(modifier = Modifier.width(190.dp))
                SwitchButtonFunction(switchState)
            }
            details.value = false
        }
    }
}
@Composable
fun SwitchButtonFunction(switchState: MutableState<Boolean>) {
    Switch(
        checked = switchState.value,
        onCheckedChange = { isChecked ->
            switchState.value = isChecked
        },
        colors = SwitchDefaults.colors(
            checkedThumbColor = Color.Black,
            checkedTrackColor = Color.White
        )
    )
}
@Composable
fun MyAppTheme(
    isDarkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    val context = LocalContext.current
    val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
    // isDarkTheme = sharedPreferences.getBoolean(PREF_DARK_THEME, false)
    val colorScheme =
        if (isDarkTheme) {
            darkColorScheme()
        } else{
            lightColorScheme()
    }
    Log.e(isDarkTheme.toString(), isDarkTheme.toString())
    sharedPreferences.edit {
        putBoolean(PREF_DARK_THEME, isDarkTheme)
    }
    MaterialTheme(
        colorScheme = colorScheme,
        content = content
    )
}
/*
@Composable
fun slideInVertically(
    animationSpec: FiniteAnimationSpec<IntOffset> = spring(
        stiffness = Spring.StiffnessMediumLow,
        visibilityThreshold = IntOffset.VisibilityThreshold
    ),
    initialOffsetY: (fullHeight: Int) -> Int = { -it / 2 }
): EnterTransition{}
 */