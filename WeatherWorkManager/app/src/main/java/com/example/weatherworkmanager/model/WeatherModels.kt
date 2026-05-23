package com.example.weatherworkmanager.model

import com.google.gson.annotations.SerializedName

// ---------- Ответ от OpenWeatherMap /weather ----------

data class WeatherResponse(
    val name: String,                      // Название города
    val main: MainWeather,
    val weather: List<WeatherDescription>,
    val wind: Wind,
    val sys: Sys,
    val visibility: Int,
    val dt: Long                           // Unix timestamp
)

data class MainWeather(
    val temp: Double,
    @SerializedName("feels_like") val feelsLike: Double,
    @SerializedName("temp_min")   val tempMin: Double,
    @SerializedName("temp_max")   val tempMax: Double,
    val pressure: Int,
    val humidity: Int
)

data class WeatherDescription(
    val id: Int,
    val main: String,
    val description: String,
    val icon: String
)

data class Wind(
    val speed: Double,
    val deg: Int
)

data class Sys(
    val country: String,
    val sunrise: Long,
    val sunset: Long
)

// ---------- Модель для отображения в списке ----------

data class CityWeatherItem(
    val city: String,
    val country: String,
    val tempCelsius: Double,
    val feelsLike: Double,
    val description: String,
    val humidity: Int,
    val windSpeed: Double,
    val icon: String,
    val isLoading: Boolean = false,
    val error: String? = null
) {
    val tempFormatted: String
        get() = String.format("%.1f°C", tempCelsius)

    val feelsLikeFormatted: String
        get() = String.format("Ощущается: %.1f°C", feelsLike)

    val humidityFormatted: String
        get() = "Влажность: $humidity%"

    val windFormatted: String
        get() = String.format("Ветер: %.1f м/с", windSpeed)

    val titleFormatted: String
        get() = "$city, $country"

    companion object {
        fun loading(city: String) = CityWeatherItem(
            city = city, country = "", tempCelsius = 0.0, feelsLike = 0.0,
            description = "Загружается...", humidity = 0, windSpeed = 0.0,
            icon = "", isLoading = true
        )

        fun error(city: String, message: String) = CityWeatherItem(
            city = city, country = "", tempCelsius = 0.0, feelsLike = 0.0,
            description = "", humidity = 0, windSpeed = 0.0,
            icon = "", error = message
        )

        fun fromResponse(response: WeatherResponse) = CityWeatherItem(
            city = response.name,
            country = response.sys.country,
            tempCelsius = response.main.temp - 273.15,
            feelsLike = response.main.feelsLike - 273.15,
            description = response.weather.firstOrNull()?.description?.replaceFirstChar { it.uppercase() } ?: "",
            humidity = response.main.humidity,
            windSpeed = response.wind.speed,
            icon = response.weather.firstOrNull()?.icon ?: ""
        )
    }
}
