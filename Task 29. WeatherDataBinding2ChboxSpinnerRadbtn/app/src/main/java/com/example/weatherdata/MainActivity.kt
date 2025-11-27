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
import java.io.File
import java.io.FileWriter
import java.io.IOException
import java.io.InputStream
import java.net.URL
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.Scanner

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
        val citiesApi = resources.getStringArray(R.array.weather_cities_api)
        val defaultIndex = citiesApi.indexOf(defaultCity).takeIf { it >= 0 } ?: 0
        binding.citySpinner.setSelection(defaultIndex)
        requestWeather(defaultCity)
    }

    private fun setupCitySpinner() {
        val citiesDisplay = resources.getStringArray(R.array.weather_cities_display)
        val citiesApi = resources.getStringArray(R.array.weather_cities_api)
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, citiesDisplay)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.citySpinner.adapter = adapter
        
        binding.citySpinner.onItemSelectedListener = object : android.widget.AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: android.widget.AdapterView<*>?, view: android.view.View?, position: Int, id: Long) {
                val selectedCity = citiesApi[position]
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
            val citiesApi = resources.getStringArray(R.array.weather_cities_api)
            if (selectedPosition >= 0 && selectedPosition < citiesApi.size) {
                requestWeather(citiesApi[selectedPosition])
            }
        }
    }

    private fun requestWeather(city: String) {
        if (city.isBlank()) {
            return
        }

        lifecycleScope.launch {
            updateUiState { state -> state.asLoading() }
            runCatching { loadWeather(city) }
                .onSuccess { data ->
                    // Сохраняем данные в JSON файл (основное задание)
                    saveWeatherToJson(data)
                    
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
                    // Пытаемся загрузить из сохраненного JSON, если есть ошибка сети
                    runCatching { loadWeatherFromJson(city) }
                        .onSuccess { cachedData ->
                            cachedData?.let { data ->
                                val formattedTime = formatUpdateTime(data.fetchedAtMillis)
                                val state = WeatherUiState.fromData(
                                    data = data,
                                    lastUpdateLabel = getString(R.string.label_last_update, formattedTime) + " (кэш)",
                                    temperatureScale = currentTemperatureScale,
                                    showWindDirection = showWindDirection
                                )
                                binding.uiState = state
                            }
                        }
                        .onFailure {
                            val message = getString(
                                R.string.error_loading_weather,
                                throwable.userMessage()
                            )
                            updateUiState { state -> state.withError(message) }
                        }
                }
        }
    }

    private suspend fun loadWeather(city: String): WeatherData {
        // Получаем API ключ до перехода в IO поток
        val API_KEY = getString(R.string.openweather_api_key)
        if (API_KEY.isBlank()) {
            throw IOException("API ключ не найден в ресурсах")
        }

        return withContext(Dispatchers.IO) {
            try {
                val weatherURL = "https://api.openweathermap.org/data/2.5/weather?q=$city&appid=$API_KEY&units=metric&lang=ru"
                
                val stream = try {
                    URL(weatherURL).getContent() as? InputStream
                } catch (e: java.net.UnknownHostException) {
                    throw IOException("Нет подключения к интернету", e)
                } catch (e: java.net.ConnectException) {
                    throw IOException("Не удалось подключиться к серверу", e)
                } catch (e: Exception) {
                    throw IOException("Ошибка сети: ${e.message}", e)
                }
                
                if (stream == null) {
                    throw IOException("Пустой ответ от сервера")
                }

                // JSON отдаётся одной строкой
                val data = try {
                    Scanner(stream).useDelimiter("\\A").use { scanner ->
                        if (scanner.hasNext()) scanner.next() else ""
                    }
                } catch (e: Exception) {
                    throw IOException("Ошибка чтения данных: ${e.message}", e)
                } finally {
                    stream.close()
                }

                if (data.isBlank()) {
                    throw IOException("Пустой ответ от сервера")
                }

                val jsonObject = try {
                    JSONObject(data)
                } catch (e: Exception) {
                    throw IOException("Ошибка парсинга JSON: ${e.message}", e)
                }

                // Проверка на ошибки API
                if (jsonObject.has("cod") && jsonObject.optInt("cod") != 200) {
                    val message = jsonObject.optString("message", "Неизвестная ошибка API")
                    throw IOException("Ошибка API: $message")
                }

                // Парсинг данных из OpenWeatherMap API
                val mainObject = jsonObject.getJSONObject("main")
                val weatherArray = jsonObject.getJSONArray("weather")
                val weatherObject = if (weatherArray.length() > 0) weatherArray.getJSONObject(0) else null
                val windObject = jsonObject.optJSONObject("wind")
                val cloudsObject = jsonObject.optJSONObject("clouds")
                val cityName = jsonObject.optString("name", city)

                WeatherData(
                    city = cityName,
                    temperature = mainObject.getDouble("temp"),
                    feelsLike = mainObject.getDouble("feels_like"),
                    description = weatherObject?.optString("description", "") ?: "",
                    humidity = mainObject.getInt("humidity"),
                    pressure = mainObject.getInt("pressure"),
                    windSpeed = windObject?.optDouble("speed") ?: 0.0,
                    windDeg = windObject?.optInt("deg") ?: 0,
                    cloudiness = cloudsObject?.optInt("all") ?: 0,
                    fetchedAtMillis = System.currentTimeMillis(),
                )
            } catch (e: IOException) {
                throw e
            } catch (e: Exception) {
                throw IOException("Ошибка загрузки данных о погоде: ${e.message}", e)
            }
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

    /**
     * Сохраняет данные о погоде в JSON файл во внутреннем хранилище приложения
     * (основное задание - хранение сведений о погоде в JSON файле)
     */
    private suspend fun saveWeatherToJson(weatherData: WeatherData) = withContext(Dispatchers.IO) {
        try {
            val file = File(filesDir, "weather_data.json")
            val jsonObject = if (file.exists()) {
                // Читаем существующий JSON
                val existingContent = file.readText()
                if (existingContent.isNotBlank()) {
                    JSONObject(existingContent)
                } else {
                    JSONObject()
                }
            } else {
                JSONObject()
            }

            // Создаем объект для города
            val cityObject = JSONObject().apply {
                put("city", weatherData.city)
                put("temperature", weatherData.temperature)
                put("feelsLike", weatherData.feelsLike)
                put("description", weatherData.description)
                put("humidity", weatherData.humidity)
                put("pressure", weatherData.pressure)
                put("windSpeed", weatherData.windSpeed)
                put("windDeg", weatherData.windDeg)
                put("cloudiness", weatherData.cloudiness)
            }

            // Добавляем или обновляем данные для города
            jsonObject.put(weatherData.city, cityObject)

            // Записываем обратно в файл
            FileWriter(file).use { writer ->
                writer.write(jsonObject.toString(2)) // 2 - отступ для читаемости
            }
        } catch (e: Exception) {
            // Логируем ошибку, но не прерываем выполнение
            android.util.Log.e("MainActivity", "Ошибка сохранения в JSON: ${e.message}", e)
        }
    }

    /**
     * Загружает данные о погоде из сохраненного JSON файла
     * (основное задание - хранение сведений о погоде в JSON файле)
     * @return WeatherData если данные найдены, null в противном случае
     */
    private suspend fun loadWeatherFromJson(city: String): WeatherData? = withContext(Dispatchers.IO) {
        try {
            val file = File(filesDir, "weather_data.json")
            if (!file.exists()) {
                return@withContext null
            }

            val jsonString = file.readText()
            if (jsonString.isBlank()) {
                return@withContext null
            }

            val jsonObject = JSONObject(jsonString)
            val cityData = jsonObject.optJSONObject(city) ?: return@withContext null

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
            android.util.Log.e("MainActivity", "Ошибка чтения из JSON: ${e.message}", e)
            null
        }
    }
}