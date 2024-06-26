package com.limjihoon.myhero.data

data class Todo(
    val uid: String,
    val workTodo: String,
    var state: Int,
    val no: Int,
    var quest:String="",
    var oinm:Boolean = false
    )

data class Todo2(
    val uid: String,
    val workTodo: String,
    var state: Int,
    var quest:String="",
    )

data class MyTodo(
    val uid: String,
    val workTodo: String,
    var state: Int,
    val no: Int,
//    var quest:String,
)