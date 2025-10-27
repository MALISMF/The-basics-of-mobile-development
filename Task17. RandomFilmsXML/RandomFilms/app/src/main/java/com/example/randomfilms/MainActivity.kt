package com.example.randomfilms

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import java.util.*

class MainActivity : AppCompatActivity() {

    lateinit var movies: Array<String>
    private var remainingMovies: MutableList<String> = mutableListOf()
    val r = Random()
    val m = Movie("Inception", 2010, 9.0f)

    override fun onStart() {
        super.onStart()
        Log.d("mytag", "onStart()")
    }

    override fun onStop() {
        super.onStop()
        Log.d("mytag", "onStop()")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        
        movies = resources.getStringArray(R.array.movies)
        remainingMovies.addAll(movies.toList())
        
        Log.d("mytag", movies[0])

        val pref = getPreferences(Context.MODE_PRIVATE)
        val editor = pref.edit()
        editor.putString("name", "Vasya")
        editor.apply()
        Log.d("mytag", "name:" + pref.getString("name", ""))
    }

    fun onNextClick(view: View) {
        val tvTitle = findViewById<TextView>(R.id.movieText)
        val nextButton = findViewById<TextView>(R.id.nextButton)
        nextButton.text = "Следующий фильм"

        if (remainingMovies.isNotEmpty()) {
            val randomIndex = r.nextInt(remainingMovies.size)
            val selectedMovie = remainingMovies[randomIndex]
            tvTitle.text = selectedMovie
            remainingMovies.removeAt(randomIndex)
        } else {
            tvTitle.text = "Фильмы закончились."
        }
    }

    fun onClearClick(view: View) {
        val tvTitle = findViewById<TextView>(R.id.movieText)
        val nextButton = findViewById<TextView>(R.id.nextButton)
        nextButton.text = "Выбрать фильм"

        remainingMovies.clear()
        remainingMovies.addAll(movies.toList())
        tvTitle.text = "Нажмите кнопку для выбора фильма"
        Log.d("mytag", "Reset movies")
    }
}