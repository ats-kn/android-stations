package com.example.techtrain.railway.android

object Util {
    fun createJson(name:String, email:String, password:String):String
        ="{" +
        "  \"name\": \"${name}\"," +
        "  \"email\": \"${email}\"," +
        "  \"password\": \"${password}\"" +
        "}"
}
