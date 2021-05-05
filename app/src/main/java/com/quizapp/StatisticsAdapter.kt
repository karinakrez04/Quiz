package com.quizapp

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.quizapp.databinding.ItemResultBinding

class StatisticsAdapter :
    RecyclerView.Adapter<StatisticsAdapter.HistoryViewHolder>() {
    //Список строк истории вычислений
    //Один элемент имеет вид "Алекс - 2/10"
    private val resultItems = mutableListOf<String>()

    fun addItems(items: List<String>) {
        if (resultItems.isEmpty()) {
            resultItems.addAll(items)
            notifyItemRangeInserted(0, items.size)
        }
    }

    fun addItem(item: String) {
        resultItems.add(item)
        notifyItemInserted(resultItems.lastIndex)
    }

    fun clear() {
        val itemCount = resultItems.size
        resultItems.clear()
        notifyItemRangeRemoved(0, itemCount)
    }

    //Создаёт элемент списка
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        return HistoryViewHolder(
            ItemResultBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    //Наполняет данными элемент списка
    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        holder.bind(resultItems[position])
    }

    //Возвращает количество элементов списка
    override fun getItemCount(): Int = resultItems.size

    inner class HistoryViewHolder(private val binding: ItemResultBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(expression: String) {
            binding.resultItemId.text = expression
        }
    }
}