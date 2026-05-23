package com.example.weatherworkmanager.api

import com.example.weatherworkmanager.model.WeatherResponse
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApiService {

    /**
     * GET /weather?q={city}&appid={apiKey}
     * Возвращает текущую погоду для города.
     * Температура приходит в Кельвинах — конвертируем в модели.
     */
    @GET("weather")
    suspend fun getCurrentWeather(
        @Query("q")      city: String,
        @Query("appid")  apiKey: String,
        @Query("lang")   lang: String = "ru"
    ): WeatherResponse
}

object WeatherApiClient {

    private const val BASE_URL = "https://api.openweathermap.org/data/2.5/"

    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .build()

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val service: WeatherApiService = retrofit.create(WeatherApiService::class.java)
}
