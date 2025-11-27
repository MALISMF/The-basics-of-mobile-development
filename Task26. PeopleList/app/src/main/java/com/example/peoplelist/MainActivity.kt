package com.example.peoplelist

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.EditText
import android.widget.ListView
import kotlin.random.Random

class MainActivity : AppCompatActivity() {
    private lateinit var lvPeople: ListView
    private lateinit var editTextName: EditText
    private lateinit var adapter: PeopleAdapter
    private val people = mutableListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        
        lvPeople = findViewById(R.id.people)
        editTextName = findViewById(R.id.editTextName)
        
        // Генерация начальной коллекции случайных имён и фамилий
        generateRandomPeople(10)
        
        // Создание и назначение адаптера
        adapter = PeopleAdapter(this, people)
        lvPeople.adapter = adapter
        
        // Установка обработчика клика на элементы списка
        lvPeople.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->
            // Устанавливаем выбранную позицию в адаптере
            adapter.setSelectedPosition(position)
        }
    }

    /**
     * Генерирует случайные комбинации имён и фамилий из строковых ресурсов
     */
    private fun generateRandomPeople(count: Int) {
        val firstNames = resources.getStringArray(R.array.first_names)
        val lastNames = resources.getStringArray(R.array.last_names)
        
        for (i in 0 until count) {
            val firstName = firstNames[Random.nextInt(firstNames.size)]
            val lastName = lastNames[Random.nextInt(lastNames.size)]
            people.add("$firstName $lastName")
        }
    }

    /**
     * Обработчик события нажатия кнопки для добавления нового элемента
     */
    fun onAddPersonClick(view: View) {
        val name = editTextName.text.toString().trim()
        
        if (name.isNotEmpty()) {
            // Добавление нового элемента в начало коллекции (сверху списка)
            people.add(0, name)
            
            // Обновляем выбранную позицию, если элемент был выбран
            val selectedPos = adapter.getSelectedPosition()
            if (selectedPos != -1) {
                // Сдвигаем выделение на одну позицию вниз, так как новый элемент добавлен сверху
                adapter.setSelectedPosition(selectedPos + 1)
            }
            
            // Уведомление адаптера об изменении коллекции
            adapter.notifyDataSetChanged()
            // Очистка поля ввода
            editTextName.text.clear()
        }
    }
}
