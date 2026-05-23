package com.example.zhdun

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.BatteryManager
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    companion object {
        const val TOAST_TEXT = "Ждун устал ждать... До новых встреч!"
    }

    private var minuteCount = 0
    private var isTimeReceiverRegistered = false
    private var isBatteryReceiverRegistered = false

    private lateinit var tvStatus: TextView
    private lateinit var btnStop: Button
    private lateinit var ivZhdun: ImageView

    // Приёмник изменения системного времени (каждую минуту)
    private val timeReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            if (intent?.action == Intent.ACTION_TIME_TICK) {
                minuteCount++
                tvStatus.text = "время созерцания: $minuteCount мин."
            }
        }
    }

    // Приёмник состояния батареи (заряжается / не заряжается)
    private val batteryReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val status = intent?.getIntExtra(BatteryManager.EXTRA_STATUS, -1) ?: -1
            val isCharging = status == BatteryManager.BATTERY_STATUS_CHARGING
                    || status == BatteryManager.BATTERY_STATUS_FULL

            if (!isCharging) {
                tvStatus.text = "накормите Ждуна, силы на исходе!"
                ivZhdun.setImageResource(R.drawable.zhdun_sad)
            } else {
                tvStatus.text = "время созерцания: $minuteCount мин."
                ivZhdun.setImageResource(R.drawable.zhdun_happy)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tvStatus = findViewById(R.id.tvStatus)
        btnStop = findViewById(R.id.btnStop)
        ivZhdun = findViewById(R.id.ivZhdun)

        tvStatus.text = "время созерцания: $minuteCount мин."

        // Регистрация приёмника TIME_TICK
        registerReceiver(timeReceiver, IntentFilter(Intent.ACTION_TIME_TICK))
        isTimeReceiverRegistered = true

        // Регистрация приёмника состояния батареи
        registerReceiver(batteryReceiver, IntentFilter(Intent.ACTION_BATTERY_CHANGED))
        isBatteryReceiverRegistered = true

        btnStop.setOnClickListener {
            if (isTimeReceiverRegistered) {
                unregisterReceiver(timeReceiver)
                isTimeReceiverRegistered = false
            }
            Toast.makeText(this, TOAST_TEXT, Toast.LENGTH_LONG).show()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (isTimeReceiverRegistered) {
            unregisterReceiver(timeReceiver)
            isTimeReceiverRegistered = false
        }
        if (isBatteryReceiverRegistered) {
            unregisterReceiver(batteryReceiver)
            isBatteryReceiverRegistered = false
        }
    }
}
