package com.quizapp

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.hoc081098.viewbindingdelegate.viewBinding
import com.quizapp.databinding.ActivityStatisticsBinding

class StatisticsActivity : AppCompatActivity(R.layout.activity_statistics) {
    private val binding by viewBinding<ActivityStatisticsBinding>()
    private lateinit var statisticsAdapter: StatisticsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //Обработка нажатия на стрелочку "Назад"
        binding.toolbar.setNavigationOnClickListener {
            finish()
        }

        //Настройка списка с историей
        setUpStatisticsRecyclerView()
    }

    private fun setUpStatisticsRecyclerView() {
        //Создать объект адаптера
        statisticsAdapter = StatisticsAdapter()

        //Получить список истории из файла
        val items = StatisticsSaver.readResults()
        Log.d("TAG", "Items - $items")
        //Если он не пуст
        if (items.isNotEmpty()) {
            //Добавить элементы в список и отобразить на экране
            statisticsAdapter.addItems(items)
        }

        //Присвоить списку адаптер
        binding.rvStatistics.adapter = statisticsAdapter
    }
}