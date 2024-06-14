package com.limjihoon.myhero.activitis

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.limjihoon.myhero.R
import com.limjihoon.myhero.adapter.AdminMemberRecyclerAdapter
import com.limjihoon.myhero.data.AdminMember
import com.limjihoon.myhero.databinding.ActivityMemberManageBinding
import com.limjihoon.myhero.network.RetrofitHelper
import com.limjihoon.myhero.network.RetrofitService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MemberManageActivity : AppCompatActivity() {
    private val binding by lazy { ActivityMemberManageBinding.inflate(layoutInflater) }
    private val items = mutableListOf<AdminMember>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.recyclerView.adapter = AdminMemberRecyclerAdapter(this, items)

        loadData()
    }

    private fun loadData() {
        val retrofit = RetrofitHelper.getRetrofitInstance("http://myhero.dothome.co.kr")
        val retrofitService = retrofit.create(RetrofitService::class.java)

        retrofitService.adminGetMember().enqueue(object : Callback<List<AdminMember>> {
            @SuppressLint("NotifyDataSetChanged")
            override fun onResponse(p0: Call<List<AdminMember>>, p1: Response<List<AdminMember>>) {
                val data = p1.body()

                data?.let {
                    items.clear()
                    items.addAll(it)
                    binding.recyclerView.adapter?.notifyDataSetChanged()
                }
            }

            override fun onFailure(p0: Call<List<AdminMember>>, p1: Throwable) {
                Log.d("err", p1.message.toString())
            }

        })

    }
}