package com.limjihoon.myhero.network

import com.limjihoon.myhero.data.AdminMember
import com.limjihoon.myhero.data.Board
import com.limjihoon.myhero.data.Inventory
import com.limjihoon.myhero.data.KakaoData
import com.limjihoon.myhero.data.Markers
import com.limjihoon.myhero.data.Member
import com.limjihoon.myhero.data.Member2
import com.limjihoon.myhero.data.MyBoard
import com.limjihoon.myhero.data.MyTodo
import com.limjihoon.myhero.data.Todo
import com.limjihoon.myhero.data.Todo2
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Query

interface RetrofitService {

    @GET("/levelUpLife/map/LoadMap.php")
    fun loadMap(@Query("todoUid") todoUid: String): Call<List<Markers>>

    @GET("/levelUpLife/map/LoadMap.php")
    fun loadMap2(@Query("todoUid") todoUid: String): Call<String>

    @GET("/levelUpLife/map/GetAllMarkers.php")
    fun getAllMarkers(): Call<List<Markers>>

    @GET("/levelUpLife/map/insertMap.php")
    fun insertMap(
        @Query("todoUid") todouid: String,
        @Query("workTodo") worktodo: String,
        @Query("lat") lat: Double,
        @Query("lng") lng: Double,
        @Query("state") state: Int,
    ): Call<String>

    @GET("/levelUpLife/member/getEmail.php")
    fun getEmail(@Query("email") email: String): Call<Member>

    @GET("/levelUpLife/member/getNickname.php")
    fun getNickname(@Query("nickname") nickname: String): Call<Member>

    @POST("/levelUpLife/member/insertMember.php")
    fun insertMember(@Body member: Member): Call<String>

    @GET("/levelUpLife/member/getMember.php")
    fun getMember(@Query("uid") uid: String): Call<Member2>

    @GET("/levelUpLife/member/getInventory.php")
    fun getInventory(@Query("memberNo") memberNo: Int): Call<Inventory>

    @POST("/levelUpLife/todo/insertTodo.php")
    fun insertTodo(@Body todo: Todo2): Call<String>

    @GET("/levelUpLife/member/getTodo.php")
    fun getTodo(@Query("uid") uid: String): Call<List<Todo>>


    @GET("/levelUpLife/member/DeleteTodo.php")
    fun deleteTodo(@Query("no") no: Int, @Query("quest") quest: String): Call<String>

    @GET("/levelUpLife/member/UpdateTodo.php")
    fun updateTodo(@Query("no") no: Int): Call<String>








    @Headers("Authorization: KakaoAK 3f16c86dce6a4075f70b6034a4edcd01")
    @GET("/v2/local/search/keyword.json?sort=distance")
    fun kakoDataSearch(
        @Query("query") query: String,
        @Query("x") longitude: String,
        @Query("y") latitude: String
    ): Call<KakaoData>
    //카카오 로컬 검색 api 요청해주는 코드 만들어줘 우선 응답type 을  :KakaoData
    @Headers("Authorization: KakaoAK 3f16c86dce6a4075f70b6034a4edcd01")
    @GET("/v2/local/search/keyword.json?sort=distance")
    fun kakaoSearchPlaceToString3(
        @Query("query") query: String,
        @Query("x") longitude: String,
        @Query("y") latitude: String
    ): Call<KakaoData>

    @Headers("Authorization: KakaoAK 3f16c86dce6a4075f70b6034a4edcd01")
    @GET("/v2/local/search/keyword.json?sort=distance")
    fun kakaoSearchPlaceToString2(
        @Query("x") longitude: String,
        @Query("y") latitude: String
    ): Call<KakaoData>


    @GET("/levelUpLife/todo/UpdateTodoQuest.php")
    fun updateQuest(
        @Query("no") no: Int,
        @Query("uid") uid: String,
        @Query("quest") quest: String,
        @Query("exp") exp: Int,
        @Query("level") level: Int,
        @Query("qcc") qcc: Int
    ): Call<String>


    @GET("/levelUpLife/member/updateHero.php")
    fun updateHero(@Query("uid") uid: String, @Query("hero") hero: Int): Call<String>

    @GET("/levelUpLife/member/updateInventory.php")
    fun updateInventory(@Query("uid") uid: String, @Query("hero") hero: Int): Call<String>

    @GET("/levelUpLife/member/updateInventoryHiden.php")
    fun updateInventoryHiden(@Query("uid") uid: String): Call<String>








    @GET("/levelUpLife/admin/adminGetMember.php")
    fun adminGetMember() : Call<List<AdminMember>>

    @GET("/levelUpLife/admin/adminDeleteMember.php")
    fun adminDeleteMember(@Query("no") no: Int, @Query("uid") uid: String) : Call<String>

    @GET("/levelUpLife/admin/adminGetBoard.php")
    fun adminGetBoard() : Call<List<Board>>

    @GET("/levelUpLife/admin/adminDeleteBoard.php")
    fun adminDeleteBoard(@Query("no") no: Int) : Call<String>








    @GET("/levelUpLife/my/getMyBoard.php")
    fun getMyBoard(@Query("uid") uid: String) : Call<List<MyBoard>>

    @GET("/levelUpLife/my/getMyTodo.php")
    fun  getMyTodo(@Query("uid") uid: String) : Call<List<MyTodo>>

    @GET("/levelUpLife/my/myMemberOut.php")
    fun myMemberOut(@Query("uid") uid: String) : Call<String>

    @GET("/levelUpLife/my/aaa.php")
    fun aaa() : Call<String>

    @GET("/levelUpLife/my/reset.php")
    fun reset() : Call<String>

}
