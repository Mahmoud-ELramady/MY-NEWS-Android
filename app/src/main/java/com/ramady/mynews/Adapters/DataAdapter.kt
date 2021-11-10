package com.ramady.mynews.Adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.jackandphantom.carouselrecyclerview.view.ReflectionImageView
import com.ramady.mynews.R
import com.ramady.mynews.models.DataCategory

class DataAdapter (private var list : List<DataCategory>): RecyclerView.Adapter<DataAdapter.ViewHolder>() {



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater =
            LayoutInflater.from(parent.context).inflate(R.layout.category_item, parent, false)
        return ViewHolder(inflater)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Glide.with(holder.image).load(list.get(position).imageCategory).into(holder.image)
    }

    fun updateData(list: List<DataCategory>) {
        this.list = list
        notifyDataSetChanged()
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val image: ReflectionImageView = itemView.findViewById(R.id.image)
    }

}