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
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException
import java.net.HttpURLConnection
import java.net.SocketTimeoutException
import java.net.URLEncoder
import java.net.URL
import java.nio.charset.StandardCharsets
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.lifecycleOwner = this
        binding.uiState = WeatherUiState()

        setupCityInput()
        setupActions()
        requestWeather(getString(R.string.default_city))
    }

    private fun setupCityInput() {
        val cities = resources.getStringArray(R.array.popular_cities).toList()
        val adapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, cities)
        binding.cityInput.setAdapter(adapter)
        binding.cityInput.setOnItemClickListener { _, _, _, _ ->
            requestWeather(binding.cityInput.text?.toString())
        }
    }

    private fun setupActions() {
        binding.fetchButton.setOnClickListener {
            requestWeather(binding.cityInput.text?.toString())
        }
    }

    private fun requestWeather(rawCity: String?) {
        val city = rawCity?.trim().takeUnless { it.isNullOrEmpty() }
            ?: run {
                binding.cityInputLayout.error = getString(R.string.error_empty_city)
                return
            }
        binding.cityInputLayout.error = null

        lifecycleScope.launch {
            updateUiState { state -> state.asLoading() }
            runCatching { loadWeather(city) }
                .onSuccess { data ->
                    val formattedTime = formatUpdateTime(data.fetchedAtMillis)
                    val state = WeatherUiState.fromData(
                        data = data,
                        lastUpdateLabel = getString(R.string.label_last_update, formattedTime)
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

    private suspend fun loadWeather(city: String): WeatherData = withContext(Dispatchers.IO) {
        val apiKey = getString(R.string.openweather_api_key)
        if (apiKey.isBlank()) throw IllegalStateException("API key не задан")
        val encodedCity = URLEncoder.encode(city, StandardCharsets.UTF_8.toString())
        val weatherURL =
            "https://api.openweathermap.org/data/2.5/weather?q=$encodedCity&appid=$apiKey&units=metric&lang=ru"
        val connection = (URL(weatherURL).openConnection() as HttpURLConnection).apply {
            connectTimeout = 10_000
            readTimeout = 10_000
            requestMethod = "GET"
        }
        try {
            val responseCode = connection.responseCode
            val stream = if (responseCode == HttpURLConnection.HTTP_OK) {
                connection.inputStream
            } else {
                connection.errorStream
            }
            val payload = stream?.bufferedReader()?.use { it.readText() }.orEmpty()
            if (payload.isBlank()) {
                throw IOException("Пустой ответ сервера")
            }
            if (responseCode != HttpURLConnection.HTTP_OK) {
                throw IOException("HTTP $responseCode: $payload")
            }
            parseWeatherResponse(payload, city)
        } finally {
            connection.disconnect()
        }
    }

    private fun parseWeatherResponse(payload: String, fallbackCity: String): WeatherData {
        try {
            val json = JSONObject(payload)
            val main = json.optJSONObject("main") ?: throw JSONException("Секция main отсутствует")
            val weatherFirst = json.optJSONArray("weather")?.optJSONObject(0)
            val wind = json.optJSONObject("wind")
            val clouds = json.optJSONObject("clouds")
            return WeatherData(
                city = json.optString("name", fallbackCity),
                temperature = main.optDouble("temp"),
                feelsLike = main.optDouble("feels_like"),
                description = weatherFirst?.optString("description").orEmpty(),
                humidity = main.optInt("humidity"),
                pressure = main.optInt("pressure"),
                windSpeed = wind?.optDouble("speed") ?: 0.0,
                windDeg = wind?.optInt("deg") ?: 0,
                cloudiness = clouds?.optInt("all") ?: 0,
                fetchedAtMillis = System.currentTimeMillis(),
            )
        } catch (error: JSONException) {
            throw IOException("Ошибка разбора ответа", error)
        }
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
        is SocketTimeoutException -> "Превышено время ожидания"
        is java.net.UnknownHostException -> "Нет соединения"
        is IOException -> message ?: getString(R.string.error_unknown)
        else -> message ?: getString(R.string.error_unknown)
    }
}