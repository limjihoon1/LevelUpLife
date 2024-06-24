package com.limjihoon.myhero.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.limjihoon.myhero.data.Markers
import com.limjihoon.myhero.databinding.RecyclMapBinding

class MapDrawerAdapter(
    val context: Context,
    val items: MutableList<Markers>,
    private val itemClickListener: OnItemClickListener
) : RecyclerView.Adapter<MapDrawerAdapter.VH>() {

    inner class VH(val binding: RecyclMapBinding) : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.root.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    itemClickListener.onItemClick(items[position])
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        return VH(RecyclMapBinding.inflate(LayoutInflater.from(context), parent, false))
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        val item = items[position]
        holder.binding.tvMapWorkTodo.text = item.workTodo
    }

    interface OnItemClickListener {
        fun onItemClick(marker: Markers)
    }
}