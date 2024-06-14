package com.limjihoon.myhero.activitis

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.limjihoon.myhero.R
import com.limjihoon.myhero.adapter.AdminBoardRecyclerAdapter
import com.limjihoon.myhero.data.Board
import com.limjihoon.myhero.databinding.ActivityBoardManageBinding
import com.limjihoon.myhero.network.RetrofitHelper
import com.limjihoon.myhero.network.RetrofitService
import retrofit2.Call
import retrofit2.Response

class BoardManageActivity : AppCompatActivity() {
    private val binding by lazy { ActivityBoardManageBinding.inflate(layoutInflater) }
    private val items = mutableListOf<Board>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.recyclerView.adapter = AdminBoardRecyclerAdapter(this, items)

        loadData()
    }

    private fun loadData() {
        val retrofit = RetrofitHelper.getRetrofitInstance("http://myhero.dothome.co.kr")
        val retrofitService = retrofit.create(RetrofitService::class.java)

        retrofitService.adminGetBoard().enqueue(object : retrofit2.Callback<List<Board>> {
            @SuppressLint("NotifyDataSetChanged")
            override fun onResponse(p0: Call<List<Board>>, p1: Response<List<Board>>) {
                val data = p1.body()

                data?.let {
                    items.clear()
                    items.addAll(it)
                    binding.recyclerView.adapter?.notifyDataSetChanged()
                }
            }

            override fun onFailure(p0: Call<List<Board>>, p1: Throwable) {
                Log.d("err", p1.message.toString())
            }

        })
    }
}