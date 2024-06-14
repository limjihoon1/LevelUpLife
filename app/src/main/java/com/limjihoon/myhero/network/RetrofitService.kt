package com.limjihoon.myhero.network

import com.limjihoon.myhero.data.Inventory
import com.limjihoon.myhero.data.KakaoData
import com.limjihoon.myhero.data.Member
import com.limjihoon.myhero.data.Member2
import com.limjihoon.myhero.data.Todo
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Query

interface RetrofitService {

    @GET("/levelUpLife/member/getEmail.php")
    fun getEmail(@Query("email") email: String): Call<Member>

    @GET("/levelUpLife/member/getNickname.php")
    fun getNickname(@Query("nickname") nickname: String) : Call<Member>

    @POST("/levelUpLife/member/insertMember.php")
    fun insertMember(@Body member: Member): Call<String>

    @GET("/levelUpLife/member/getMember.php")
    fun getMember(@Query("uid") uid: String): Call<Member2>

    @GET("/levelUpLife/member/getInventory.php")
    fun getInventory(@Query("memberNo") memberNo: Int): Call<Inventory>

    @POST("/levelUpLife/member/insertTodo.php")
    fun insertTodo(@Body todo: Todo): Call<String>

    @GET("/levelUpLife/member/getTodo.php")
    fun getTodo(@Query("uid") uid: String): Call<List<Todo>>

    @Headers("Authorization: KakaoAK 3f16c86dce6a4075f70b6034a4edcd01")
    @GET("/v2/local/search/keyword.json?sort=distance")
    fun kakoDataSearch(
        @Query("query") query: String,
        @Query("x") longitude: String,
        @Query("y") latitude: String
    ): Call<KakaoData>

    @GET("/levelUpLife/member/DeleteTodo.php")
    fun deleteTodo(@Query("no") no: Int): Call<String>

    @GET("/levelUpLife/member/UpdateTodo.php")
    fun updateTodo(@Query("no") no: Int): Call<String>











    @GET("/levelUpLife/admin/adminGetMember.php")
    fun adminGetMember() : Call<List<AdminMember>>

    @GET("/levelUpLife/admin/adminDeleteMember.php")
    fun adminDeleteMember(@Query("no") no: Int, @Query("uid") uid: String) : Call<String>

    @GET("/levelUpLife/admin/adminGetBoard.php")
    fun adminGetBoard() : Call<List<Board>>

    @GET("/levelUpLife/admin/adminDeleteBoard.php")
    fun adminDeleteBoard(@Query("no") no: Int) : Call<String>

}
