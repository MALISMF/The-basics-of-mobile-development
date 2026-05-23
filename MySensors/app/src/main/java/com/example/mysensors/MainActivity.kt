package com.example.mysensors

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorManager
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.Spinner
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var sensorManager: SensorManager
    private lateinit var spinner: Spinner
    private lateinit var listSensor: TextView

    // Датчики окружающей среды:
    //   Магнитное поле (2), Освещённость (5), Давление (6),
    //   Относительная влажность (12), Температура (13)
    private val environmentTypes = intArrayOf(
        Sensor.TYPE_MAGNETIC_FIELD,
        Sensor.TYPE_LIGHT,
        Sensor.TYPE_PRESSURE,
        Sensor.TYPE_RELATIVE_HUMIDITY,
        Sensor.TYPE_AMBIENT_TEMPERATURE
    )

    // Датчики положения устройства:
    //   Акселерометр (1), Гироскоп (4), Приближение (8), Гравитация (9),
    //   Линейное ускорение (10), Вектор вращения (11),
    //   Игровой вектор вращения (15), Некалиброванный гироскоп (16),
    //   Значительные колебания (17), Одиночный шаг (18),
    //   Счётчик шагов (19), Движение (30)
    private val positionTypes = intArrayOf(
        Sensor.TYPE_ACCELEROMETER,
        Sensor.TYPE_GYROSCOPE,
        Sensor.TYPE_PROXIMITY,
        Sensor.TYPE_GRAVITY,
        Sensor.TYPE_LINEAR_ACCELERATION,
        Sensor.TYPE_ROTATION_VECTOR,
        Sensor.TYPE_GAME_ROTATION_VECTOR,
        Sensor.TYPE_GYROSCOPE_UNCALIBRATED,
        Sensor.TYPE_SIGNIFICANT_MOTION,
        Sensor.TYPE_STEP_DETECTOR,
        Sensor.TYPE_STEP_COUNTER,
        30  // TYPE_MOTION_DETECT (API 24+)
    )

    // Датчики состояния человека:
    //   Мониторинг пульса (21), ЧСС (31),
    //   Удаление устройства от человека (34)
    private val humanTypes = intArrayOf(
        21,   // TYPE_HEART_RATE
        31,   // TYPE_HEART_BEAT
        34    // TYPE_LOW_LATENCY_OFFBODY_DETECT
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        spinner = findViewById(R.id.spinner)
        listSensor = findViewById(R.id.list_sensor)

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>, view: View?, position: Int, id: Long
            ) {
                val types = when (position) {
                    0 -> environmentTypes
                    1 -> positionTypes
                    2 -> humanTypes
                    else -> intArrayOf()
                }
                showSensors(types)
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }

        // При запуске — датчики окружающей среды (пункт 0 Spinner вызывает onItemSelected автоматически)
    }

    /**
     * Для каждого типа из массива получает датчик через getDefaultSensor().
     * Если датчик отсутствует — пропускает. Если ни одного нет — TextView пустой.
     */
    private fun showSensors(types: IntArray) {
        val sb = StringBuilder()
        for (type in types) {
            val sensor = sensorManager.getDefaultSensor(type)
            if (sensor != null) {
                val wakeUp = if (sensor.isWakeUpSensor) "Wakeup" else "Non-wakeup"
                sb.appendLine("${sensor.name}  $wakeUp")
            }
        }
        listSensor.text = sb.toString().trimEnd()
    }
}
