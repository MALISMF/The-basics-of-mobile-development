import java.net.URL
import java.io.InputStream
import java.io.InputStreamReader
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName

// Классы для парсинга JSON ответа от OpenWeatherMap
data class Weather(
    val name: String,
    val main: Main,
    val sys: Sys
)

data class Main(
    val temp: Double
)

data class Sys(
    val sunset: Int, 
    val sunrise: Int
)


// Функция для расчета длины дня в часах и минутах
fun formatDayLength(sunrise: Int, sunset: Int): String {
    val dayLength = sunset - sunrise
    val hours = dayLength / 3600
    val minutes = (dayLength % 3600) / 60
    return "${hours}ч ${minutes}м"
}


// Функция для получения погоды по ID города
fun getWeatherForCityById(cityId: Int, apiKey: String): Weather {
    val weatherUrl = "https://api.openweathermap.org/data/2.5/weather?id=$cityId&appid=$apiKey&units=metric"
    val url = URL(weatherUrl)
    val stream = url.getContent() as InputStream
    val gson = Gson()
    val weather: Weather = gson.fromJson(InputStreamReader(stream), Weather::class.java)
    
    return weather
}

fun main() {
    val API_KEY = "c18759d07ad27290d08d2c2781409501"
    
    // Список городов с их ID
    val cities = listOf(
        "Angarsk" to 2027667,
        "Moscow" to 524901,
        "Saint Petersburg" to 498817,
        "Sochi" to 491422,
        "Irkutsk" to 2023469,
        "Harbin" to 2037013,
        "Beijing" to 1816670,
        "Shanghai" to 1796236,
        "Berlin" to 2950159,
        "Cairo" to 360630,

    )
    
    println("Поиск самых теплых городов...")
    println("=".repeat(50))
    
    val weatherData = mutableListOf<Weather>()
    
    // Получаем погоду для каждого города
    for ((cityName, cityId) in cities) {
        println("Получаем погоду для $cityName...")
        val weather = getWeatherForCityById(cityId, API_KEY)
        weatherData.add(weather)
        Thread.sleep(1000) // Пауза между запросами, чтобы не превысить лимит API
    }
    
    // Сортируем города по температуре (от более теплых к более холодным)
    val sortedWeather = weatherData.sortedByDescending { it.main.temp }
    
    println("\nРезультаты (от самых теплых к самым холодным):")
    println("=".repeat(50))
    
    // Выводим результаты в консоль
    for ((index, weather) in sortedWeather.withIndex()) {
        val dayLengthFormatted = formatDayLength(weather.sys.sunrise, weather.sys.sunset)
        println("${index + 1}. ${weather.name}: ${String.format("%.1f", weather.main.temp)}°C (длина дня: $dayLengthFormatted)")
    }
    
    // Сохраняем результаты в файл
    val outputFile = java.io.File("weather_results.txt")
    outputFile.writeText("Результаты поиска самых теплых городов\n")
    outputFile.appendText("=".repeat(50) + "\n")
    
    for ((index, weather) in sortedWeather.withIndex()) {
        val dayLengthFormatted = formatDayLength(weather.sys.sunrise, weather.sys.sunset)
        outputFile.appendText("${index + 1}. ${weather.name}: ${String.format("%.1f", weather.main.temp)}°C (длина дня: $dayLengthFormatted)\n")
    }
    
    println("\nРезультаты сохранены в файл weather_results.txt")
}