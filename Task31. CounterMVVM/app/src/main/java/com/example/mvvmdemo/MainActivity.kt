package com.example.mvvmdemo

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider


class MainActivity : AppCompatActivity() {

    lateinit var viewModel: MainViewModel
    private lateinit var textCounter: TextView
    private lateinit var btnIncrement: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        val provider = ViewModelProvider(this)
        viewModel = provider.get(MainViewModel::class.java)
        // Инструкции в упражнении
        // https://www.innovationcampus.ru/lms/mod/book/view.php?id=918&chapterid=940
        
        textCounter = findViewById(R.id.text_counter)
        btnIncrement = findViewById(R.id.btn_increment)
        
        observeViewModel()

        initView()
    }

    fun observeViewModel() {
        viewModel.counter.observe(this) { count ->
            textCounter.text = count.toString()
        }
    }

    fun initView() {
        btnIncrement.setOnClickListener {
            viewModel.onIncrementClicked()
        }
    }
}