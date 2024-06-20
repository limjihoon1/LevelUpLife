package com.limjihoon.myhero.model

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.limjihoon.myhero.network.RetrofitHelper
import com.limjihoon.myhero.network.RetrofitService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MyBroadcastReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        // 밤 12시에 실행될 메서드 호출
        myMethod()
    }
}

fun myMethod() {

    val retrofit = RetrofitHelper.getRetrofitInstance("http://myhero.dothome.co.kr")
    val retrofitService = retrofit.create(RetrofitService::class.java)

    retrofitService.reset().enqueue(object : Callback<String> {
        override fun onResponse(p0: Call<String>, p1: Response<String>) {
            Log.d("성공", "${p1.body()}")
        }

        override fun onFailure(p0: Call<String>, p1: Throwable) {
            Log.d("실패", "${p1.message}")
        }

    })
}