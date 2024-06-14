package com.limjihoon.myhero.adapter

import android.app.AlertDialog
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.limjihoon.myhero.data.Board
import com.limjihoon.myhero.databinding.RecyclAdminBoardBinding
import com.limjihoon.myhero.network.RetrofitHelper
import com.limjihoon.myhero.network.RetrofitService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AdminBoardRecyclerAdapter(val context: Context, val items: List<Board>) :
    RecyclerView.Adapter<AdminBoardRecyclerAdapter.VH>() {
    inner class VH(val binding: RecyclAdminBoardBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val binding = RecyclAdminBoardBinding.inflate(LayoutInflater.from(context), parent, false)
        return VH(binding)
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: VH, position: Int) {
        val item = items[position]
        holder.binding.tvNo.text = "${item.no}번 글"
        holder.binding.tvLevel.text = "LV : ${item.level}"
        holder.binding.tvNickname.text = item.nickname
        holder.binding.tvContent.text = item.content

        holder.binding.delete.setOnClickListener {

            val retrofit = RetrofitHelper.getRetrofitInstance("http://myhero.dothome.co.kr")
            val retrofitService = retrofit.create(RetrofitService::class.java)

            retrofitService.adminDeleteBoard(item.no).enqueue(object :
                Callback<String> {
                override fun onResponse(p0: Call<String>, p1: Response<String>) {
                    AlertDialog.Builder(context).setTitle("게시글 삭제")
                        .setMessage("게시글을 삭제 하시겠습니까?").setPositiveButton("확인") { dialog, id ->
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