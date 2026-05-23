package com.example.weatherworkmanager.worker

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.Data
import androidx.work.WorkerParameters
import com.example.weatherworkmanager.BuildConfig
import com.example.weatherworkmanager.api.WeatherApiClient
import com.google.gson.Gson

/**
 * WeatherWorker получает погоду для ОДНОГО города через OpenWeatherMap API.
 *
 * Входные данные (inputData):
 *   KEY_CITY       — название города (String)
 *   KEY_RESULTS    — JSON-массив результатов предыдущих городов (String, может отсутствовать)
 *
 * Выходные данные (outputData):
 *   KEY_RESULTS    — JSON-массив, в который добавлен результат текущего города
 *   KEY_LAST_CITY  — название последнего обработанного города
 */
class WeatherWorker(
    appContext: Context,
    params: WorkerParameters
) : CoroutineWorker(appContext, params) {

    companion object {
        const val KEY_CITY       = "city"
        const val KEY_RESULTS    = "results"   // накопленный JSON-массив
        const val KEY_LAST_CITY  = "last_city"

        // Gson-сериализация одного результата, который кладём в массив
        data class CityResult(
            val city: String,
            val temp: Double,
            val feelsLike: Double,
            val description: String,
            val humidity: Int,
            val windSpeed: Double,
            val country: String,
            val icon: String,
            val error: String? = null
        )
    }

    private val gson = Gson()

    override suspend fun doWork(): Result {
        val city = inputData.getString(KEY_CITY)
            ?: return Result.failure(
                Data.Builder().putString("error", "Не задан город").build()
            )

        // Читаем накопленный список предыдущих результатов
        val previousJson = inputData.getString(KEY_RESULTS) ?: "[]"
        val resultsList = gson.fromJson(previousJson, Array<CityResult>::class.java)
            .toMutableList()

        // Запрашиваем API
        val newEntry = try {
            val response = WeatherApiClient.service.getCurrentWeather(
                city = city,
                apiKey = BuildConfig.OWM_API_KEY
            )
            CityResult(
                city        = response.name,
                temp        = response.main.temp - 273.15,
                feelsLike   = response.main.feelsLike - 273.15,
                description = response.weather.firstOrNull()
                    ?.description
                    ?.replaceFirstChar { it.uppercase() } ?: "",
                humidity    = response.main.humidity,
                windSpeed   = response.wind.speed,
                country     = response.sys.country,
                icon        = response.weather.firstOrNull()?.icon ?: ""
            )
        } catch (e: Exception) {
            // Ошибка не прерывает цепочку — добавляем запись с флагом error
            CityResult(
                city        = city,
                temp        = 0.0,
                feelsLike   = 0.0,
                description = "",
                humidity    = 0,
                windSpeed   = 0.0,
                country     = "",
                icon        = "",
                error       = e.message ?: "Неизвестная ошибка"
            )
        }

        resultsList.add(newEntry)

        val outputData = Data.Builder()
            .putString(KEY_RESULTS,   gson.toJson(resultsList))
            .putString(KEY_LAST_CITY, city)
            .build()

        return Result.success(outputData)
    }
}
