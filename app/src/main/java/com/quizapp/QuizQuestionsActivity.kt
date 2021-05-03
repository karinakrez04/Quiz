package com.quizapp

import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.annotation.DrawableRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.hoc081098.viewbindingdelegate.viewBinding
import com.quizapp.databinding.ActivityQuizQuestionsBinding

class QuizQuestionsActivity : AppCompatActivity(R.layout.activity_quiz_questions),
    View.OnClickListener {
    private val binding by viewBinding<ActivityQuizQuestionsBinding>()
    private var mCurrentPosition: Int = 0 // Default and the first question position
    private lateinit var database: FirebaseDatabase
    private var mCurrentQuestion: Question? = null

    //Позиция выбранного ответа
    private var mSelectedOptionPosition: Int = 0

    //Счетчик правильных ответов
    private var mCorrectAnswers: Int = 0

    // TODO (STEP 3: Create a variable for getting the name from intent.)
    private var mUserName: String? = null

    /**
     * This function is auto created by Android when the Activity Class is created.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        database = Firebase.database

        loadQuestion()
        //Обработка нажатия на кнопку "Помощь друга"
        binding.btnFriend.setOnClickListener {
            val intent = Intent()
            intent.action = Intent.ACTION_SEND
            intent.putExtra(Intent.EXTRA_TEXT, buildString {
                append("${binding.tvQuestion.text}\n")
                append("1. ${binding.tvOptionOne.text}\n")
                append("2. ${binding.tvOptionTwo.text}\n")
                append("3. ${binding.tvOptionThree.text}\n")
                append("4. ${binding.tvOptionFour.text}\n")
            })
            intent.type = "text/plain"
            startActivity(Intent.createChooser(intent, "Share To:"))
        }

        // TODO (STEP 4: Get the NAME from intent and assign it the variable.)
        // START
        mUserName = intent.getStringExtra(Constants.USER_NAME)
        // END

        binding.tvOptionOne.setOnClickListener(this)
        binding.tvOptionTwo.setOnClickListener(this)
        binding.tvOptionThree.setOnClickListener(this)
        binding.tvOptionFour.setOnClickListener(this)
        binding.btnSubmit.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.tv_option_one -> {
                selectedOptionView(binding.tvOptionOne, 1)
            }

            R.id.tv_option_two -> {
                selectedOptionView(binding.tvOptionTwo, 2)
            }

            R.id.tv_option_three -> {
                selectedOptionView(binding.tvOptionThree, 3)
            }

            R.id.tv_option_four -> {
                selectedOptionView(binding.tvOptionFour, 4)
            }

            R.id.btn_submit -> {
                //Если ответ уже проверен
                if (mSelectedOptionPosition == CHECKED) {
                    //Если вопросы еще есть
                    if (mCurrentPosition < 10) {
                        loadQuestion()
                    } else { //Если вопросы кончились
                        binding.btnSubmit.apply {
                            text = "Конец"
                            setOnClickListener {
                                startActivity(
                                    Intent(
                                        this@QuizQuestionsActivity,
                                        ResultActivity::class.java
                                    ).apply {
                                        putExtra(Constants.USER_NAME, mUserName)
                                        putExtra(Constants.TOTAL_QUESTIONS, 10)
                                        putExtra(Constants.CORRECT_ANSWERS, mCorrectAnswers)
                                    }
                                )
                                finish()
                            }
                        }
                    }
                    mSelectedOptionPosition = NOTHING_SELECTED
                }
                //Нужно проверить ответ
                else {
                    Log.i("TAG", "Checking")
                    //Добавить единицу к позиции вопроса
                    mCurrentPosition++
                    //Проверить
                    when (mSelectedOptionPosition) {
                        NOTHING_SELECTED -> {
                            //Перейти к новому вопросу
                            loadQuestion()
                        }
                        else -> {
                            //Если ответ правильный
                            if (mSelectedOptionPosition == mCurrentQuestion!!.correctAnswer) {
                                //Добавить ОЧКО
                                mCorrectAnswers++
                                //Покрасить ответ в зеленый
                                answerView(
                                    mSelectedOptionPosition,
                                    R.drawable.correct_option_border_bg
                                )
                            } else { //Если ответ неверный
                                //Покрасить выбранный в красный
                                answerView(
                                    mSelectedOptionPosition,
                                    R.drawable.wrong_option_border_bg
                                )
                                //Покрасить правильный в зеленый
                                answerView(
                                    mCurrentQuestion!!.correctAnswer!!,
                                    R.drawable.correct_option_border_bg
                                )
                            }
                            mSelectedOptionPosition = CHECKED
                            binding.btnSubmit.text = "Следующий вопрос"
                        }
                    }
                }
            }
        }
    }

    /**
     * A function for setting the question to UI components.
     */
    private fun loadQuestion() {
        Log.i("TAG", "loadQuestion")
        binding.btnSubmit.text = "Проверить"
        defaultOptionsView()
        database.getReference("questions").child("$mCurrentPosition")
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    mCurrentQuestion = snapshot.getValue(Question::class.java)
                    Log.i("TAG", "Question - $mCurrentQuestion")
                    defaultOptionsView()
                    if (mCurrentQuestion == null) {
                        binding.btnSubmit.text = "Конец"
                    } else {
                        binding.btnSubmit.text = "Проверить"
                        binding.progressBar.progress = mCurrentPosition + 1
                        binding.tvProgress.text =
                            "${mCurrentPosition + 1} / ${binding.progressBar.max}"
                        binding.tvQuestion.text = mCurrentQuestion!!.question
                        binding.ivImage.setImageResource(mCurrentQuestion!!.image!!)
                        binding.tvOptionOne.text = mCurrentQuestion!!.optionOne
                        binding.tvOptionTwo.text = mCurrentQuestion!!.optionTwo
                        binding.tvOptionThree.text = mCurrentQuestion!!.optionThree
                        binding.tvOptionFour.text = mCurrentQuestion!!.optionFour
                    }
                }

                override fun onCancelled(error: DatabaseError) {}
            })
    }

    /**
     * A function to set the view of selected option view.
     */
    private fun selectedOptionView(tv: TextView, selectedOptionNum: Int) {
        defaultOptionsView()
        //Запоминаем позицию выбранного ответа
        mSelectedOptionPosition = selectedOptionNum

        tv.setTextColor(
            Color.parseColor("#363A43")
        )
        tv.setTypeface(tv.typeface, Typeface.BOLD)
        tv.background = ContextCompat.getDrawable(
            this@QuizQuestionsActivity,
            R.drawable.selected_option_border_bg
        )
    }

    /**
     * A function to set default options view when the new question is loaded or when the answer is reselected.
     */
    private fun defaultOptionsView() {
        val options = ArrayList<TextView>()
        options.add(0, binding.tvOptionOne)
        options.add(1, binding.tvOptionTwo)
        options.add(2, binding.tvOptionThree)
        options.add(3, binding.tvOptionFour)

        for (option in options) {
            option.setTextColor(Color.parseColor("#7A8089"))
            option.typeface = Typeface.DEFAULT
            option.background = ContextCompat.getDrawable(
                this@QuizQuestionsActivity,
                R.drawable.default_option_border_bg
            )
        }
    }

    /**
     * A function for answer view which is used to highlight the answer is wrong or right.
     */
    private fun answerView(answer: Int, @DrawableRes drawableView: Int) {
        when (answer) {
            1 -> {
                binding.tvOptionOne.background = ContextCompat.getDrawable(
                    this@QuizQuestionsActivity,
                    drawableView
                )
            }
            2 -> {
                binding.tvOptionTwo.background = ContextCompat.getDrawable(
                    this@QuizQuestionsActivity,
                    drawableView
                )
            }
            3 -> {
                binding.tvOptionThree.background = ContextCompat.getDrawable(
                    this@QuizQuestionsActivity,
                    drawableView
                )
            }
            4 -> {
                binding.tvOptionFour.background = ContextCompat.getDrawable(
                    this@QuizQuestionsActivity,
                    drawableView
                )
            }
        }
    }

    companion object {
        const val NOTHING_SELECTED = 0
        const val CHECKED = -1
    }
}