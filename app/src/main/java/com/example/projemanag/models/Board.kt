package com.example.projemanag.models

import java.io.Serializable

data class Board(
    val name:String,
    val image:String,
    val createdBy:String,
    val assignedTo:ArrayList<String> = ArrayList(),
    val documentId:String = ""
):Serializable
