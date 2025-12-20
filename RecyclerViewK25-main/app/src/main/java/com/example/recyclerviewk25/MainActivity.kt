package com.example.recyclerviewk25

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {

    // Генерация цветов из палитры colors.xml
    private fun generateColorsFromPalette(): List<Int> {
        val colorsList = mutableListOf<Int>()
        
        // Получаем цвета из палитры colors.xml
        val colorIds = listOf(
            R.color.red, R.color.green, R.color.blue, R.color.yellow,
            R.color.cyan, R.color.magenta,
            R.color.pastel_red, R.color.pastel_green, R.color.pastel_blue,
            R.color.pastel_yellow, R.color.pastel_pink, R.color.pastel_purple,
            R.color.pastel_orange, R.color.pastel_cyan,
            R.color.dark_red, R.color.dark_green, R.color.dark_blue,
            R.color.dark_orange, R.color.dark_purple, R.color.dark_cyan,
            R.color.bright_red, R.color.bright_green, R.color.bright_blue,
            R.color.bright_yellow, R.color.bright_orange, R.color.bright_pink,
            R.color.coral, R.color.turquoise, R.color.lavender,
            R.color.mint, R.color.salmon, R.color.gold,
            R.color.silver, R.color.indigo, R.color.lime, R.color.olive
        )
        
        // Конвертируем ресурсы цветов в Int значения
        colorIds.forEach { colorId ->
            colorsList.add(ContextCompat.getColor(this, colorId))
        }
        
        return colorsList
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Генерируем список цветов из палитры
        val colorsList = generateColorsFromPalette()

        // пример использования RecyclerView с собственным адаптером
        val rv = findViewById<RecyclerView>(R.id.rview)
        val colorAdapter = ColorAdapter(LayoutInflater.from(this))
        // добавляем данные в список для отображения
        colorAdapter.submitList(colorsList)
        rv.layoutManager = LinearLayoutManager(this)
        rv.adapter = colorAdapter
    }
}