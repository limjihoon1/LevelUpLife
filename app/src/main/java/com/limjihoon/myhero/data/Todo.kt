package com.limjihoon.myhero.data

import com.limjihoon.myhero.R

data class Todo(
    val uid: String,
    val workTodo: String,
    var state: Int,
    val no: Int,
    var quest:String="",

    )

data class MyTodo(
    val uid: String,
    val workTodo: String,
    var state: Int,
    val no: Int,
//    var quest:String,

)