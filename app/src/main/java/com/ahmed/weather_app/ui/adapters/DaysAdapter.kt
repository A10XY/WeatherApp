package com.ahmed.weather_app.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ahmed.weather_app.data.DaysInfo
import com.ahmed.weather_app.databinding.LayoutRvDaysItemBinding

class DaysAdapter(private val list: List<DaysInfo>) : RecyclerView.Adapter<DaysAdapter.TimesViewHolder>() {
    inner class TimesViewHolder(val binding: LayoutRvDaysItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TimesViewHolder = TimesViewHolder(LayoutRvDaysItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: TimesViewHolder, position: Int) {
        holder.binding.run {
            ivDayOrNight.setImageResource(list[position].icon)
            tvTime.text = list[position].day
            tvTemperature.text = list[position].temperature
        }
    }

    override fun getItemCount(): Int = list.size
}