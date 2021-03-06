package com.quizapp

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.hoc081098.viewbindingdelegate.viewBinding
import com.quizapp.databinding.ActivityResultBinding


class ResultActivity : AppCompatActivity(R.layout.activity_result) {
    private val binding by viewBinding<ActivityResultBinding>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // TODO (STEP 6: Hide the status bar and get the details from intent and set it to the UI. And also add a click event to the finish button.)
        // START
        // Hide the status bar.
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN

        val userName = intent.getStringExtra(Constants.USER_NAME)
        val totalQuestions = intent.getIntExtra(Constants.TOTAL_QUESTIONS, 0)
        val correctAnswers = intent.getIntExtra(Constants.CORRECT_ANSWERS, 0)
        val incorrectAnswers = intent.getIntExtra(Constants.INCORRECT_ANSWERS, 0)

        StatisticsSaver.saveNewResult("$userName - $correctAnswers/$totalQuestions")

        binding.tvName.text = userName
        binding.tvScore.text = "Ваш счет $correctAnswers из $totalQuestions"
        binding.correctAnswers.text = "Правильные $correctAnswers"
        binding.incorrectAnswers.text = "Неправильные $incorrectAnswers"
        binding.unansweredAnswers.text =
            "Не отвеченные ${totalQuestions - (correctAnswers + incorrectAnswers)}"

        binding.btnFinish.setOnClickListener {
            startActivity(Intent(this@ResultActivity, MainActivity::class.java))
            finish()
        }

        binding.btnRecord.setOnClickListener {
            startActivity(Intent(this@ResultActivity, StatisticsActivity::class.java))
        }
        // END
    }
}