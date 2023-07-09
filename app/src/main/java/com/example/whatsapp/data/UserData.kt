package com.example.whatsapp.data
class UserData(val userId: String? = "", val name: String? = "", val number: String? = "", val imageUrl: String? = "") {
    fun toMap(): MutableMap<String, String?> {
        return mutableMapOf(
            "userId" to userId,
            "name" to name,
            "number" to number,
            "imageUrl" to imageUrl
        )
    }
}