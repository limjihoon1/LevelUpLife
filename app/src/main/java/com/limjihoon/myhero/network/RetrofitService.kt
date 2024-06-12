package com.limjihoon.myhero.network

import com.limjihoon.myhero.data.Member
import com.limjihoon.myhero.data.Member2
import com.limjihoon.myhero.data.Todo
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface RetrofitService {

    @GET("/levelUpLife/member/getEmail.php")  //파일질라 경로                //이과정은 동일 이메일이 있는지 확인하는 과정
    fun getEmail(@Query("email") email: String): Call<Member>
//    fun getEmail(@Query("email") email: String) : Call<String>
    //        내가 찾을꺼내용    서버에 있는 자료 타입   data/Member

    @POST("/levelUpLife/member/insertMember.php")
    fun insertMember(@Body member: Member) : Call<String>

    @GET("/levelUpLife/member/getMember.php")
    fun getMember(@Query("uid") uid: String) : Call<Member2>

    @POST("/levelUpLife/member/insertTodo.php")
    fun insertTodo(@Body todo: Todo) : Call<String>

    @GET("/levelUpLife/member/getTodo.php")
    fun getTodo(@Query("uid") uid: String) : Call<List<Todo>>
//    fun getTodo(@Query("uid") uid: String) : Call<String>

}