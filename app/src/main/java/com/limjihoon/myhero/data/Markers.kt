package com.limjihoon.myhero.data

data class Markers(
    var todoNo:Int = 0,
    val todoUid: String = "",
    val workTodo: String = "",
    val lat: Double = 0.0,
    val lng: Double = 0.0,
    var state:Int = 0
)
