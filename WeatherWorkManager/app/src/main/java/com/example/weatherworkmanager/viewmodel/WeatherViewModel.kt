package com.example.weatherworkmanager.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map
import androidx.work.Data
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkInfo
import androidx.work.WorkManager
import com.example.weatherworkmanager.model.CityWeatherItem
import com.example.weatherworkmanager.worker.WeatherWorker
import com.google.gson.Gson
import java.util.UUID

class WeatherViewModel(application: Application) : AndroidViewModel(application) {

    private val workManager = WorkManager.getInstance(application)
    private val gson = Gson()

    // Список городов, которые будем запрашивать
    val cities = listOf("Moscow", "Saint Petersburg", "Riga", "Berlin", "London", "Tokyo")

    // Отображаемые карточки погоды
    private val _weatherItems = MutableLiveData<List<CityWeatherItem>>(emptyList())
    val weatherItems: LiveData<List<CityWeatherItem>> = _weatherItems

    // Состояние всей цепочки
    private val _chainState = MutableLiveData<ChainState>(ChainState.IDLE)
    val chainState: LiveData<ChainState> = _chainState

    // ID финального воркера цепочки — следим за его WorkInfo
    private var finalWorkId: UUID? = null

    enum class ChainState { IDLE, RUNNING, SUCCEEDED, FAILED }

    /**
     * Запускает цепочку WorkManager: один город = один Worker.
     * Каждый воркер передаёт накопленный JSON следующему через outputData → inputData.
     *
     * Схема:
     *   [Москва] → [СПб] → [Рига] → [Берлин] → [Лондон] → [Токио]
     */
    fun fetchAllCities() {
        if (_chainState.value == ChainState.RUNNING) return

        // Показываем «скелетные» карточки сразу
        _weatherItems.value = cities.map { CityWeatherItem.loading(it) }
        _chainState.value = ChainState.RUNNING

        // Строим запросы для каждого города
        val requests = cities.map { city ->
            OneTimeWorkRequestBuilder<WeatherWorker>()
                .setInputData(
                    Data.Builder()
                        .putString(WeatherWorker.KEY_CITY, city)
                        .build()
                )
                // Тег позволяет удобно найти все воркеры этой цепочки
                .addTag("weather_chain")
                .addTag("city_$city")
                .build()
        }

        // Создаём цепочку: beginWith → then → then → ...
        // WorkManager автоматически прокидывает outputData предыдущего как inputData следующего
        val chain = workManager
            .beginWith(requests.first())
            .let { continuation ->
                requests.drop(1).fold(continuation) { acc, request ->
                    acc.then(request)
                }
            }

        chain.enqueue()

        // Запоминаем ID последнего воркера для отслеживания завершения цепочки
        finalWorkId = requests.last().id

        // Наблюдаем за каждым воркером — по мере выполнения обновляем список
        requests.forEachIndexed { index, request ->
            workManager.getWorkInfoByIdLiveData(request.id)
                .observeForever { workInfo ->
                    if (workInfo != null) {
                        onWorkerUpdate(index, cities[index], workInfo)
                    }
                }
        }
    }

    /**
     * Вызывается при изменении статуса любого воркера.
     * Обновляет конкретную карточку в списке.
     */
    private fun onWorkerUpdate(index: Int, city: String, workInfo: WorkInfo) {
        val current = _weatherItems.value?.toMutableList() ?: return

        current[index] = when (workInfo.state) {
            WorkInfo.State.SUCCEEDED -> {
                val resultsJson = workInfo.outputData.getString(WeatherWorker.KEY_RESULTS) ?: "[]"
                val results = gson.fromJson(
                    resultsJson,
                    Array<WeatherWorker.Companion.CityResult>::class.java
                )
                // Берём последний добавленный результат (он соответствует этому воркеру)
                val result = results.lastOrNull()
                if (result != null && result.error == null) {
                    CityWeatherItem(
                        city        = result.city,
                        country     = result.country,
                        tempCelsius = result.temp,
                        feelsLike   = result.feelsLike,
                        description = result.description,
                        humidity    = result.humidity,
                        windSpeed   = result.windSpeed,
                        icon        = result.icon
                    )
                } else {
                    CityWeatherItem.error(city, result?.error ?: "Ошибка")
                }
            }
            WorkInfo.State.FAILED    -> CityWeatherItem.error(city, "Worker завершился с ошибкой")
            WorkInfo.State.RUNNING   -> CityWeatherItem.loading(city)
            else                     -> current[index]
        }

        _weatherItems.value = current

        // Обновляем общий статус цепочки по последнему воркеру
        if (workInfo.id == finalWorkId) {
            _chainState.value = when (workInfo.state) {
                WorkInfo.State.SUCCEEDED -> ChainState.SUCCEEDED
                WorkInfo.State.FAILED    -> ChainState.FAILED
                else                     -> ChainState.RUNNING
            }
        }
    }

    /** Отменяет все воркеры текущей цепочки */
    fun cancelChain() {
        workManager.cancelAllWorkByTag("weather_chain")
        _chainState.value = ChainState.IDLE
    }
}
