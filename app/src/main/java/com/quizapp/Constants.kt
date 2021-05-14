package com.quizapp


object Constants {
    // TODO (STEP 1: Create a constant variables which we required in the result screen.)
    // START
    const val USER_NAME: String = "user_name"
    const val TOTAL_QUESTIONS: String = "total_questions"
    const val CORRECT_ANSWERS: String = "correct_answers"
    const val INCORRECT_ANSWERS: String = "incorrect_answers"
    // END
    
    fun getQuestions(): ArrayList<Question> {
        val questionsList = ArrayList<Question>()

        // 1
        questionsList.add(
            Question(
                1, "Какой это самолет?",
                R.drawable.sr,
                "https://firebasestorage.googleapis.com/v0/b/project-1342928702433724578.appspot.com/o/planes%2F1.png?alt=media&token=90070b01-bc27-4ab8-b44d-a80a938cbef5",
                "Lockheed SR-71", "Боинг 707",
                "Cessna 172", "Hughes H-4 Hercules", 1
            )
        )

        // 2
        questionsList.add(
            Question(
                2, "Какой это самолет?",
                R.drawable.tu,
                "https://firebasestorage.googleapis.com/v0/b/project-1342928702433724578.appspot.com/o/planes%2F2.png?alt=media&token=d7fef667-99f5-46c3-a5fa-92f5535b6b9f",
                "Cessna 172", "Боинг 747",
                "ТУ-144", "Douglas DC-3", 3
            )
        )

        // 3
        questionsList.add(
            Question(
                3, "Какой это самолет?",
                R.drawable.boeing,
                "",
                "Боинг 747", "Cessna 172",
                "Боинг 656", "Боинг 707", 4
            )
        )

        // 4
        val que4 = Question(
            4, "Какой это самолет?",
            R.drawable.b,
            "",
            "Douglas DC-3", "B-29 «Суперфортресс»",
            "АН-225 «Мрия»", "Flyer I", 2
        )

        questionsList.add(que4)

        // 5
        val que5 = Question(
            5, "Какой это самолет?",
            R.drawable.cessna,
            "",
            " B-29 «Суперфортресс»", "АН-225 «Мрия»",
            "Cessna 172", "Douglas DC-3", 3
        )

        questionsList.add(que5)

        // 6
        val que6 = Question(
            6, "Какой это самолет?",
            R.drawable.douglas,
            "",
            "Douglas DC-3", "Cessna 172",
            "Боинг 747", "Hughes H-4 Hercules", 1
        )

        questionsList.add(que6)

        // 7
        val que7 = Question(
            7, "Какой это самолет?",
            R.drawable.hughes,
            "",
            "Flyer I", "АН-225 «Мрия»",
            "Hughes H-4 Hercules", "Douglas DC-3", 3
        )

        questionsList.add(que7)

        // 8
        val que8 = Question(
            8, "Какой это самолет?",
            R.drawable.first,
            "",
            "Боинг 746", "B-29 «Суперфортресс»",
            "Douglas DC-3", "Боинг 747", 4
        )

        questionsList.add(que8)

        // 9
        val que9 = Question(
            9, "Какой это самолет?",
            R.drawable.antonov,
            "",
            "Douglas DC-3", "АН-225 «Мрия»",
            "Cessna 172", "B-29 «Суперфортресс»", 2
        )

        questionsList.add(que9)

        // 10
        val que10 = Question(
            10, "Какой это самолет?",
            R.drawable.flyer,
            "",
            "Flyer I", "Cessna 172",
            "ТУ-144", "Flyer II", 1
        )

        questionsList.add(que10)

        // 11
        val que11 = Question(
            11, "Какой это самолет?",
            R.drawable.flyer,
            "",
            "Flyer I", "Cessna 172",
            "Cessna 172", "Flyer II", 1
        )

        questionsList.add(que11)

        return questionsList
    }
}