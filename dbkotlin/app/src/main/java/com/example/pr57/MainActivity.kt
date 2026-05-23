package com.example.pr57

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {

    private lateinit var db: CompanyDatabase
    private lateinit var adapter: CompanyAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var etSubstring: EditText
    private lateinit var btnDelete: Button
    private lateinit var btnAnalysis: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        db = CompanyDatabase.getDatabase(this)

        recyclerView = findViewById(R.id.recyclerView)
        etSubstring = findViewById(R.id.etSubstring)
        btnDelete = findViewById(R.id.btnDelete)
        btnAnalysis = findViewById(R.id.btnAnalysis)

        adapter = CompanyAdapter(emptyList())
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        btnDelete.setOnClickListener {
            val substring = etSubstring.text.toString()
            if (substring.isNotBlank()) {
                Thread {
                    db.companyDao().deleteBySubstring(substring)
                    val companies = db.companyDao().getAll()
                    runOnUiThread { adapter.updateData(companies) }
                }.start()
            }
        }

        btnAnalysis.setOnClickListener {
            startActivity(Intent(this, AnalysisActivity::class.java))
        }
    }

    override fun onResume() {
        super.onResume()
        loadCompanies()
    }

    private fun loadCompanies() {
        Thread {
            val dao = db.companyDao()
            if (dao.getAll().isEmpty()) {
                dao.insertAll(CompanyDatabase.initialCompanies)
            }
            val companies = dao.getAll()
            runOnUiThread { adapter.updateData(companies) }
        }.start()
    }
}
