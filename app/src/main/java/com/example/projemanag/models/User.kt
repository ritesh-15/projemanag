package com.example.projemanag.models
import java.io.Serializable

data class User(
    val id:String,
    val name:String,
    val email:String,
    val image:String = "",
    val mobile:Long = 0,
    val fcmToken:String = ""
):Serializable


