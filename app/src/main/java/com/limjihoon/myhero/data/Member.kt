package com.limjihoon.myhero.data

data class Member(
    val email: String = "",
    val uid: String = "",
    val nickname: String = "",
    val hero: Int = 0,
)

data class Member2(
    val email: String = "",
    val uid: String = "",
    val nickname: String = "",
    val level: Int = 0,
    val exp: Int = 0,
    val coin: Int = 0,
    val hero: Int = 0,
)
