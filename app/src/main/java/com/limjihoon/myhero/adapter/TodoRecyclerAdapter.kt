package com.limjihoon.myhero.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.limjihoon.myhero.data.Todo
import com.limjihoon.myhero.databinding.RecyclHomeBinding

class TodoRecyclerAdapter(val context: Context, val items: List<Todo>) : RecyclerView.Adapter<TodoRecyclerAdapter.VH>() {
    inner class VH(val binding: RecyclHomeBinding) : ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val binding = RecyclHomeBinding.inflate(LayoutInflater.from(context), parent, false)
        return VH(binding)
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: VH, position: Int) {
        val item = items[position]
    }

}