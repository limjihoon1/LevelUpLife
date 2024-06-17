package com.limjihoon.myhero.activitis

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.limjihoon.myhero.G
import com.limjihoon.myhero.adapter.MyBoardRecyclerAdapter
import com.limjihoon.myhero.data.MyBoard
import com.limjihoon.myhero.databinding.ActivityMyBoardBinding
import com.limjihoon.myhero.network.RetrofitHelper
import com.limjihoon.myhero.network.RetrofitService
import retrofit2.Call
import retrofit2.Response

class MyBoardActivity : AppCompatActivity() {
    private val binding by lazy { ActivityMyBoardBinding.inflate(layoutInflater) }
    private val items = mutableListOf<MyBoard>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.recyclerView.adapter = MyBoardRecyclerAdapter(this, items)
        loadData()
    }

    private fun loadData() {
        val retrofit = RetrofitHelper.getRetrofitInstance("http://myhero.dothome.co.kr")
        val retrofitService = retrofit.create(RetrofitService::class.java)

        retrofitService.getMyBoard(G.uid).enqueue(object : retrofit2.Callback<List<MyBoard>> {
            @SuppressLint("NotifyDataSetChanged")
            override fun onResponse(p0: Call<List<MyBoard>>, p1: Response<List<MyBoard>>) {
                val data = p1.body()

                data?.let {
                    items.clear()
                    items.addAll(it)
                    binding.recyclerView.adapter?.notifyDataSetChanged()
                }
            }

            override fun onFailure(p0: Call<List<MyBoard>>, p1: Throwable) {
                Log.d("err", p1.message.toString())
            }

        })
    }
}