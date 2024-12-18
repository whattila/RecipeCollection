package hu.bme.aut.recipecollection.timer

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import hu.bme.aut.recipecollection.R
import kotlinx.android.synthetic.main.activity_timer.*

class TimerActivity : AppCompatActivity() {
    lateinit var timer: CountDownTimer
    var isRunning = false
    private var timeInMillis = 60000L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_timer)
        timeInMillis *= intent.getIntExtra(EXTRA_TIME, 1) // Int extra is definitely larger than 0; it is checked before creating the Intent
        updateText()
        btnControl.setOnClickListener {
            if (isRunning)
                pauseTimer()
             else {
                startTimer()
            }
        }
    }

    private fun pauseTimer() {
        btnControl.text = getString(R.string.start)
        timer.cancel()
        isRunning = false
    }

    private fun startTimer() {
        timer = object : CountDownTimer(timeInMillis, 1000){
            override fun onFinish() {
                ready.text = getString(R.string.ready)
                btnControl.isEnabled = false
            }
            override fun onTick(millisUntilFinished: Long) {
                timeInMillis = millisUntilFinished
                updateText()
            }
        }
        timer.start()
        btnControl.text = getString(R.string.pause)
        isRunning = true
        ready.text = getString(R.string.in_progress)
    }

    private fun updateText() {
        val minute = (timeInMillis / 1000) / 60
        val seconds = (timeInMillis / 1000) % 60

        time.text = "$minute:$seconds"
    }

    companion object {
        const val EXTRA_TIME = "extra_time"
    }
}