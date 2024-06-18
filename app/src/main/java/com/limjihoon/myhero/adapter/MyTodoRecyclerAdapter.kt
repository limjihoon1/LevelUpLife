package com.limjihoon.myhero.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.limjihoon.myhero.data.MyTodo
import com.limjihoon.myhero.databinding.RecyclMyTodoBinding

class MyTodoRecyclerAdapter(val context: Context, val items: List<MyTodo>) :
    RecyclerView.Adapter<MyTodoRecyclerAdapter.VH>() {

    inner class VH(val binding: RecyclMyTodoBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val binding = RecyclMyTodoBinding.inflate(LayoutInflater.from(context), parent, false)
        return VH(binding)
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: VH, position: Int) {
        val item = items[position]
        holder.binding.tvTodo.text = item.workTodo

    }
}