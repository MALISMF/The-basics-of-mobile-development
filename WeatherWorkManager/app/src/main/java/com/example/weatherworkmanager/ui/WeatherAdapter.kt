package com.example.weatherworkmanager.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherworkmanager.databinding.ItemWeatherBinding
import com.example.weatherworkmanager.model.CityWeatherItem

class WeatherAdapter :
    ListAdapter<CityWeatherItem, WeatherAdapter.WeatherViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeatherViewHolder {
        val binding = ItemWeatherBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return WeatherViewHolder(binding)
    }

    override fun onBindViewHolder(holder: WeatherViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class WeatherViewHolder(
        private val binding: ItemWeatherBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: CityWeatherItem) {
            binding.item = item
            binding.executePendingBindings()
        }
    }

    companion object DiffCallback : DiffUtil.ItemCallback<CityWeatherItem>() {
        override fun areItemsTheSame(old: CityWeatherItem, new: CityWeatherItem) =
            old.city == new.city

        override fun areContentsTheSame(old: CityWeatherItem, new: CityWeatherItem) =
            old == new
    }
}
