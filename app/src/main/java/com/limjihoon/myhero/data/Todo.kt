package com.limjihoon.myhero.data

data class Todo(
    val uid: String,
    val workTodo: String,
    var state: Int,
    val no: Int,
    var quest:String="normal",

    )

data class MyTodo(
    val uid: String,
    val workTodo: String,
    var state: Int,
    val no: Int,
//    var quest:String,

)