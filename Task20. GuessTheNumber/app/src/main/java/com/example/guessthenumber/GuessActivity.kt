package com.example.guessthenumber

import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.button.MaterialButton

class GuessActivity : AppCompatActivity() {

    private lateinit var attemptTextView: TextView
    private lateinit var rangeTextView: TextView
    private lateinit var questionTextView: TextView
    private lateinit var moreButton: MaterialButton
    private lateinit var lessButton: MaterialButton
    private lateinit var equalButton: MaterialButton
    private lateinit var restartButton: MaterialButton

    private var lowerBound: Int = 0
    private var upperBound: Int = 0
    private var currentGuess: Int = 0
    private var attemptCount: Int = 1
    private var isFinished: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_guess)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.guessRoot)) { view, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        attemptTextView = findViewById(R.id.attemptTextView)
        rangeTextView = findViewById(R.id.rangeTextView)
        questionTextView = findViewById(R.id.questionTextView)
        moreButton = findViewById(R.id.moreButton)
        lessButton = findViewById(R.id.lessButton)
        equalButton = findViewById(R.id.equalButton)
        restartButton = findViewById(R.id.restartButton)

        setupListeners()

        if (savedInstanceState != null) {
            lowerBound = savedInstanceState.getInt(STATE_LOWER_BOUND)
            upperBound = savedInstanceState.getInt(STATE_UPPER_BOUND)
            currentGuess = savedInstanceState.getInt(STATE_CURRENT_GUESS)
            attemptCount = savedInstanceState.getInt(STATE_ATTEMPT_COUNT)
            isFinished = savedInstanceState.getBoolean(STATE_IS_FINISHED)
        } else {
            val hasMin = intent.hasExtra(EXTRA_MIN_VALUE)
            val hasMax = intent.hasExtra(EXTRA_MAX_VALUE)

            if (!hasMin || !hasMax) {
                Toast.makeText(this, R.string.guess_missing_range, Toast.LENGTH_SHORT).show()
                finish()
                return
            }

            lowerBound = intent.getIntExtra(EXTRA_MIN_VALUE, DEFAULT_MIN)
            upperBound = intent.getIntExtra(EXTRA_MAX_VALUE, DEFAULT_MAX)

            if (lowerBound > upperBound) {
                showInconsistentState()
                return
            }

            attemptCount = 1
            currentGuess = computeGuess(lowerBound, upperBound)
        }

        if (isFinished) {
            showInconsistentState()
        } else {
            refreshUi()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(STATE_LOWER_BOUND, lowerBound)
        outState.putInt(STATE_UPPER_BOUND, upperBound)
        outState.putInt(STATE_CURRENT_GUESS, currentGuess)
        outState.putInt(STATE_ATTEMPT_COUNT, attemptCount)
        outState.putBoolean(STATE_IS_FINISHED, isFinished)
    }

    private fun setupListeners() {
        moreButton.setOnClickListener {
            if (isFinished) return@setOnClickListener
            lowerBound = currentGuess + 1
            handleNextGuess()
        }

        lessButton.setOnClickListener {
            if (isFinished) return@setOnClickListener
            upperBound = currentGuess - 1
            handleNextGuess()
        }

        equalButton.setOnClickListener {
            if (isFinished) return@setOnClickListener
            isFinished = true
            Toast.makeText(
                this,
                getString(R.string.guess_success_message, attemptCount),
                Toast.LENGTH_SHORT
            ).show()
            finish()
        }

        restartButton.setOnClickListener { finish() }
    }

    private fun handleNextGuess() {
        if (lowerBound > upperBound) {
            showInconsistentState()
            return
        }

        attemptCount += 1
        currentGuess = computeGuess(lowerBound, upperBound)
        refreshUi()
    }

    private fun refreshUi() {
        attemptTextView.text = getString(R.string.guess_title, attemptCount)
        rangeTextView.text = getString(R.string.guess_range, lowerBound, upperBound)
        questionTextView.text = getString(R.string.guess_question, currentGuess)
    }

    private fun showInconsistentState() {
        isFinished = true
        moreButton.isEnabled = false
        lessButton.isEnabled = false
        equalButton.isEnabled = false
        restartButton.visibility = View.VISIBLE
        questionTextView.text = getString(R.string.guess_inconsistent_state)
        Toast.makeText(this, R.string.guess_inconsistent_state, Toast.LENGTH_LONG).show()
    }

    private fun computeGuess(min: Int, max: Int): Int = min + (max - min) / 2

    companion object {
        const val EXTRA_MIN_VALUE = "extra_min_value"
        const val EXTRA_MAX_VALUE = "extra_max_value"

        private const val DEFAULT_MIN = 0
        private const val DEFAULT_MAX = 0

        private const val STATE_LOWER_BOUND = "state_lower_bound"
        private const val STATE_UPPER_BOUND = "state_upper_bound"
        private const val STATE_CURRENT_GUESS = "state_current_guess"
        private const val STATE_ATTEMPT_COUNT = "state_attempt_count"
        private const val STATE_IS_FINISHED = "state_is_finished"
    }
}

