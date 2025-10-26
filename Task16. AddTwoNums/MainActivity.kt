package com.example.addtwonums

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.TextView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun notNullValues(strA: String, strB: String, tvSum: TextView): Boolean {
        if (strA.isEmpty()) {
            tvSum.text = "В первом поле нет значения"
            return false
        }
        if (strB.isEmpty()){
            tvSum.text = "Во втором поле нет значения"
            return false
        }
        return true
    }

    fun calculateSum(strA: String, strB: String, tvSum: TextView) {
        val sum = strA.toDouble() + strB.toDouble()
        val strSum = sum.toString()
        tvSum.text = strSum
    }

    fun onClick(v: View) {
        val etA = findViewById<EditText>(R.id.numA)
        val etB = findViewById<EditText>(R.id.numB)
        val tvSum = findViewById<TextView>(R.id.sum)

        val strA = etA.text.toString()
        val strB = etB.text.toString()

        if(notNullValues(strA, strB, tvSum)) {
            calculateSum(strA, strB, tvSum)
        }
    }
}