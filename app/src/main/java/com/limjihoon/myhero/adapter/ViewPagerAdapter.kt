package com.limjihoon.myhero.adapter
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.limjihoon.myhero.R

class ViewPagerAdapter(private val context: Context) : RecyclerView.Adapter<ViewPagerAdapter.ViewHolder>() {

    private val layouts = listOf(

        R.layout.custum_dialog_viewpager_page_first,
        R.layout.custum_dialog_viewpager_page_second,
        R.layout.custum_dialog_viewpager_page_third

    )

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(viewType, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        // 페이지에 필요한 데이터 바인딩
    }

    override fun getItemViewType(position: Int): Int {
        return layouts[position]
    }

    override fun getItemCount(): Int {
        return layouts.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}