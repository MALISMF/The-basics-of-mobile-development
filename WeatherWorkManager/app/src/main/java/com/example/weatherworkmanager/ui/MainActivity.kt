package com.example.weatherworkmanager.ui

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.weatherworkmanager.R
import com.example.weatherworkmanager.databinding.ActivityMainBinding
import com.example.weatherworkmanager.viewmodel.WeatherViewModel

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: WeatherViewModel by viewModels()
    private lateinit var adapter: WeatherAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        // RecyclerView
        adapter = WeatherAdapter()
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = adapter

        // Наблюдаем за списком карточек
        viewModel.weatherItems.observe(this) { items ->
            adapter.submitList(items)
        }

        // Наблюдаем за статусом цепочки
        viewModel.chainState.observe(this) { state ->
            when (state) {
                WeatherViewModel.ChainState.IDLE -> {
                    binding.btnFetch.text = "Запустить цепочку"
                    binding.btnFetch.isEnabled = true
                    binding.progressBar.visibility = View.GONE
                    binding.tvStatus.text = "Нажмите кнопку для загрузки погоды"
                }
                WeatherViewModel.ChainState.RUNNING -> {
                    binding.btnFetch.text = "Выполняется..."
                    binding.btnFetch.isEnabled = false
                    binding.progressBar.visibility = View.VISIBLE
                    binding.tvStatus.text = "Цепочка WorkManager выполняется..."
                }
                WeatherViewModel.ChainState.SUCCEEDED -> {
                    binding.btnFetch.text = "Обновить"
                    binding.btnFetch.isEnabled = true
                    binding.progressBar.visibility = View.GONE
                    binding.tvStatus.text = "✓ Все города загружены"
                }
                WeatherViewModel.ChainState.FAILED -> {
                    binding.btnFetch.text = "Повторить"
                    binding.btnFetch.isEnabled = true
                    binding.progressBar.visibility = View.GONE
                    binding.tvStatus.text = "✗ Произошла ошибка"
                }
            }
        }

        binding.btnFetch.setOnClickListener {
            viewModel.fetchAllCities()
        }
    }
}
