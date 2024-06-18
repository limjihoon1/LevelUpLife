package com.limjihoon.myhero.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.limjihoon.myhero.data.MyBoard
import com.limjihoon.myhero.databinding.RecyclMyBoardBinding

class MyBoardRecyclerAdapter(val context: Context, val items: List<MyBoard>) : RecyclerView.Adapter<MyBoardRecyclerAdapter.VH>() {
    inner class VH(val binding: RecyclMyBoardBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val binding = RecyclMyBoardBinding.inflate(LayoutInflater.from(context), parent, false)
        return VH(binding)
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: VH, position: Int) {
        val item = items[position]
        holder.binding.tvNo.text = item.no.toString()
        holder.binding.tvContent.text = item.content
        holder.binding.tvLevel.text = item.level
        holder.binding.tvNickname.text = item.nickname
    }
}