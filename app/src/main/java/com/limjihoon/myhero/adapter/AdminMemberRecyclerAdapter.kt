package com.limjihoon.myhero.adapter

import android.app.AlertDialog
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.limjihoon.myhero.data.AdminMember
import com.limjihoon.myhero.databinding.RecyclAdminMemberBinding
import com.limjihoon.myhero.network.RetrofitHelper
import com.limjihoon.myhero.network.RetrofitService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AdminMemberRecyclerAdapter(val context: Context, val items: List<AdminMember>) :
    RecyclerView.Adapter<AdminMemberRecyclerAdapter.VH>() {

    inner class VH(val binding: RecyclAdminMemberBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val binding = RecyclAdminMemberBinding.inflate(LayoutInflater.from(context), parent, false)
        return VH(binding)
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: VH, position: Int) {
        val item = items[position]
        holder.binding.tvNo.text = "${item.no}"
        holder.binding.tvLevel.text = "LV : ${item.level}"
        holder.binding.tvNickname.text = item.nickname
        holder.binding.tvEmail.text = item.email

        holder.binding.delete.setOnClickListener {

            val retrofit = RetrofitHelper.getRetrofitInstance("http://myhero.dothome.co.kr")
            val retrofitService = retrofit.create(RetrofitService::class.java)

            retrofitService.adminDeleteMember(item.no, item.uid).enqueue(object : Callback<String> {
                override fun onResponse(p0: Call<String>, p1: Response<String>) {
                    AlertDialog.Builder(context).setTitle("회원 삭제")
                        .setMessage("회원을 삭제 하시겠습니까?").setPositiveButton("확인") { dialog, id ->
                            Toast.makeText(context, "${p1.body()}", Toast.LENGTH_SHORT).show()
                        }.setNegativeButton("취소") { dialog, id ->
                            dialog.dismiss()
                        }.create().show()

                }

                override fun onFailure(p0: Call<String>, p1: Throwable) {
                    Log.d("err", p1.message.toString())
                }

            })
        }
    }
}