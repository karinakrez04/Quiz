package com.quizapp

import java.io.File

object StatisticsSaver {
    //Название файла, в котором сохранена история
    private const val fileName = "statistics.txt"
    lateinit var file: File

    //Инициализация объекта файл для последующей работы с ним
    fun initPath(path: String) {
        file = File(path, fileName)
    }

    //Сохранить строку в файл истории
    fun saveNewResult(result: String) {
        file.appendText("$result\n")
    }

    //Прочитать все сохраненные строки из истории
    fun readResults(): List<String> {
        return file.readLines()
    }

    //Очистить файл истории
    fun clear() {
        file.writeText("")
    }
}