package com.example.projemanag.models

import java.io.Serializable

data class Card(val name:String = "",
                val createdBy:String = "",
                val assignedTo:ArrayList<String> = ArrayList()
                ):Serializable
