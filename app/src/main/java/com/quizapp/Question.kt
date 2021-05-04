package com.quizapp

data class Question(
    val id: Int? = null,
    val question: String? = null,
    val image: Int? = null,
    val imageLink: String? = null,
    val optionOne: String? = null,
    val optionTwo: String? = null,
    val optionThree: String? = null,
    val optionFour: String? = null,
    val correctAnswer: Int? = null
)