package com.example.pr57

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class CompanyAdapter(private var companies: List<Company>) :
    RecyclerView.Adapter<CompanyAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvCompany: TextView = view.findViewById(R.id.tvCompany)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_company, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val company = companies[position]
        holder.tvCompany.text = "${company.name} ${company.capitalization}"
    }

    override fun getItemCount(): Int = companies.size

    fun updateData(newCompanies: List<Company>) {
        companies = newCompanies
        notifyDataSetChanged()
    }
}
