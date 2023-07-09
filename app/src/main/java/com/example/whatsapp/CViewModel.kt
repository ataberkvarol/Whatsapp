package com.example.whatsapp

import android.annotation.SuppressLint
import android.app.PendingIntent.getActivity
import android.app.usage.UsageEvents
import android.content.Context
import android.media.metrics.Event
import android.util.EventLog
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import com.example.whatsapp.data.COLLECTON_USER
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import dagger.hilt.android.lifecycle.HiltViewModel
import java.lang.Exception
import javax.inject.Inject
import com.example.whatsapp.data.UserData
import com.google.firebase.firestore.ktx.toObject



@HiltViewModel
class CViewModel @Inject constructor(val auth:FirebaseAuth, val db: FirebaseFirestore, val storage: FirebaseStorage):ViewModel() {

    val inProgress = mutableStateOf(false)
    val popUpNotification = mutableStateOf<com.example.whatsapp.data.Event<String>?>(null)
    val signedIn = mutableStateOf(false)
    val userData = mutableStateOf<UserData?>(null)

    init {
        //onLogout()
        val currentUser = auth.currentUser
        signedIn.value = currentUser != null
        currentUser?.uid.let { uid ->
            if (uid != null) {
                getUserData(uid)
            }
        }
    }

    @SuppressLint("SuspiciousIndentation")
    fun signUp(name: String, number: String, email: String, password: String) {
        if (name.isBlank() || number.isBlank() || email.isBlank() || password.isBlank()) {
            handleException(customMessage = "please fill the required fields")
            return
        }
        inProgress.value = true
        db.collection(com.example.whatsapp.data.COLLECTON_USER).whereEqualTo("number", number).get()
            .addOnSuccessListener {
                if (it.isEmpty) {
                    auth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                signedIn.value = true
                                createOrUpdateProfile(name = name, number = number)
                            } else
                                handleException(customMessage = "signIn failed")
                        }
                } else
                    inProgress.value = false
                handleException(customMessage = "number already exists")

            }.addOnFailureListener {
            handleException(it)
        }

    }

    private fun handleException(exception: Exception? = null, customMessage: String = "") {
        Log.e("error", exception.toString())
        val errorMessage = exception?.localizedMessage ?: ""
        val message = if (customMessage.isEmpty()) errorMessage else "$customMessage: $errorMessage"
        popUpNotification.value = com.example.whatsapp.data.Event(message)
        inProgress.value = false
    }


    @SuppressLint("SuspiciousIndentation")
    private fun createOrUpdateProfile(
        name: String? = null,
        number: String? = null,
        imageUrl: String? = null
    ) {
        val uid = auth.currentUser?.uid
        val userData = UserData(
            userId = uid,
            name = name?: userData.value?.name,
            number = number?:userData.value?.number,
            imageUrl = imageUrl?:userData.value?.imageUrl
        )
        uid?.let { uid ->
            inProgress.value = true
            db.collection(COLLECTON_USER).document().get().addOnSuccessListener {
                if (it.exists()) {
                   // it.reference.update(UserData)
                     //   .addOnSuccessListener { inProgress.value = false }
                       // .addOnFailureListener { handleException(it,"cannot update user") }
                } else
                    db.collection(COLLECTON_USER).document(uid).set(userData)
                    inProgress.value = false
            }.addOnFailureListener { handleException(it, "cannot create user") }
        }
    }
    @Composable
    private fun passwordValidation(password: String):Boolean {
        val context = LocalContext.current
        if (password.isBlank()){

            handleException(customMessage = "please fill the password field")
            Log.e("error", "please fill the password field")
            return false
        }
        if(password.length < 8){
            handleException(customMessage = "password must be longer than 8 characters")
            Log.e("error", "password must be longer than 8 characters")
            Toast.makeText(context,"password must be longer than 8 characters",Toast.LENGTH_LONG).show()
            return false
        }
        if (!password.matches(".*[A-Z].*".toRegex())){
                handleException(customMessage = "password must contain 1 uppercase character")
                Log.e("error", "password must contain 1 uppercase character")
                return false
            }
        if (!password.matches(".*[a-z].*".toRegex())){
            handleException(customMessage = "password must contain 1 lowercase character")
            Log.e("error", "password must contain 1 lowercase character")
            return false
        }
        if (!password.matches(".*[@#\$%^&+=].*".toRegex())){
            handleException(customMessage = "password must contain 1 special character")
            Log.e("error", "password must contain 1 special character")
             Toast.makeText(context,"password must contain 1 special character",Toast.LENGTH_LONG).show()
            return false
        }
        if (password.contains(" ")){
            handleException(customMessage = "password must not contain space character")
            Log.e("error", "password must not contail space character")
            return false
        }

        return true
    }


    @Composable
    fun onLogin(email: String, password: String) {
       if (passwordValidation(password)) {

           if (email.isBlank() || password.isBlank()) {
               handleException(customMessage = "please fill the required fields")
               Log.e("error", "please fill the required fields")
               return
           }
           inProgress.value = true
           auth.signInWithEmailAndPassword(email, password)
               .addOnCompleteListener { task ->
                   if (task.isSuccessful) {
                       signedIn.value = true
                       inProgress.value = false
                       auth.currentUser?.uid?.let { getUserData(it) }
                   } else
                       handleException(task.exception, "login failed")
               }.addOnFailureListener { handleException(it, "login failed") }
       }else
           handleException(customMessage = "login failed")
    }

    fun onLogout(){
        auth.signOut()
        signedIn.value = false
        userData.value = null
        popUpNotification.value = com.example.whatsapp.data.Event("logged out")
    }



    private fun getUserData(uid:String){

        inProgress.value = true
        db.collection(COLLECTON_USER).document(uid).addSnapshotListener{value,error ->
            if (error != null){
                handleException(error,"cannot retrive user data")
            }
            if (value != null){
                val user = value.toObject<UserData>()
                userData.value = user
            }
        }

    }
}