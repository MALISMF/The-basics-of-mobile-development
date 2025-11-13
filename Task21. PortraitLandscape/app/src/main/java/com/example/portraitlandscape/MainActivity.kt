package com.example.portraitlandscape

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener {

    private lateinit var adapter: ArrayAdapter<CharSequence>
    private lateinit var pictureView: ImageView
    private lateinit var picturesSpinner: Spinner
    private val preferences by lazy { getPreferences(MODE_PRIVATE) }

    private val pictureResources = listOf(
        R.drawable.car1,
        R.drawable.car2,
        R.drawable.car3
    )

    private var currentPictureIndex = 0
    private var suppressSpinnerCallback = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        pictureView = findViewById(R.id.picture)
        picturesSpinner = findViewById(R.id.pictures_list)

        adapter = ArrayAdapter.createFromResource(this, R.array.pictures, R.layout.item).apply {
            setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        }
        picturesSpinner.adapter = adapter
        picturesSpinner.onItemSelectedListener = this

        currentPictureIndex = loadSavedPictureIndex()
        updatePicture()
        suppressSpinnerCallback = true
        picturesSpinner.setSelection(currentPictureIndex, false)
    }

    fun onChangePictureClick(@Suppress("UNUSED_PARAMETER") v: View) {
        currentPictureIndex = (currentPictureIndex + 1) % pictureResources.size
        updatePicture()
        saveCurrentPicture()
        suppressSpinnerCallback = true
        picturesSpinner.setSelection(currentPictureIndex, false)
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        if (suppressSpinnerCallback) {
            suppressSpinnerCallback = false
            return
        }

        if (position in pictureResources.indices) {
            currentPictureIndex = position
            updatePicture()
            saveCurrentPicture()
            Toast.makeText(this, getString(R.string.picture_selected_toast, position + 1), Toast.LENGTH_SHORT).show()
        }
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
        Toast.makeText(this, R.string.picture_not_selected_toast, Toast.LENGTH_SHORT).show()
    }

    private fun updatePicture() {
        pictureView.setImageResource(pictureResources[currentPictureIndex])
    }

    private fun saveCurrentPicture() {
        preferences.edit()
            .putInt(PREF_SELECTED_PICTURE_INDEX, currentPictureIndex)
            .apply()
    }

    private fun loadSavedPictureIndex(): Int {
        val savedIndex = preferences.getInt(PREF_SELECTED_PICTURE_INDEX, DEFAULT_PICTURE_INDEX)
        return savedIndex.coerceIn(pictureResources.indices)
    }

    companion object {
        private const val PREF_SELECTED_PICTURE_INDEX = "pref_selected_picture_index"
        private const val DEFAULT_PICTURE_INDEX = 0
    }
}