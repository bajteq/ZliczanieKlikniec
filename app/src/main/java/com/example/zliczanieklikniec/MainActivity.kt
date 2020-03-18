package com.example.zliczanieklikniec

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.os.PersistableBundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import android.util.Log


class MainActivity : AppCompatActivity() {

    private lateinit var gameScoreTextView: TextView
    private lateinit var timeLeftTextView: TextView
    private lateinit var tapButton: Button

    private val TAG = MainActivity::class.java.simpleName

    companion object {
        private const val SCORE_KEY = "SCORE_KEY"
        private const val TIME_LEFT_KEY = "TIME_LEFT_KEY"
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        gameScoreTextView = findViewById(R.id.game_score_text_view)
        timeLeftTextView = findViewById(R.id.time_left_text_view)
        tapButton = findViewById(R.id.tap_me_button)
        resetGame()
        tapButton.setOnClickListener {incrementScore()}
        if (savedInstanceState != null) {
            score = savedInstanceState.getInt(SCORE_KEY)
            timeLeft = savedInstanceState.getInt(TIME_LEFT_KEY)
            restoreGame()
        } else {
            resetGame()
        }
    }

    private var score = 0
    private var gameStarted = false
    private lateinit var countDownTimer: CountDownTimer
    private var initialCountDown: Long = 60000
    private var countDownInterval: Long = 1000
    private var timeLeft = 60


    private fun incrementScore() {
        if(!gameStarted){
            startGame()
        }
        score++
        val newScore: String = getString(R.string.your_score, score)
        gameScoreTextView.text = newScore
    }


    private fun resetGame() {
        score = 0
        val initialScore: String = getString(R.string.your_score, score)
        gameScoreTextView.text = initialScore
        val initialTimeLeft: String = getString(R.string.time_left, 60)
        timeLeftTextView.text = initialTimeLeft
        gameStarted = false
        countDownTimer = object : CountDownTimer(initialCountDown, countDownInterval) {
            override fun onTick(millisUntilFinished: Long) {
                timeLeft = millisUntilFinished.toInt()/1000
                val timeLeftString: String = getString(R.string.time_left, timeLeft)
                timeLeftTextView.text = timeLeftString
            }

            override fun onFinish() {
                endGame()
            }
        }

    }
    private fun startGame() {
        countDownTimer.start()
        gameStarted = true
    }
    private fun endGame() {
        Toast.makeText(this, getString(R.string.game_over_message, score),
            Toast.LENGTH_LONG).show()
        resetGame()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(SCORE_KEY, score)
        outState.putInt(TIME_LEFT_KEY, timeLeft)
        countDownTimer.cancel()

    }
    private fun restoreGame() {
        val restoredScore: String = getString(R.string.your_score, score)
        gameScoreTextView.text = restoredScore

        val restoredTime: String = getString(R.string.time_left, timeLeft)
        timeLeftTextView.text = restoredTime
        countDownTimer = object : CountDownTimer((timeLeft*1000).toLong(),
            countDownInterval){

            override fun onTick(millisUntilFinished: Long) {
                timeLeft = millisUntilFinished.toInt() /1000
                val timeLeftString : String = getString(R.string.time_left, timeLeft)
                timeLeftTextView.text = timeLeftString
            }
            override fun onFinish() {
                endGame() }
        }
        countDownTimer.start()
        gameStarted = true
    }
}