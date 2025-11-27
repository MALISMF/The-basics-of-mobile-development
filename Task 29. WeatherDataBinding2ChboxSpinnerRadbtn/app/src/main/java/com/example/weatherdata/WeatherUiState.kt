package com.example.weatherdata

import androidx.annotation.DrawableRes
import java.util.Locale
import kotlin.math.roundToInt

enum class TemperatureScale {
    CELSIUS,
    FAHRENHEIT
}

data class WeatherUiState(
    val city: String = "",
    val temperature: Double? = null,
    val feelsLike: Double? = null,
    val description: String? = null,
    val humidity: Int? = null,
    val pressure: Int? = null,
    val windSpeed: Double? = null,
    val windDeg: Int? = null,
    val cloudiness: Int? = null,
    val lastUpdateText: String = "",
    val loading: Boolean = false,
    val errorMessage: String? = null,
    val temperatureScale: TemperatureScale = TemperatureScale.CELSIUS,
    val showWindDirection: Boolean = true,
) {
    val cityLabel: String
        get() = city.ifBlank { PLACEHOLDER }

    private fun celsiusToFahrenheit(celsius: Double): Double = celsius * 9.0 / 5.0 + 32.0

    val temperatureText: String
        get() = temperature?.let { temp ->
            val displayTemp = when (temperatureScale) {
                TemperatureScale.CELSIUS -> temp
                TemperatureScale.FAHRENHEIT -> celsiusToFahrenheit(temp)
            }
            val unit = when (temperatureScale) {
                TemperatureScale.CELSIUS -> "°C"
                TemperatureScale.FAHRENHEIT -> "°F"
            }
            "${displayTemp.roundToInt()}$unit"
        } ?: PLACEHOLDER

    val feelsLikeText: String
        get() = feelsLike?.let { temp ->
            val displayTemp = when (temperatureScale) {
                TemperatureScale.CELSIUS -> temp
                TemperatureScale.FAHRENHEIT -> celsiusToFahrenheit(temp)
            }
            val unit = when (temperatureScale) {
                TemperatureScale.CELSIUS -> "°C"
                TemperatureScale.FAHRENHEIT -> "°F"
            }
            "Ощущается как ${displayTemp.roundToInt()}$unit"
        } ?: "Ощущается как $PLACEHOLDER"

    val descriptionText: String
        get() = description?.replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() } ?: ""

    val humidityText: String
        get() = humidity?.let { "Влажность: $it%" } ?: "Влажность: $PLACEHOLDER"

    val pressureText: String
        get() = pressure?.let { "Давление: $it гПа" } ?: "Давление: $PLACEHOLDER"

    val windText: String
        get() = windSpeed?.let { speed ->
            val value = String.format(Locale.getDefault(), "%.1f", speed)
            "Ветер: $value м/с"
        } ?: "Ветер: $PLACEHOLDER"

    val cloudinessText: String
        get() = cloudiness?.let { "Облачность: $it%" } ?: "Облачность: $PLACEHOLDER"

    val windRotation: Float
        get() = (windDeg ?: 0).toFloat()

    @get:DrawableRes
    val cloudIconRes: Int
        get() = when ((cloudiness ?: 0)) {
            in 0..20 -> R.drawable.ic_cloud_clear
            in 21..70 -> R.drawable.ic_cloud_partial
            else -> R.drawable.ic_cloud_overcast
        }

    @get:DrawableRes
    val windArrowIconRes: Int = R.drawable.ic_wind_arrow

    fun asLoading(): WeatherUiState = copy(loading = true, errorMessage = null)

    fun withError(message: String): WeatherUiState = copy(loading = false, errorMessage = message)

    companion object {
        private const val PLACEHOLDER = "--"

        fun fromData(
            data: WeatherData,
            lastUpdateLabel: String,
            temperatureScale: TemperatureScale = TemperatureScale.CELSIUS,
            showWindDirection: Boolean = true
        ): WeatherUiState = WeatherUiState(
            city = data.city,
            temperature = data.temperature,
            feelsLike = data.feelsLike,
            description = data.description,
            humidity = data.humidity,
            pressure = data.pressure,
            windSpeed = data.windSpeed,
            windDeg = data.windDeg,
            cloudiness = data.cloudiness,
            lastUpdateText = lastUpdateLabel,
            loading = false,
            errorMessage = null,
            temperatureScale = temperatureScale,
            showWindDirection = showWindDirection,
        )
    }
}

data class WeatherData(
    val city: String,
    val temperature: Double,
    val feelsLike: Double,
    val description: String,
    val humidity: Int,
    val pressure: Int,
    val windSpeed: Double,
    val windDeg: Int,
    val cloudiness: Int,
    val fetchedAtMillis: Long,
)

