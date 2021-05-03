package com.quizapp

import android.content.ContentValues
import android.content.ContentValues.TAG
import android.util.Log
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase


object Constants {


    // TODO (STEP 1: Create a constant variables which we required in the result screen.)
    // START
    const val USER_NAME: String = "user_name"
    const val TOTAL_QUESTIONS: String = "total_questions"
    const val CORRECT_ANSWERS: String = "correct_answers"
    // END
    
    fun getQuestions(): ArrayList<Question> {
        val questionsList = ArrayList<Question>()



        // 1
        val que1 = Question(
            1, "Какой это самолет?",
            R.drawable.sr,
            "Lockheed SR-71", "Боинг 707",
            "Cessna 172", "Hughes H-4 Hercules", 1
        )

        questionsList.add(que1)

        // 2
        val que2 = Question(
            2, "Какой это самолет?",
            R.drawable.tu,
            "Cessna 172", "Боинг 747",
            "ТУ-144", "Douglas DC-3", 3
        )

        questionsList.add(que2)

        // 3
        val que3 = Question(
            3, "Какой это самолет?",
            R.drawable.boeing,
            "Боинг 747", "Cessna 172",
            "Боинг 656", "Боинг 707", 4
        )

        questionsList.add(que3)

        // 4
        val que4 = Question(
            4, "Какой это самолет?",
            R.drawable.b,
            "Douglas DC-3", "B-29 «Суперфортресс»",
            "АН-225 «Мрия»", "Flyer I", 2
        )

        questionsList.add(que4)

        // 5
        val que5 = Question(
            5, "Какой это самолет?",
            R.drawable.cessna,
            " B-29 «Суперфортресс»", "АН-225 «Мрия»",
            "Cessna 172", "Douglas DC-3", 3
        )

        questionsList.add(que5)

        // 6
        val que6 = Question(
            6, "Какой это самолет?",
            R.drawable.douglas,
            "Douglas DC-3", "Cessna 172",
            "Боинг 747", "Hughes H-4 Hercules", 1
        )

        questionsList.add(que6)

        // 7
        val que7 = Question(
            7, "Какой это самолет?",
            R.drawable.hughes,
            "Flyer I", "АН-225 «Мрия»",
            "Hughes H-4 Hercules", "Douglas DC-3", 3
        )

        questionsList.add(que7)

        // 8
        val que8 = Question(
            8, "Какой это самолет?",
            R.drawable.first,
            "Боинг 746", "B-29 «Суперфортресс»",
            "Douglas DC-3", "Боинг 747", 4
        )

        questionsList.add(que8)

        // 9
        val que9 = Question(
            9, "Какой это самолет?",
            R.drawable.antonov,
            "Douglas DC-3", "АН-225 «Мрия»",
            "Cessna 172", "B-29 «Суперфортресс»", 2
        )

        questionsList.add(que9)

        // 10
        val que10 = Question(
            10, "Какой это самолет?",
            R.drawable.flyer,
            "Flyer I", "Cessna 172",
            "ТУ-144", "Flyer II", 1
        )

        questionsList.add(que10)

        return questionsList
    }
}