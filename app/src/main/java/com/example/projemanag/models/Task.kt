package com.example.projemanag.models

import java.io.Serializable

data class Task(
    val title:String = "",
    val createdBy:String = "",
    val cardList:ArrayList<Card> = ArrayList()
):Serializable
