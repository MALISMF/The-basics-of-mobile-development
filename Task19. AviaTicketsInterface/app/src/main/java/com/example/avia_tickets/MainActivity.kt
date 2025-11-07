package com.example.avia_tickets

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.button.MaterialButton
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.textfield.TextInputEditText
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {
    
    private lateinit var spinnerFrom: Spinner
    private lateinit var spinnerTo: Spinner
    private lateinit var etDepartDate: TextInputEditText
    private lateinit var etReturnDate: TextInputEditText
    private lateinit var etAdults: TextInputEditText
    private lateinit var etChildren: TextInputEditText
    private lateinit var etInfants: TextInputEditText
    private lateinit var btnSearch: MaterialButton
    
    private val dateFormat = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(android.R.id.content)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        
        initViews()
        setupSpinners()
        setupDatePickers()
        setupSearchButton()
    }
    
    private fun initViews() {
        spinnerFrom = findViewById(R.id.spinner_from)
        spinnerTo = findViewById(R.id.spinner_to)
        etDepartDate = findViewById(R.id.et_depart_date)
        etReturnDate = findViewById(R.id.et_return_date)
        etAdults = findViewById(R.id.et_adults)
        etChildren = findViewById(R.id.et_children)
        etInfants = findViewById(R.id.et_infants)
        btnSearch = findViewById(R.id.btn_search)
    }
    
    private fun setupSpinners() {
        val cities = resources.getStringArray(R.array.cities)
        
        val adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            cities
        ).apply {
            setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        }
        
        spinnerFrom.adapter = adapter
        spinnerTo.adapter = adapter
    }
    
    private fun setupDatePickers() {
        // DatePicker для даты вылета
        etDepartDate.setOnClickListener {
            showDatePicker(etDepartDate)
        }
        
        // DatePicker для даты прилёта
        etReturnDate.setOnClickListener {
            showDatePicker(etReturnDate)
        }
    }
    
    private fun showDatePicker(textInput: TextInputEditText) {
        val datePicker = MaterialDatePicker.Builder.datePicker()
            .setTitleText("Выберите дату")
            .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
            .build()
        
        datePicker.addOnPositiveButtonClickListener { selection ->
            val date = Date(selection)
            textInput.setText(dateFormat.format(date))
        }
        
        datePicker.show(supportFragmentManager, "DATE_PICKER")
    }
    
    private fun setupSearchButton() {
        btnSearch.setOnClickListener {
            val fromCity = spinnerFrom.selectedItem as String
            val toCity = spinnerTo.selectedItem as String
            val departDate = etDepartDate.text.toString()
            val returnDate = etReturnDate.text.toString()
            val adults = etAdults.text.toString()
            val children = etChildren.text.toString()
            val infants = etInfants.text.toString()
            
            // Простая валидация
            if (departDate.isEmpty() || returnDate.isEmpty()) {
                Toast.makeText(this, "Пожалуйста, выберите даты вылета и прилёта", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            
            // Формируем сообщение с результатами поиска
            val message = buildString {
                append("Поиск билетов:\n")
                append("Откуда: $fromCity\n")
                append("Куда: $toCity\n")
                append("Дата вылета: $departDate\n")
                append("Дата прилёта: $returnDate\n")
                append("Взрослые: ${if (adults.isEmpty()) "0" else adults}\n")
                append("Дети: ${if (children.isEmpty()) "0" else children}\n")
                append("Младенцы: ${if (infants.isEmpty()) "0" else infants}")
            }
            
            Toast.makeText(this, message, Toast.LENGTH_LONG).show()
        }
    }
}