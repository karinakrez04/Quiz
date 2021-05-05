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
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
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
    private var mIncorrectAnswers: Int = 0

    //Была ли уже проверка
    private var isAnswerChecked = false

    // TODO (STEP 3: Create a variable for getting the name from intent.)
    private var mUserName: String? = null

    private val optionsList by lazy {
        listOf(
            binding.tvOptionOne,
            binding.tvOptionTwo,
            binding.tvOptionThree,
            binding.tvOptionFour
        )
    }

    /**
     * This function is auto created by Android when the Activity Class is created.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        database = Firebase.database

        blockViews(false)
        loadQuestion()
        //Обработка нажатия на кнопку "Помощь друга"
        binding.btnFriend.setOnClickListener {
            val intent = Intent()
            intent.action = Intent.ACTION_SEND
            intent.putExtra(Intent.EXTRA_TEXT, buildString {
                append("${mCurrentQuestion?.imageLink}\n")
                append("${binding.tvQuestion.text}\n")
                optionsList.forEachIndexed { index, textView ->
                    if (textView.isEnabled) append("${index + 1}. ${textView.text}")
                }
            })
            intent.type = "text/plain"
            startActivity(Intent.createChooser(intent, "Share To:"))
        }

        //Обработка нажатия на кнопку "50/50"
        binding.btnFifty.setOnClickListener {
            if (mCurrentQuestion != null) {
                //Индексы
                val answersToBlock = mutableListOf(0, 1, 2, 3)

                //Индекс правильного ответа
                val correctAnswerIndex = mCurrentQuestion!!.correctAnswer!! - 1
                answersToBlock.removeAt(correctAnswerIndex)

                //Рандомный индекс, чтобы оставить рандомный ответ с правильным
                val randomIndex = (0..2).random()
                answersToBlock.removeAt(randomIndex)

                for (index in 0..3) {
                    if (answersToBlock.contains(index)) {
                        colorAnswerView(index + 1, R.drawable.blocked_option_border_bg)
                        blockAnswerView(index + 1)
                    }
                }
            }
        }

        // TODO (STEP 4: Get the NAME from intent and assign it the variable.)
        // START
        mUserName = intent.getStringExtra(Constants.USER_NAME)
        // END

        binding.tvOptionOne.setOnClickListener(this)
        binding.tvOptionTwo.setOnClickListener(this)
        binding.tvOptionThree.setOnClickListener(this)
        binding.tvOptionFour.setOnClickListener(this)
        binding.btnCheck.setOnClickListener(this)
        binding.btnNextQuestion.setOnClickListener(this)
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

            R.id.btn_check -> {
                //Нужно проверить ответ
                Log.i(
                    "TAG",
                    "Checking $mCurrentPosition question, mSelectedOptionPosition - $mSelectedOptionPosition"
                )
                when (mSelectedOptionPosition) {
                    //Если ничего не выбрано
                    NOTHING_SELECTED -> {
                        if (mCurrentPosition == 9) {
                            startResultActivity()
                        } else checkNextQuestion()
                    }
                    else -> { //Если ответ правильный
                        if (mSelectedOptionPosition == mCurrentQuestion?.correctAnswer) {
                            //Добавить ОЧКО ВЕРНОСТИ
                            mCorrectAnswers++
                            //Покрасить ответ в зеленый
                            colorAnswerView(
                                mSelectedOptionPosition,
                                R.drawable.correct_option_border_bg
                            )
                        } else { //Если ответ неверный
                            //Добавить ОЧКО НЕВЕРНОСТИ
                            mIncorrectAnswers++
                            //Покрасить выбранный в красный
                            colorAnswerView(
                                mSelectedOptionPosition,
                                R.drawable.wrong_option_border_bg
                            )
                            //Покрасить правильный в зеленый
                            colorAnswerView(
                                mCurrentQuestion!!.correctAnswer!!,
                                R.drawable.correct_option_border_bg
                            )
                        }
                        //Заблокировать кнопки помощи, потому что после проверки они не нужны
                        blockHelpButtons(false)
                        checkButtonVisible(false)
                        isAnswerChecked = true
                    }
                }
            }

            R.id.btn_next_question -> checkNextQuestion()
        }
    }

    private fun checkNextQuestion() {
        mCurrentPosition++
        mSelectedOptionPosition = NOTHING_SELECTED
        if (mCurrentPosition < 10) {
            if (mCurrentPosition == 9) {
                binding.btnNextQuestion.apply {
                    text = "Конец"
                    setOnClickListener(resultClickListener)
                }
            }
            loadQuestion()
            blockHelpButtons(true)
            unblockAnswerButtons()
        }
    }

    private val resultClickListener = View.OnClickListener { startResultActivity() }

    private fun startResultActivity() {
        startActivity(
            Intent(
                this@QuizQuestionsActivity,
                ResultActivity::class.java
            ).apply {
                putExtra(Constants.USER_NAME, mUserName)
                putExtra(Constants.TOTAL_QUESTIONS, 10)
                putExtra(Constants.CORRECT_ANSWERS, mCorrectAnswers)
                putExtra(Constants.INCORRECT_ANSWERS, mIncorrectAnswers)
            }
        )
        finish()
    }

    //Менять видимость кнопки "Проверить"
    private fun checkButtonVisible(visible: Boolean) {
        binding.btnCheck.isVisible = visible
    }

    //Заблокировать кнопки помощи
    private fun blockHelpButtons(enabled: Boolean) {
        binding.btnFifty.isEnabled = enabled
        binding.btnFriend.isEnabled = enabled
    }

    //Разблокировать кнопки ответов
    private fun unblockAnswerButtons() {
        binding.apply {
            tvOptionOne.isEnabled = true
            tvOptionTwo.isEnabled = true
            tvOptionThree.isEnabled = true
            tvOptionFour.isEnabled = true
        }
    }

    /**
     * A function for setting the question to UI components.
     */
    private fun loadQuestion() {
        Log.i("TAG", "loadQuestion, mCurrentPosition - $mCurrentPosition")
        binding.btnCheck.text = "Проверить"
        defaultOptionsView()
        database.getReference("questions").child("$mCurrentPosition")
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    mCurrentQuestion = snapshot.getValue(Question::class.java)
                    Log.i("TAG", "Question - $mCurrentQuestion")
                    defaultOptionsView()
                    if (mCurrentQuestion != null) {
                        binding.btnCheck.text = "Проверить"
                        binding.progressBar.progress = mCurrentQuestion!!.id!!
                        binding.tvProgress.text =
                            "${mCurrentQuestion!!.id!!} / ${binding.progressBar.max}"
                        binding.tvQuestion.text = mCurrentQuestion!!.question
                        Glide.with(this@QuizQuestionsActivity)
                            .load(mCurrentQuestion!!.imageLink)
                            .into(binding.ivImage)
                        //binding.ivImage.setImageResource(mCurrentQuestion!!.image!!)
                        binding.tvOptionOne.text = mCurrentQuestion!!.optionOne
                        binding.tvOptionTwo.text = mCurrentQuestion!!.optionTwo
                        binding.tvOptionThree.text = mCurrentQuestion!!.optionThree
                        binding.tvOptionFour.text = mCurrentQuestion!!.optionFour
                    }
                    blockViews(true)
                    checkButtonVisible(true)
                    defaultOptionsView()
                    isAnswerChecked = false
                }

                override fun onCancelled(error: DatabaseError) {
                    blockViews(true)
                    checkButtonVisible(true)
                }
            })
    }

    /**
     * A function to set the view of selected option view.
     */
    private fun selectedOptionView(tv: TextView, selectedOptionNum: Int) {
        if (!isAnswerChecked) {
            if (mSelectedOptionPosition != NOTHING_SELECTED) {
                defaultOptionsView()
            }
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
    }

    /**
     * A function to set default options view when the new question is loaded or when the answer is reselected.
     */
    private fun defaultOptionsView() {
        for (option in optionsList) {
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
    private fun colorAnswerView(answer: Int, @DrawableRes drawableView: Int) {
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

    private fun blockAnswerView(answer: Int) {
        when (answer) {
            1 -> binding.tvOptionOne.isEnabled = false
            2 -> binding.tvOptionTwo.isEnabled = false
            3 -> binding.tvOptionThree.isEnabled = false
            4 -> binding.tvOptionFour.isEnabled = false
        }
    }

    //Заблокировать все кнопки
    private fun blockViews(enabled: Boolean) {
        binding.apply {
            tvOptionOne.isEnabled = enabled
            tvOptionTwo.isEnabled = enabled
            tvOptionThree.isEnabled = enabled
            tvOptionFour.isEnabled = enabled
            btnCheck.isEnabled = enabled
            btnNextQuestion.isEnabled = enabled
        }
    }

    companion object {
        const val NOTHING_SELECTED = 0
    }
}