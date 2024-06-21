package com.limjihoon.myhero.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.limjihoon.myhero.data.Markers

import com.limjihoon.myhero.databinding.RecyclMapBinding

class MapDrawerAdapter(val context: Context, val items: MutableList<Markers>) :Adapter<MapDrawerAdapter.VH>(){
    inner class VH(val binding: RecyclMapBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        return VH(RecyclMapBinding.inflate(LayoutInflater.from(context), parent, false))
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        var item = items[position]
        holder.binding.tvMapWorkTodo.text = item.workTodo

    }
}