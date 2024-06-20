package com.limjihoon.myhero.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.limjihoon.myhero.activitis.MainActivity
import com.limjihoon.myhero.data.Todo
import com.limjihoon.myhero.databinding.RecyclHome1Binding
import com.limjihoon.myhero.databinding.RecyclHomeBinding
import com.limjihoon.myhero.network.RetrofitHelper
import com.limjihoon.myhero.network.RetrofitService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TodoRecyclerAdapter(val context: Context, val items: MutableList<Todo>) : RecyclerView.Adapter<ViewHolder>() {
    private val ma = context as MainActivity

    inner class VH1(val binding: RecyclHomeBinding) : ViewHolder(binding.root)
    inner class VH2(val binding: RecyclHome1Binding) : ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return if (viewType == 0) VH1(RecyclHomeBinding.inflate(LayoutInflater.from(context), parent, false))
        else VH2(RecyclHome1Binding.inflate(LayoutInflater.from(context), parent, false))
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]

        if (item.quest == "normal") {
            val vh = holder as VH1
            vh.binding.tvWorkTodo.text = item.workTodo

            vh.binding.ivSuccess.setOnClickListener {
                updateTodo(position, ma.dataManager.memberFlow.value!!.exp, ma.dataManager.memberFlow.value!!.level, ma.dataManager.memberFlow.value!!.qcc)
//                Toast.makeText(context, "${ma.dataManager.memberFlow.value!!.exp}", Toast.LENGTH_SHORT).show()
            }

            vh.binding.ivDelete.setOnClickListener {
                deleteTodo(position, item.no)
//                Toast.makeText(context, "${ma.dataManager.memberFlow.value!!.exp}", Toast.LENGTH_SHORT).show()
            }
        } else {
            val vh = holder as VH2
            vh.binding.tvWorkTodo.text = item.workTodo

            vh.binding.ivSuccess.setOnClickListener {
                updateTodo(position, ma.dataManager.memberFlow.value!!.exp, ma.dataManager.memberFlow.value!!.level, ma.dataManager.memberFlow.value!!.qcc)
                Toast.makeText(context, "aaaaaaaa", Toast.LENGTH_SHORT).show()
            }

            vh.binding.ivDelete.setOnClickListener {
                deleteTodo(position, item.no)
                Toast.makeText(context, "aaaaaaaa", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun updateTodo(position: Int, exp: Int, level: Int, qcc: Int) {
        val retrofit = RetrofitHelper.getRetrofitInstance("http://myhero.dothome.co.kr")
        val retrofitService = retrofit.create(RetrofitService::class.java)

        try {
            retrofitService.updateQuest(items[position].no, items[position].uid, items[position].quest, exp, level, qcc).enqueue(object : Callback<String> {
                override fun onResponse(p0: Call<String>, p1: Response<String>) {
                    items.removeAt(position)
                    notifyItemRemoved(position)
                    ma.getMember()
                    Toast.makeText(context, "${p1.body()}", Toast.LENGTH_SHORT).show()
                }

                override fun onFailure(p0: Call<String>, p1: Throwable) {
                    Toast.makeText(context, "업데이트 에러: ${p1.message}", Toast.LENGTH_SHORT).show()
                    Log.d("error", "${p1.message}")
                }

            })
        } catch (e: Exception) {
            Log.d("tErr", "${e.message}")
        }



//        val response = retrofitService.updateQuest(items[position].no, items[position].uid, items[position].quest, exp).execute()
//
//        try {
//            if (response.isSuccessful && response.body() != null) {
//                items.removeAt(position)
//                Toast.makeText(context, "${response.body()}", Toast.LENGTH_SHORT).show()
//            }
//        } catch (e: Exception) {
//            Toast.makeText(context, "${e.message}", Toast.LENGTH_SHORT).show()
//        }


//        retrofitService.updateQuest(items[position].no, items[position].uid, items[position].quest).enqueue(object : Callback<String> {
//            override fun onResponse(call: Call<String>, response: Response<String>) {
//                if (response.isSuccessful) {
//                    if (response.body() == "업데이트 성공") {
//                        items[position].state = 1
//                        notifyItemChanged(position)
//                        Toast.makeText(context, "성공", Toast.LENGTH_SHORT).show()
//                    } else {
//                        Toast.makeText(context, "업데이트 실패: ${response.body()}", Toast.LENGTH_SHORT).show()
//                    }
//                } else {
//                    Toast.makeText(context, "업데이트 실패: ${response.message()}", Toast.LENGTH_SHORT).show()
//                }
//            }
//
//            override fun onFailure(call: Call<String>, t: Throwable) {
//                Toast.makeText(context, "업데이트 에러: ${t.message}", Toast.LENGTH_SHORT).show()
//                Log.d("error", "${t.message}")
//            }
//        })
    }

    private fun deleteTodo(position: Int, no: Int) {
        val retrofit = RetrofitHelper.getRetrofitInstance("http://myhero.dothome.co.kr")
        val retrofitService = retrofit.create(RetrofitService::class.java)

        try {
            retrofitService.deleteTodo(no).enqueue(object : Callback<String> {
                override fun onResponse(call: Call<String>, response: Response<String>) {
                    if (response.isSuccessful && response.body() == "삭제 성공") {
                        items.removeAt(position)
                        notifyItemRemoved(position)
                        Toast.makeText(context, "삭제 성공", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(context, "삭제 실패", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<String>, t: Throwable) {
                    Toast.makeText(context, "삭제 에러: ${t.message}", Toast.LENGTH_SHORT).show()
                    Log.d("error", "${t.message}")
                }
            })
        } catch (e: Exception) {
            Log.d("tErr", "${e.message}")
        }

    }

    override fun getItemViewType(position: Int): Int {
        return if (items[position].quest == "normal") 0 else 1
    }

}
