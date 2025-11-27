package com.example.weatherdata

import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import com.example.weatherdata.databinding.ActivityMainBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.io.IOException
import java.io.InputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var currentTemperatureScale = TemperatureScale.CELSIUS
    private var showWindDirection = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.lifecycleOwner = this
        binding.uiState = WeatherUiState()

        setupCitySpinner()
        setupTemperatureScale()
        setupWindDirectionCheckbox()
        setupActions()
        
        // Load default city weather
        val defaultCity = getString(R.string.default_city)
        val cities = resources.getStringArray(R.array.popular_cities)
        val defaultIndex = cities.indexOf(defaultCity).takeIf { it >= 0 } ?: 0
        binding.citySpinner.setSelection(defaultIndex)
        requestWeather(defaultCity)
    }

    private fun setupCitySpinner() {
        val cities = resources.getStringArray(R.array.popular_cities)
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, cities)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.citySpinner.adapter = adapter
        
        binding.citySpinner.onItemSelectedListener = object : android.widget.AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: android.widget.AdapterView<*>?, view: android.view.View?, position: Int, id: Long) {
                val selectedCity = cities[position]
                requestWeather(selectedCity)
            }
            override fun onNothingSelected(parent: android.widget.AdapterView<*>?) {}
        }
    }

    private fun setupTemperatureScale() {
        binding.temperatureScaleGroup.setOnCheckedChangeListener { _, checkedId ->
            currentTemperatureScale = when (checkedId) {
                R.id.radioCelsius -> TemperatureScale.CELSIUS
                R.id.radioFahrenheit -> TemperatureScale.FAHRENHEIT
                else -> TemperatureScale.CELSIUS
            }
            updateTemperatureDisplay()
        }
    }

    private fun setupWindDirectionCheckbox() {
        binding.showWindDirectionCheckbox.setOnCheckedChangeListener { _, isChecked ->
            showWindDirection = isChecked
            updateWindDirectionVisibility()
        }
    }

    private fun setupActions() {
        binding.fetchButton.setOnClickListener {
            val selectedPosition = binding.citySpinner.selectedItemPosition
            val cities = resources.getStringArray(R.array.popular_cities)
            if (selectedPosition >= 0 && selectedPosition < cities.size) {
                requestWeather(cities[selectedPosition])
            }
        }
    }

    private fun requestWeather(city: String) {
        if (city.isBlank()) {
            return
        }

        lifecycleScope.launch {
            updateUiState { state -> state.asLoading() }
            runCatching { loadWeatherFromJson(city) }
                .onSuccess { data ->
                    val formattedTime = formatUpdateTime(data.fetchedAtMillis)
                    val state = WeatherUiState.fromData(
                        data = data,
                        lastUpdateLabel = getString(R.string.label_last_update, formattedTime),
                        temperatureScale = currentTemperatureScale,
                        showWindDirection = showWindDirection
                    )
                    binding.uiState = state
                }
                .onFailure { throwable ->
                    val message = getString(
                        R.string.error_loading_weather,
                        throwable.userMessage()
                    )
                    updateUiState { state -> state.withError(message) }
                }
        }
    }

    private suspend fun loadWeatherFromJson(city: String): WeatherData = withContext(Dispatchers.IO) {
        try {
            val inputStream: InputStream = assets.open("weather_data.json")
            val jsonString = inputStream.bufferedReader().use { it.readText() }
            val jsonObject = JSONObject(jsonString)
            
            val cityData = jsonObject.optJSONObject(city)
                ?: throw IOException("Данные для города '$city' не найдены")
            
            WeatherData(
                city = cityData.optString("city", city),
                temperature = cityData.optDouble("temperature"),
                feelsLike = cityData.optDouble("feelsLike"),
                description = cityData.optString("description", ""),
                humidity = cityData.optInt("humidity"),
                pressure = cityData.optInt("pressure"),
                windSpeed = cityData.optDouble("windSpeed"),
                windDeg = cityData.optInt("windDeg"),
                cloudiness = cityData.optInt("cloudiness"),
                fetchedAtMillis = System.currentTimeMillis(),
            )
        } catch (e: Exception) {
            throw IOException("Ошибка загрузки данных: ${e.message}", e)
        }
    }

    private fun updateTemperatureDisplay() {
        val currentState = binding.uiState ?: return
        if (currentState.temperature != null) {
            val updatedState = currentState.copy(temperatureScale = currentTemperatureScale)
            binding.uiState = updatedState
        }
    }

    private fun updateWindDirectionVisibility() {
        val currentState = binding.uiState ?: return
        val updatedState = currentState.copy(showWindDirection = showWindDirection)
        binding.uiState = updatedState
    }

    private fun formatUpdateTime(timestamp: Long): String {
        val formatter = SimpleDateFormat("HH:mm", Locale.getDefault())
        return formatter.format(Date(timestamp))
    }

    private inline fun updateUiState(transform: (WeatherUiState) -> WeatherUiState) {
        val current = binding.uiState ?: WeatherUiState()
        binding.uiState = transform(current)
    }

    private fun Throwable.userMessage(): String = when (this) {
        is IOException -> message ?: getString(R.string.error_unknown)
        else -> message ?: getString(R.string.error_unknown)
    }
}