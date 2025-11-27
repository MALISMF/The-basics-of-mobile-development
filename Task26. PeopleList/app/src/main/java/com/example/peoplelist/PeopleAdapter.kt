package com.example.peoplelist

import android.content.Context
import androidx.core.content.ContextCompat
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView

/**
 * Кастомный адаптер для списка людей с поддержкой выделения элемента
 */
class PeopleAdapter(
    context: Context,
    private val items: MutableList<String>
) : ArrayAdapter<String>(context, R.layout.item, items) {
    
    private var selectedPosition = -1
    
    /**
     * Устанавливает выбранную позицию
     */
    fun setSelectedPosition(position: Int) {
        val previousPosition = selectedPosition
        selectedPosition = position
        // Уведомляем об изменении предыдущего и нового элемента
        if (previousPosition != -1) {
            notifyItemChanged(previousPosition)
        }
        if (position != -1) {
            notifyItemChanged(position)
        }
    }
    
    /**
     * Получает выбранную позицию
     */
    fun getSelectedPosition(): Int = selectedPosition
    
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = super.getView(position, convertView, parent) as TextView
        
        // Устанавливаем фон в зависимости от того, выбран ли элемент
        if (position == selectedPosition) {
            view.setBackgroundColor(ContextCompat.getColor(context, R.color.item_selected))
        } else {
            view.setBackgroundColor(ContextCompat.getColor(context, R.color.item))
        }
        
        return view
    }
    
    /**
     * Уведомляет об изменении конкретного элемента
     */
    private fun notifyItemChanged(position: Int) {
        if (position >= 0 && position < count) {
            notifyDataSetChanged()
        }
    }
}

