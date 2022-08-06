package com.ahmed.weather_app.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.ahmed.weather_app.data.MoreInfo
import com.ahmed.weather_app.databinding.LayoutRvMoreIfoItemBinding

class MoreInfoAdapter(private val list: List<MoreInfo>) : RecyclerView.Adapter<MoreInfoAdapter.MoreInfoViewHolder>() {
    inner class MoreInfoViewHolder(val binding: LayoutRvMoreIfoItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MoreInfoViewHolder = MoreInfoViewHolder(LayoutRvMoreIfoItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: MoreInfoViewHolder, position: Int) {
        holder.binding.run {
            tvInfoTitle.text = list[position].infoTitle
            tvInfoQuantity.text = list[position].infoQuantity
            ivInfoIcon.setImageResource(list[position].infoIcon)
        }
        if (position == list.lastIndex) holder.binding.md.isVisible = false
    }

    override fun getItemCount(): Int = list.size
}