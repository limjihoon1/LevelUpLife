package com.limjihoon.myhero.data

data class ChatGPTRequest(
    val model: String,
    val messages: List<Message>
)

data class Message(
    val role: String,
    val content: String
)

data class ChatGPTResponse(
    val choices: List<Choices>
)

data class Choices(
    val message: Message
)

data class AnalysisResult(
    val myTodoList: List<MyTodoList>,
    val recommendTodo: List<RecommendTodo>,
    val msg: String
)

data class MyTodoList(
    val todo: String,
    val percentage: Float
)

data class RecommendTodo(
    val todo: String,
    val percentage: Float
)

data class LegendItem(
    val color: Int,
    val label: String
)

data class LegendItem2(
    val color: Int,
    val label: String
)