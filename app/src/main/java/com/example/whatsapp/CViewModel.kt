package com.example.whatsapp

import android.annotation.SuppressLint
import android.net.Uri
import android.provider.CalendarContract.CalendarAlerts
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.platform.LocalContext
import androidx.core.text.isDigitsOnly
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.example.whatsapp.data.COLLECTION_CHAT
import com.example.whatsapp.data.COLLECTION_MESSAGES
import com.example.whatsapp.data.COLLECTION_STATUS
import com.example.whatsapp.data.COLLECTON_USER
import com.example.whatsapp.data.ChatData
import com.example.whatsapp.data.ChatUser
import com.example.whatsapp.data.Message
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import dagger.hilt.android.lifecycle.HiltViewModel
import java.lang.Exception
import javax.inject.Inject
import com.example.whatsapp.data.UserData
import com.example.whatsapp.data.passwordChecker
import com.google.firebase.firestore.Filter
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.firestore.ktx.toObjects
import java.util.Calendar
import javax.net.ssl.SSLEngineResult.Status


@HiltViewModel
class CViewModel @Inject constructor(val auth:FirebaseAuth, val db: FirebaseFirestore, val storage: FirebaseStorage):ViewModel() {

    // sign in
    val inProgress = mutableStateOf(false)
    val popUpNotification = mutableStateOf<com.example.whatsapp.data.Event<String>?>(null)
    val signedIn = mutableStateOf(false)
    val signedUp = mutableStateOf(false)
    val userData = mutableStateOf<UserData?>(null)


    // chat
    val chats = mutableStateOf<List<ChatData>>(listOf())
    val inProgressChats = mutableStateOf(false)
    val chatMessages = mutableStateOf<List<Message>>(listOf())
    val inProgressMessageChats = mutableStateOf(false)
    // status screen
    val status = mutableStateOf<List<Status>>(listOf())
    val inProgressStatus = mutableStateOf(false)
    var currentChatMessagesListener: ListenerRegistration? = null

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
    fun signUp(name: String, number: String, email: String, password: String):Boolean {
        if (name.isBlank() || number.isBlank() || email.isBlank() || password.isBlank()) {
            handleException(customMessage = "please fill the required fields")
            return false
        }
        inProgress.value = true
        db.collection(com.example.whatsapp.data.COLLECTON_USER).whereEqualTo("number", number).get()
            .addOnSuccessListener {
                if (it.isEmpty) {
                    auth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                signedUp.value = true
                                createOrUpdateProfile(name = name, number = number)
                            } else {
                                handleException(customMessage = "signIn failed")
                                signedUp.value = false
                            }
                        }
                } else {
                    inProgress.value = false
                    signedUp.value = false
                    handleException(customMessage = "number already exists")
                }
            }.addOnFailureListener {
            handleException(it)
        }
        if (signedUp.value){
            return true
        }else{
            return false
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
    fun onLogin(email: String, password: String):Boolean {
        Log.e("Model"+password,"Model"+password)
           if (!email.isBlank() || !password.isBlank()) {
               inProgress.value = true
               auth.signInWithEmailAndPassword(email, password)
                   .addOnCompleteListener { task ->
                       if (task.isSuccessful) {
                           signedIn.value = true
                           inProgress.value = false
                           auth.currentUser?.uid?.let { getUserData(it) }
                       } else {
                           handleException(task.exception, "login failed")
                           Log.e("error", task.exception.toString())
                       }
                   }.addOnFailureListener { handleException(it, "login failed") }
                    if(signedIn.value == true) {
                        return true
                    }else
                        return false
           } else {
        handleException(customMessage = "please fill the required fields")
        Log.e("error", "please fill the required fields")
               return false
    }


        /*
               if (passwordChecker().checkNumberOfChar(password)){
                   if (passwordChecker().checkSpaces(password)){
                       if (passwordChecker().checkSymbol(password)){
                           if (passwordChecker().checkLowerCase(password)){
                               if (passwordChecker().checkUpperCase(password)){
                               */

                            /*
                               }else
                                   handleException(customMessage = "there must be one uppercase character in the password")
                                    Log.e("there must be one uppercase character in the password","there must be one uppercase character in the password")
                           }else
                               handleException(customMessage = "there must be one lowercase character in the password")
                           Log.e("there must be one lowercase character in the password","there must be one lowercase character in the password")
                       }else
                           handleException(customMessage = "there must be one special character in the password")
                       Log.e("there must be one special character in the password","there must be one special character in the password")
                   }else
                       handleException(customMessage = "there should not be space character in the password")
                   Log.e("there should not be space character in the password","there should not be space character in the password")
               }else
                   handleException(customMessage = "the password must be longer or equal than 8 characters")
               Log.e("the password must be longer or equal than 8 characters","the password must be longer or equal than 8 characters")

                             */


    }
    fun onLogout(){
        auth.signOut()
        signedIn.value = false
        userData.value = null
        popUpNotification.value = com.example.whatsapp.data.Event("logged out")
        chats.value = listOf()
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
                inProgress.value = false
                populateStatuses()
                //populateChat() fixme
            }
        }
    }
    private fun createStatus(imageUrl: String?){
        val newStatus = com.example.whatsapp.data.Status(
            ChatUser(
                userData.value?.userId,
                userData.value?.name,
                userData.value?.imageUrl,
                userData.value?.number
            ) as ChatUser,
            imageUrl,
            System.currentTimeMillis()
        )
        db.collection(COLLECTION_STATUS).document().set(newStatus)
    }
    private fun uploadProfileImage(uri: Uri) {
       uploadImage(uri){
           createOrUpdateProfile(imageUrl = "url")//fixme
       }
    }
    private fun uploadImage(uri: Uri, function: () -> Unit) {
        TODO("Not yet implemented")
    }
    fun onAddChat(number: String){
        if (number.isEmpty() || !number.isDigitsOnly())
            handleException(customMessage = "number must contain only digits")
        else{
            db.collection(COLLECTION_CHAT).where(Filter.or(
                Filter.and(Filter.equalTo("user1.number",number),
                    Filter.equalTo("user2.number",userData.value?.number)),
                Filter.and
                    (Filter.equalTo("user1.number",userData.value?.number),
                    Filter.equalTo("user2.number",number))
            )
            )
                .get()
                .addOnSuccessListener {
                    if (it.isEmpty){
                        db.collection(COLLECTON_USER).whereEqualTo("number",number)
                            .get()
                            .addOnSuccessListener {
                                if (it.isEmpty)
                                    handleException(customMessage = "cannot retrive data from user with number $number")
                                else{
                                    val chatPartner = it.toObjects<UserData>()[0]
                                    val id = db.collection(COLLECTION_CHAT).document().id
                                    val chat = ChatData(id,
                                    ChatUser(
                                        userData.value?.userId,
                                        userData.value?.number,
                                        userData.value?.name,
                                        userData.value?.imageUrl
                                    ),
                                        ChatUser(
                                            chatPartner.userId,
                                            chatPartner.number,
                                            chatPartner.name,
                                            chatPartner.imageUrl
                                        )
                                    )
                                    db.collection(COLLECTION_CHAT).document(id).set(chat)
                                }
                            }.addOnFailureListener {
                                handleException(it)
                            }
                    }else{
                        handleException(customMessage = "chat already exists")
                    }
                }
        }
    }
    fun  uploadStatus(imageUri:Uri){
        uploadImage(imageUri){
            createStatus(imageUrl = "url") // fixme
        }
    }
    fun populateStatuses(){
        inProgressStatus.value = true
        val milliTimeData = 24L * 60 * 60 * 1000
        val cutOff = System.currentTimeMillis() - milliTimeData

        db.collection(COLLECTION_CHAT)
            .where(Filter.or(
                Filter.equalTo("user1.userId",userData.value?.userId),
                Filter.equalTo("user2.userId",userData.value?.userId)
            )
            )
            .addSnapshotListener{value, error ->
                if (error != null)
                    handleException(error)
                if (value != null) {
                    val currentConnections = arrayListOf(userData.value?.userId)
                    val chats = value.toObjects<ChatData>()
                    chats.forEach{
                        chat ->
                        if (chat.user1.userId == userData.value?.userId)
                            currentConnections.add(chat.user2.userId)
                        else
                            currentConnections.add(chat.user1.userId)
                    }
                    db.collection(COLLECTION_STATUS)
                        .whereGreaterThan("timestamp",cutOff)
                        .whereIn("user.userId",currentConnections)
                        .addSnapshotListener{value,error ->
                            if (error != null)
                                handleException(error)
                            if (value!= null)
                                status.value = value.toObjects()
                            inProgressStatus.value = false
                        }
                }
            }
    }
    fun populateChat(chatId: String){
        inProgressChats.value = true
       currentChatMessagesListener = db.collection(COLLECTION_CHAT).where(Filter.or(
            Filter.equalTo("user1.userId",userData.value?.userId),
            Filter.equalTo("user2.userId",userData.value?.userId)
        ))
            .addSnapshotListener{value,error ->
                if (error!=null)
                    handleException(error)
                if (value!= null)
                    chatMessages.value = value.documents
                        .mapNotNull { it.toObject<Message>() }
                        .sortedBy { it.timestamp }
                inProgressChats.value = false
            }
    }

    fun depopulateChat(){
        chatMessages.value = listOf()
        currentChatMessagesListener = null
    }
    fun onSendReply(chatId:String,message: String){
        val time = Calendar.getInstance().time.toString()
        val msg = Message(userData.value?.userId,message,time)
        db.collection(COLLECTION_CHAT).document(chatId).collection(COLLECTION_MESSAGES).document().set(msg)
    }
}