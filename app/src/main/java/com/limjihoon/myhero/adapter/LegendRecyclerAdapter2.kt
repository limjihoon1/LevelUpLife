package com.limjihoon.myhero.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.limjihoon.myhero.data.LegendItem2
import com.limjihoon.myhero.databinding.LegendItemBinding

class LegendRecyclerAdapter2(val context: Context, val items: MutableList<LegendItem2>) : RecyclerView.Adapter<LegendRecyclerAdapter2.VH>() {
    inner class VH(val binding: LegendItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val binding = LegendItemBinding.inflate(LayoutInflater.from(context), parent, false)
        return VH(binding)
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: VH, position: Int) {
        val item = items[position]
        holder.binding.colorView.setBackgroundColor(item.color)
        holder.binding.labelTextView.text = item.label
    }
}