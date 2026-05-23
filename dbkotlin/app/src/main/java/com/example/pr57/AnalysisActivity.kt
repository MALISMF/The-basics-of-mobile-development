package com.example.pr57

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class AnalysisActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_analysis)

        val db = CompanyDatabase.getDatabase(this)

        Thread {
            val totalCap = db.companyDao().getTotalCapitalization()
            val aboveAverage = db.companyDao().getCountAboveAverage()
            val englishCount = db.companyDao().getEnglishCount()
            val maxCapName = db.companyDao().getMaxCapCompanyName() ?: ""
            val longestName = db.companyDao().getLongestNameCompany() ?: ""

            runOnUiThread {
                findViewById<TextView>(R.id.tvTotalCap).text = totalCap.toString()
                findViewById<TextView>(R.id.tvAboveAverage).text = aboveAverage.toString()
                findViewById<TextView>(R.id.tvEnglishCount).text = englishCount.toString()
                findViewById<TextView>(R.id.tvMaxCapCompany).text = maxCapName
                findViewById<TextView>(R.id.tvLongestCompany).text = longestName
            }
        }.start()
    }
}
