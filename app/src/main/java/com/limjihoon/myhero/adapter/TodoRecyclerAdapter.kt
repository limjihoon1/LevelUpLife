package com.limjihoon.myhero.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.limjihoon.myhero.data.Todo
import com.limjihoon.myhero.databinding.RecyclHomeBinding
import com.limjihoon.myhero.databinding.RecyclHome2Binding
import com.limjihoon.myhero.network.RetrofitHelper
import com.limjihoon.myhero.network.RetrofitService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TodoRecyclerAdapter(
    private val context: Context,
    private val items: MutableList<Todo>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        const val TYPE_NORMAL = 0
        const val TYPE_MAP = 1
    }

    override fun getItemViewType(position: Int): Int {
        return if (items[position].quest == "map") TYPE_MAP else TYPE_NORMAL
    }

    inner class NormalViewHolder(val binding: RecyclHomeBinding) : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.ivDelete.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val item = items[position]
                    deleteTodoItem(item.no, position)
                }
            }

            binding.ivSuccess.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val item = items[position]
                    updateTodoState(item.no, item.uid, "normal", position)
                }
            }
        }
    }

    inner class MapViewHolder(val binding: RecyclHome2Binding) : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.ivDelete.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val item = items[position]
                    deleteTodoItem(item.no, position)
                }
            }

            binding.ivSuccess.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val item = items[position]
                    updateTodoState(item.no, item.uid, "map", position)
                }
            }
        }
    }

    private fun deleteTodoItem(no: Int, position: Int) {
        val retrofit = RetrofitHelper.getRetrofitInstance("http://myhero.dothome.co.kr")
        val retrofitService = retrofit.create(RetrofitService::class.java)

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
    }

    private fun updateTodoState(no: Int, uid: String, quest: String, position: Int) {
        val retrofit = RetrofitHelper.getRetrofitInstance("http://myhero.dothome.co.kr")
        val retrofitService = retrofit.create(RetrofitService::class.java)

        retrofitService.updateQuest(no, uid, quest).enqueue(object : Callback<String> {
            override fun onResponse(call: Call<String>, response: Response<String>) {
                if (response.isSuccessful) {
                    if (response.body() == "업데이트 성공") {
                        items[position].state = 1
                        notifyItemChanged(position)
                        Toast.makeText(context, "성공", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(context, "업데이트 실패: ${response.body()}", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(context, "업데이트 실패: ${response.message()}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<String>, t: Throwable) {
                Toast.makeText(context, "업데이트 에러: ${t.message}", Toast.LENGTH_SHORT).show()
                Log.d("error", "${t.message}")
            }
        })
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == TYPE_MAP) {
            val binding = RecyclHome2Binding.inflate(LayoutInflater.from(context), parent, false)
            MapViewHolder(binding)
        } else {
            val binding = RecyclHomeBinding.inflate(LayoutInflater.from(context), parent, false)
            NormalViewHolder(binding)
        }
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = items[position]
        if (holder is NormalViewHolder) {
            holder.binding.tvWorkTodo.text = item.workTodo
        } else if (holder is MapViewHolder) {
            holder.binding.tvMapTodo.text = item.workTodo
        }
    }
}
