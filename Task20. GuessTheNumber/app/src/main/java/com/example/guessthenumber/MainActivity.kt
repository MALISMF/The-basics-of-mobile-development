package com.example.guessthenumber

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.widget.doAfterTextChanged
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val minInputLayout = findViewById<TextInputLayout>(R.id.minInputLayout)
        val maxInputLayout = findViewById<TextInputLayout>(R.id.maxInputLayout)
        val minInputEditText = findViewById<TextInputEditText>(R.id.minInputEditText)
        val maxInputEditText = findViewById<TextInputEditText>(R.id.maxInputEditText)
        val startButton = findViewById<MaterialButton>(R.id.startButton)

        minInputEditText.doAfterTextChanged { minInputLayout.error = null }
        maxInputEditText.doAfterTextChanged { maxInputLayout.error = null }

        startButton.setOnClickListener {
            val minText = minInputEditText.text?.toString()?.trim().orEmpty()
            val maxText = maxInputEditText.text?.toString()?.trim().orEmpty()

            val minValue = parseValue(minText, minInputLayout)
            val maxValue = parseValue(maxText, maxInputLayout)

            if (minValue == null || maxValue == null) {
                return@setOnClickListener
            }

            if (minValue >= maxValue) {
                minInputLayout.error = getString(R.string.range_error_order)
                maxInputLayout.error = getString(R.string.range_error_order)
                return@setOnClickListener
            }

            val intent = Intent(this, GuessActivity::class.java).apply {
                putExtra(GuessActivity.EXTRA_MIN_VALUE, minValue)
                putExtra(GuessActivity.EXTRA_MAX_VALUE, maxValue)
            }
            startActivity(intent)
        }
    }

    private fun parseValue(value: String, layout: TextInputLayout): Int? = when {
        value.isEmpty() -> {
            layout.error = getString(R.string.range_error_empty)
            null
        }
        value.toIntOrNull() == null -> {
            layout.error = getString(R.string.range_error_number)
            null
        }
        else -> {
            layout.error = null
            value.toInt()
        }
    }
}