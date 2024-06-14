package com.limjihoon.myhero.data

data class Member(
    val email: String = "",
    val uid: String = "",
    val nickname: String = "",
    val hero: Int = 0,
)

data class Member2(
    val no: Int = 0,
    val email: String = "",
    val uid: String = "",
    val nickname: String = "",
    val level: Int = 0,
    val exp: Int = 0,
    val coin: Int = 0,
    val hero: Int = 0,
    val qcc: Int = 0,
)

data class AdminMember(
    val no: Int = 0,
    val email: String = "",
    val uid: String = "",
    val nickname: String = "",
    val level: Int = 0,
    val exp: Int = 0,
    val coin: Int = 0,
    val hero: Int = 0,
    val qcc: Int = 0,
)

data class Inventory(
    val char1: Int = 0,
    val char2: Int = 0,
    val char3: Int = 0,
    val char4: Int = 0,
    val char5: Int = 0,
    val char6: Int = 0,
    val char7: Int = 0,
    val char8: Int = 0,
    val char9: Int = 0,
    val char10: Int = 0,
    val char11: Int = 0,
    val charHiden: Int = 0,
)
