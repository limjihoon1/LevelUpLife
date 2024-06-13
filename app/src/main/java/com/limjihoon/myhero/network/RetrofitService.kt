package com.limjihoon.myhero.network

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

    @GET("/levelUpLife/member/getEmail.php")  //파일질라 경로  //이과정은 동일 이메일이 있는지 확인하는 과정
    fun getEmail(@Query("email") email: String): Call<Member>
    // fun getEmail(@Query("email") email: String) : Call<String>
    // 내가 찾을꺼내용 서버에 있는 자료 타입   data/Member

    @GET("/levelUpLife/member/getNickname.php")
    fun getNickname(@Query("nickname") nickname: String) : Call<Member>

    @POST("/levelUpLife/member/insertMember.php")
    fun insertMember(@Body member: Member): Call<String>

    @GET("/levelUpLife/member/getMember.php")
    fun getMember(@Query("uid") uid: String): Call<Member2>

    @POST("/levelUpLife/member/insertTodo.php")
    fun insertTodo(@Body todo: Todo): Call<String>

    @GET("/levelUpLife/member/getTodo.php")
    fun getTodo(@Query("uid") uid: String): Call<List<Todo>>

    //    fun getTodo(@Query("uid") uid: String) : Call<String>
    @Headers("Authorization: KakaoAK 3f16c86dce6a4075f70b6034a4edcd01")
    @GET("/v2/local/search/keyword.json?sort=distance")
    fun kakoDataSearch(
        @Query("query") query: String,
        @Query("x") longitude: String,
        @Query("y") latitude: String
    ): Call<KakaoData>

    @GET("/levelUpLife/member/DeleteTodo.php")
    fun deleteTodo(@Query("no") no: Int): Call<String>       //Delete todo를 삭제하는거 서버 리사이클러뷰 보이는것 완전 삭제

    @GET("/levelUpLife/member/UpdateTodo.php")
    fun updateTodo(@Query("no") no: Int): Call<String>     //Update todo를 수정하면서 state값을 0에서 1로 만드는것 리사이클러뷰에는 리스트 사라짐

}